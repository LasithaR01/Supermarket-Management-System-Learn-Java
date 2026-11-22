/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmart.ui.sale;

import cmart.ui.report.ReportView;


import cmart.ui.theme.UiTheme;

import cmart.util.ReportPaths;

import cmart.config.SettingKeys;
import cmart.model.CartItem;
import cmart.model.Customer;
import cmart.model.HeldSale;
import cmart.model.PayMethod;
import cmart.model.Product;
import cmart.service.CustomerService;
import cmart.service.ProductService;
import cmart.service.SaleCheckoutRequest;
import cmart.service.SaleService;
import cmart.service.SettingService;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Go
 */
public class SalePanel extends javax.swing.JPanel {

    public static String barcode_c = "0" ;
    public static String cus_id = "0";

    private final SaleService saleService = new SaleService();
    private final ProductService productService = new ProductService();
    private final CustomerService customerService = new CustomerService();
    private final SettingService settingService = new SettingService();

    private JTextField barcodeField;
    private JTextField discountField;
    private JTextField productSearchField;
    private JComboBox discountTypeBox;
    private JComboBox payMethodBox;
    private JLabel subtotalLabel;
    private JLabel taxLabel;
    private List<Product> allProducts = new ArrayList<Product>();
    private double lastTaxAmount = 0;
    
    public SalePanel() {
        initComponents();
       data_load();
       installTillExtras();
       stylePanel();
       bindShortcuts();
    }

  public void data_load(){
      try {
          List<String> customerNames = customerService.listNames();
          com_cus.setModel(new DefaultComboBoxModel(new Vector(customerNames)));
      } catch (SQLException e) {
            System.out.println(e);
      }

      try {
          allProducts = productService.listAll();
          refreshProductCombo("");
      } catch (SQLException e) {
            System.out.println(e);
      }

      try {
          inid.setText(saleService.peekNextInvoiceNumber());
      } catch (Exception e) {
          inid.setText("1");
          System.out.println(e);
      }
  }
    
 public void pro_tot_cal(){
 
  // product calculation
        try {
            Double qt = Double.valueOf(p_qty.getText().trim());
            Double price = Double.valueOf(u_price.getText().trim());
            tot_price.setText(String.valueOf(qt * price));
        } catch (NumberFormatException e) {
            tot_price.setText("0.00");
        }
 }   
 
 public void cart_total(){
    int numofrow = jTable1.getRowCount();
    double subtotal = 0;
    for (int i = 0; i < numofrow; i++) {
        subtotal += Double.valueOf(jTable1.getValueAt(i, 5).toString());
    }
    double discount = computeDiscount(subtotal);
    double afterDiscount = Math.max(0, subtotal - discount);
    lastTaxAmount = computeTax(afterDiscount);
    double net = afterDiscount + lastTaxAmount;
    if (subtotalLabel != null) {
        subtotalLabel.setText(String.valueOf(subtotal));
    }
    if (taxLabel != null) {
        taxLabel.setText(String.valueOf(lastTaxAmount));
    }
    bill_tot.setText(Double.toString(net));

    double totals = 0;
    for (int i = 0; i < numofrow; i++) {
        totals += Double.valueOf(jTable1.getValueAt(i, 3).toString());
    }
    tot_qty.setText(Double.toString(totals));
 }

    private double computeTax(double taxable) {
        try {
            if (!settingService.getBoolean(SettingKeys.TAX_ENABLED)) {
                return 0;
            }
            double rate = settingService.getDouble(SettingKeys.TAX_RATE_PERCENT, 0);
            if (rate <= 0) {
                return 0;
            }
            return taxable * (rate / 100.0);
        } catch (SQLException e) {
            return 0;
        }
    }

    private void refreshProductCombo(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        Vector names = new Vector();
        for (Product product : allProducts) {
            String name = product.getName() == null ? "" : product.getName();
            String code = product.getBarcode() == null ? "" : product.getBarcode();
            if (q.isEmpty()
                    || name.toLowerCase().contains(q)
                    || code.toLowerCase().contains(q)) {
                names.add(name);
            }
        }
        Object selected = com_pro.getSelectedItem();
        com_pro.setModel(new DefaultComboBoxModel(names));
        if (selected != null && names.contains(selected)) {
            com_pro.setSelectedItem(selected);
        } else if (!names.isEmpty()) {
            com_pro.setSelectedIndex(0);
        }
    }

    private double computeDiscount(double subtotal) {
        if (discountField == null) {
            return 0;
        }
        try {
            String raw = discountField.getText() == null ? "" : discountField.getText().trim();
            if (raw.isEmpty()) {
                return 0;
            }
            double value = Double.parseDouble(raw);
            if (value < 0) {
                return 0;
            }
            boolean percent = discountTypeBox != null && "%".equals(String.valueOf(discountTypeBox.getSelectedItem()));
            double discount = percent ? subtotal * (value / 100.0) : value;
            if (discount > subtotal) {
                discount = subtotal;
            }
            return discount;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
 
 public void tot(){
     try {
         String paidText = paid_amt.getText() == null ? "" : paid_amt.getText().trim();
         if (paidText.isEmpty()) {
             balance.setText("0.00");
             return;
         }
         Double paid = Double.valueOf(paidText);
         Double tot = Double.valueOf(bill_tot.getText());
         balance.setText(String.valueOf(paid - tot));
     } catch (NumberFormatException e) {
         balance.setText("0.00");
     }
 }

    private double qtyAlreadyInCart(String barcode) {
        double sum = 0;
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < dt.getRowCount(); i++) {
            if (barcode.equals(String.valueOf(dt.getValueAt(i, 2)))) {
                sum += Double.parseDouble(String.valueOf(dt.getValueAt(i, 3)));
            }
        }
        return sum;
    }

    private void refreshInvoicePreview() {
        try {
            inid.setText(saleService.peekNextInvoiceNumber());
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void clearCartAfterPay() {
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        dt.setRowCount(0);
        paid_amt.setText("");
        bill_tot.setText("0.00");
        tot_qty.setText("0");
        balance.setText("0.00");
        p_qty.setText("1");
        tot_price.setText("0.00");
        if (discountField != null) {
            discountField.setText("0");
        }
        if (barcodeField != null) {
            barcodeField.setText("");
            barcodeField.requestFocusInWindow();
        }
        if (subtotalLabel != null) {
            subtotalLabel.setText("0.00");
        }
        refreshInvoicePreview();
    }

    private void installTillExtras() {
        barcodeField = new JTextField();
        barcodeField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        barcodeField.setToolTipText("Scan or type barcode, then Enter");
        barcodeField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addByBarcode();
                    e.consume();
                }
            }
        });
        JPanel scanBar = new JPanel(new BorderLayout(8, 0));
        scanBar.setOpaque(false);
        JLabel scanLbl = new JLabel("Scan barcode");
        scanLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        scanBar.add(scanLbl, BorderLayout.WEST);
        scanBar.add(barcodeField, BorderLayout.CENTER);

        remove(entryCard);
        JPanel north = new JPanel(new BorderLayout(0, 8));
        north.setOpaque(false);
        north.add(scanBar, BorderLayout.NORTH);
        north.add(entryCard, BorderLayout.CENTER);
        add(north, BorderLayout.PAGE_START);

        discountField = new JTextField("0", 6);
        discountTypeBox = new JComboBox(new Object[] {"Rs", "%"});
        payMethodBox = new JComboBox(PayMethod.values());
        subtotalLabel = new JLabel("0.00");
        taxLabel = new JLabel("0.00");
        productSearchField = new JTextField(14);
        productSearchField.setToolTipText("Type product name or barcode");
        productSearchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshProductCombo(productSearchField.getText());
            }
        });
        discountField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                cart_total();
                tot();
            }
        });
        discountTypeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cart_total();
                tot();
            }
        });

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchRow.setOpaque(false);
        searchRow.add(new JLabel("Find product"));
        searchRow.add(productSearchField);

        JPanel extras = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        extras.setOpaque(false);
        extras.add(new JLabel("Subtotal"));
        extras.add(subtotalLabel);
        extras.add(new JLabel("Discount"));
        extras.add(discountField);
        extras.add(discountTypeBox);
        extras.add(new JLabel("Tax"));
        extras.add(taxLabel);
        extras.add(new JLabel("Pay"));
        extras.add(payMethodBox);

        JButton holdBtn = new JButton("Hold (F4)");
        holdBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                holdCurrentBill();
            }
        });
        JButton recallBtn = new JButton("Recall");
        recallBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recallHeldBill();
            }
        });
        cartButtons.add(javax.swing.Box.createVerticalStrut(8));
        cartButtons.add(holdBtn);
        cartButtons.add(javax.swing.Box.createVerticalStrut(8));
        cartButtons.add(recallBtn);

        remove(payCard);
        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setOpaque(false);
        JPanel mid = new JPanel(new BorderLayout(0, 6));
        mid.setOpaque(false);
        mid.add(searchRow, BorderLayout.NORTH);
        mid.add(extras, BorderLayout.CENTER);
        south.add(mid, BorderLayout.NORTH);
        south.add(payCard, BorderLayout.CENTER);
        add(south, BorderLayout.PAGE_END);
    }

    private void bindShortcuts() {
        jButton1.setText("Add to Cart (Enter)");
        jButton4.setText("Pay & Print (F2)");

        p_qty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton1.doClick();
                    evt.consume();
                }
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "salePay");
        getActionMap().put("salePay", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                jButton4.doClick();
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "saleHold");
        getActionMap().put("saleHold", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                holdCurrentBill();
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "saleClear");
        getActionMap().put("saleClear", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        SalePanel.this, "Clear the current cart?", "Clear cart", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    clearCartAfterPay();
                    tot();
                }
            }
        });

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (barcodeField != null) {
                    barcodeField.requestFocusInWindow();
                }
            }
        });
    }

    private void addByBarcode() {
        String code = barcodeField.getText() == null ? "" : barcodeField.getText().trim();
        if (code.isEmpty()) {
            return;
        }
        try {
            Product product = productService.getByBarcode(code);
            if (product == null) {
                JOptionPane.showMessageDialog(this, "No product for barcode: " + code);
                barcodeField.selectAll();
                return;
            }
            com_pro.setSelectedItem(product.getName());
            br_code.setText(product.getBarcode());
            u_price.setText(product.getPrice());
            if (p_qty.getText() == null || p_qty.getText().trim().isEmpty()) {
                p_qty.setText("1");
            }
            pro_tot_cal();
            jButton1.doClick();
            barcodeField.setText("");
            barcodeField.requestFocusInWindow();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Barcode lookup failed: " + e.getMessage());
        }
    }

    private List<CartItem> currentCartLines() {
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        List<CartItem> lines = new ArrayList<CartItem>();
        for (int i = 0; i < dt.getRowCount(); i++) {
            CartItem line = new CartItem();
            line.setProductName(dt.getValueAt(i, 1).toString());
            line.setBarcode(dt.getValueAt(i, 2).toString());
            line.setQty(dt.getValueAt(i, 3).toString());
            line.setUnitPrice(dt.getValueAt(i, 4).toString());
            line.setTotalPrice(dt.getValueAt(i, 5).toString());
            lines.add(line);
        }
        return lines;
    }

    private void holdCurrentBill() {
        try {
            HeldSale held = new HeldSale();
            held.setCustomerId(cus_id);
            held.setCustomerName(com_cus.getSelectedItem() == null ? "" : com_cus.getSelectedItem().toString());
            held.setLines(currentCartLines());
            String code = saleService.holdSale(held);
            JOptionPane.showMessageDialog(this, "Bill held as " + code);
            clearCartAfterPay();
            tot();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hold failed: " + e.getMessage());
        }
    }

    private void recallHeldBill() {
        try {
            List<HeldSale> heldList = saleService.listHeldSales();
            if (heldList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No held bills.");
                return;
            }
            String[] codes = new String[heldList.size()];
            for (int i = 0; i < heldList.size(); i++) {
                HeldSale h = heldList.get(i);
                codes[i] = h.getHoldCode() + " — " + h.getCustomerName() + " (" + h.getLines().size() + " lines)";
            }
            Object pick = JOptionPane.showInputDialog(
                    this, "Select held bill", "Recall",
                    JOptionPane.QUESTION_MESSAGE, null, codes, codes[0]);
            if (pick == null) {
                return;
            }
            String code = pick.toString().split(" — ")[0];
            HeldSale held = saleService.recallHeldSale(code);
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);
            for (CartItem line : held.getLines()) {
                Vector v = new Vector();
                v.add(inid.getText());
                v.add(line.getProductName());
                v.add(line.getBarcode());
                v.add(line.getQty());
                v.add(line.getUnitPrice());
                v.add(line.getTotalPrice());
                dt.addRow(v);
            }
            if (held.getCustomerName() != null && !held.getCustomerName().isEmpty()) {
                com_cus.setSelectedItem(held.getCustomerName());
                cus_id = held.getCustomerId() == null ? "0" : held.getCustomerId();
            }
            cart_total();
            tot();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Recall failed: " + e.getMessage());
        }
    }
 
 
 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        entryCard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        inid = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        com_cus = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        com_pro = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        p_qty = new javax.swing.JTextField();
        jLabelBarcode = new javax.swing.JLabel();
        br_code = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        u_price = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tot_price = new javax.swing.JLabel();
        cartPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cartButtons = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        payCard = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        paid_amt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tot_qty = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        bill_tot = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(243, 247, 244));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.BorderLayout(0, 12));

        entryCard.setBackground(java.awt.Color.WHITE);
        entryCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel2.setText("Invoice No");
        inid.setFont(new java.awt.Font("Segoe UI", 1, 16)); inid.setText("0");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel3.setText("Customer");
        com_cus.setFont(new java.awt.Font("Segoe UI", 0, 14));
        com_cus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { com_cusActionPerformed(evt); }
        });
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel4.setText("Product");
        com_pro.setFont(new java.awt.Font("Segoe UI", 0, 14));
        com_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { com_proActionPerformed(evt); }
        });
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel5.setText("Qty");
        p_qty.setFont(new java.awt.Font("Segoe UI", 0, 14));
        p_qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { p_qtyKeyReleased(evt); }
        });
        jLabelBarcode.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabelBarcode.setText("Barcode");
        br_code.setFont(new java.awt.Font("Segoe UI", 0, 14)); br_code.setText("-");
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel6.setText("Unit Price");
        u_price.setFont(new java.awt.Font("Segoe UI", 0, 14)); u_price.setText("0.00");
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel7.setText("Line Total");
        tot_price.setFont(new java.awt.Font("Segoe UI", 0, 14)); tot_price.setText("0.00");

        javax.swing.GroupLayout entryCardLayout = new javax.swing.GroupLayout(entryCard);
        entryCard.setLayout(entryCardLayout);
        entryCardLayout.setHorizontalGroup(
            entryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entryCardLayout.createSequentialGroup()
                .addGroup(entryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2).addComponent(inid)
                    .addComponent(jLabel3).addComponent(com_cus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBarcode).addComponent(br_code))
                .addGap(16, 16, 16)
                .addGroup(entryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4).addComponent(com_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6).addComponent(u_price))
                .addGap(16, 16, 16)
                .addGroup(entryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5).addComponent(p_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7).addComponent(tot_price)))
        );
        entryCardLayout.setVerticalGroup(
            entryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entryCardLayout.createSequentialGroup()
                .addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(inid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(entryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(entryCardLayout.createSequentialGroup()
                        .addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(com_cus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelBarcode).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(br_code))
                    .addGroup(entryCardLayout.createSequentialGroup()
                        .addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(com_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(u_price))
                    .addGroup(entryCardLayout.createSequentialGroup()
                        .addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tot_price))))
        );
        add(entryCard, java.awt.BorderLayout.PAGE_START);

        cartPanel.setOpaque(false);
        cartPanel.setLayout(new java.awt.BorderLayout(8, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Invoice No", "Product", "Barcode", "Qty", "Unit Price", "Total" }
        ));
        jTable1.setRowHeight(32);
        jScrollPane1.setViewportView(jTable1);
        cartPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        cartButtons.setOpaque(false);
        cartButtons.setLayout(new javax.swing.BoxLayout(cartButtons, javax.swing.BoxLayout.Y_AXIS));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); jButton1.setText("Add to Cart");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton1ActionPerformed(evt); }
        });
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton2ActionPerformed(evt); }
        });
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); jButton3.setText("Remove All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton3ActionPerformed(evt); }
        });
        cartButtons.add(jButton1);
        cartButtons.add(javax.swing.Box.createVerticalStrut(8));
        cartButtons.add(jButton2);
        cartButtons.add(javax.swing.Box.createVerticalStrut(8));
        cartButtons.add(jButton3);
        cartPanel.add(cartButtons, java.awt.BorderLayout.EAST);
        add(cartPanel, java.awt.BorderLayout.CENTER);

        payCard.setBackground(java.awt.Color.WHITE);
        payCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel8.setText("Paid Amount");
        paid_amt.setFont(new java.awt.Font("Segoe UI", 0, 14));
        paid_amt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { paid_amtKeyReleased(evt); }
        });
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel1.setText("Total Qty");
        tot_qty.setFont(new java.awt.Font("Segoe UI", 1, 16)); tot_qty.setText("0");
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel10.setText("Bill Total");
        bill_tot.setFont(new java.awt.Font("Segoe UI", 1, 18)); bill_tot.setText("0.00");
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel11.setText("Balance");
        balance.setFont(new java.awt.Font("Segoe UI", 1, 18)); balance.setText("0.00");
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); jButton4.setText("Pay & Print");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton4ActionPerformed(evt); }
        });

        javax.swing.GroupLayout payCardLayout = new javax.swing.GroupLayout(payCard);
        payCard.setLayout(payCardLayout);
        payCardLayout.setHorizontalGroup(
            payCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payCardLayout.createSequentialGroup()
                .addGroup(payCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8).addComponent(paid_amt, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10).addComponent(bill_tot))
                .addGap(24, 24, 24)
                .addGroup(payCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1).addComponent(tot_qty)
                    .addComponent(jLabel11).addComponent(balance))
                .addGap(24, 24, 24)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        payCardLayout.setVerticalGroup(
            payCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payCardLayout.createSequentialGroup()
                .addGroup(payCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(payCardLayout.createSequentialGroup()
                        .addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paid_amt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(bill_tot))
                    .addGroup(payCardLayout.createSequentialGroup()
                        .addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tot_qty)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(balance))
                    .addGroup(payCardLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        add(payCard, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void com_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_proActionPerformed
        if (com_pro.getSelectedItem() == null) {
            return;
        }
        String name = com_pro.getSelectedItem().toString();
        try {
            Product product = productService.getByName(name);
            if (product != null) {
                u_price.setText(product.getPrice());
                br_code.setText(product.getBarcode());
                if (p_qty.getText() == null || p_qty.getText().trim().isEmpty()) {
                    p_qty.setText("1");
                }
                p_qty.setToolTipText("In stock: " + product.getQty());
            }
            pro_tot_cal();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_com_proActionPerformed

    private void p_qtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_qtyKeyReleased
        
        pro_tot_cal();
         
    }//GEN-LAST:event_p_qtyKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (com_pro.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select a product first.");
            return;
        }

        String barcode = br_code.getText();
        String qtyText = p_qty.getText() == null ? "" : p_qty.getText().trim();
        double qty;
        try {
            qty = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid quantity.");
            p_qty.requestFocus();
            return;
        }

        try {
            String error = saleService.validateSaleQty(barcode, qty, qtyAlreadyInCart(barcode));
            if (error != null) {
                JOptionPane.showMessageDialog(this, error);
                p_qty.requestFocus();
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not check stock: " + e.getMessage());
            return;
        }

        pro_tot_cal();

        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        Vector v = new Vector();
        v.add(inid.getText());
        v.add(com_pro.getSelectedItem().toString());
        v.add(barcode);
        v.add(qtyText);
        v.add(u_price.getText());
        v.add(tot_price.getText());
        dt.addRow(v);

        cart_total();
        tot();
        p_qty.setText("1");
        pro_tot_cal();
        p_qty.requestFocus();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int rw = jTable1.getSelectedRow();
        if (rw < 0) {
            JOptionPane.showMessageDialog(this, "Select a cart line to remove.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove the selected line from the cart?",
                "Confirm remove",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        dt.removeRow(rw);
        cart_total();
        tot();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        if (dt.getRowCount() == 0) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove all items from the cart?",
                "Confirm remove all",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        dt.setRowCount(0);
        cart_total();
        tot();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void paid_amtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paid_amtKeyReleased
        
      tot(); 
        
        
    }//GEN-LAST:event_paid_amtKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        int rc = dt.getRowCount();
        if (rc == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }
        if (com_cus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select a customer.");
            return;
        }

        String paidText = paid_amt.getText() == null ? "" : paid_amt.getText().trim();
        if (paidText.isEmpty()) {
            paidText = "0";
            paid_amt.setText("0");
        }

        double paidAmt;
        double billTotal;
        double totalQty;
        try {
            paidAmt = Double.parseDouble(paidText);
            billTotal = Double.parseDouble(bill_tot.getText());
            totalQty = Double.parseDouble(tot_qty.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Paid amount is invalid.");
            paid_amt.requestFocus();
            return;
        }

        cart_total();
        tot();
        try {
            billTotal = Double.parseDouble(bill_tot.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bill total is invalid.");
            return;
        }

        List<CartItem> lines = currentCartLines();
        double subtotal = 0;
        for (CartItem line : lines) {
            subtotal += Double.parseDouble(line.getTotalPrice());
        }
        double discountAmt = computeDiscount(subtotal);
        PayMethod payMethod = payMethodBox == null
                ? PayMethod.CASH
                : (PayMethod) payMethodBox.getSelectedItem();

        SaleCheckoutRequest request = new SaleCheckoutRequest(
                cus_id,
                com_cus.getSelectedItem().toString(),
                paidAmt,
                billTotal,
                totalQty,
                discountAmt,
                lastTaxAmount,
                payMethod,
                lines);

        try {
            String inv_id = saleService.completeSale(request);
            inid.setText(inv_id);
            JOptionPane.showMessageDialog(this, "Sale saved. Invoice " + inv_id);

            HashMap para = new HashMap();
            para.put("inv_id", inv_id);
            ReportView r = new ReportView(ReportPaths.jasper("print.jasper"), para);
            r.setVisible(true);

            clearCartAfterPay();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not complete sale: " + e.getMessage());
            refreshInvoicePreview();
        } catch (Exception e) {
            System.out.println(e);
            clearCartAfterPay();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void com_cusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_cusActionPerformed
        if (com_cus.getSelectedItem() == null) {
            return;
        }
        String name = com_cus.getSelectedItem().toString();
        try {
            Customer customer = customerService.getByName(name);
            if (customer != null) {
                cus_id = customer.getId();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_com_cusActionPerformed


    private void stylePanel() {
        setBackground(UiTheme.PAGE);
        setOpaque(true);
        UiTheme.apply(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balance;
    private javax.swing.JLabel bill_tot;
    private javax.swing.JLabel br_code;
    private javax.swing.JPanel cartButtons;
    private javax.swing.JPanel cartPanel;
    private javax.swing.JComboBox<String> com_cus;
    private javax.swing.JComboBox<String> com_pro;
    private javax.swing.JPanel entryCard;
    private javax.swing.JLabel inid;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelBarcode;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField p_qty;
    private javax.swing.JTextField paid_amt;
    private javax.swing.JPanel payCard;
    private javax.swing.JLabel tot_price;
    private javax.swing.JLabel tot_qty;
    private javax.swing.JLabel u_price;
    // End of variables declaration//GEN-END:variables
}
