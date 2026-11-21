/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmart.ui.sale;

import cmart.ui.report.ReportView;


import cmart.ui.theme.UiTheme;

import cmart.util.ReportPaths;

import cmart.db.ConnectionFactory;
import cmart.model.Product;
import cmart.service.ProductService;
import cmart.service.SaleService;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
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
    
    public SalePanel() {
        initComponents();
       data_load();
       stylePanel();
       bindShortcuts();
    }

  public void data_load(){
  
  // load customer
  
      try {
          
          Statement s= ConnectionFactory.getConnection().createStatement();
          
          ResultSet rs = s.executeQuery("SELECT * FROM customer");
          Vector v = new Vector();
          
          while (rs.next()) {              
              v.add(rs.getString("customer_name"));
              
              DefaultComboBoxModel com = new DefaultComboBoxModel(v);
              com_cus.setModel(com);
               
          }
           
      } catch (SQLException e) {
            System.out.println(e);
      }
     
     // load Product
  
      try {
          
          Statement s= ConnectionFactory.getConnection().createStatement();
          
          ResultSet rs = s.executeQuery("SELECT * FROM product");
          Vector v = new Vector();
          
          while (rs.next()) {              
              v.add(rs.getString("Product_Name"));
              
              DefaultComboBoxModel com = new DefaultComboBoxModel(v);
              com_pro.setModel(com);
               
          }
           
      } catch (SQLException e) {
            System.out.println(e);
      }  
      
      // next invoice preview (reserved only on successful Pay)
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
 
    double total = 0;
    
     for (int i = 0; i < numofrow; i++) {
         
         double value = Double.valueOf(jTable1.getValueAt(i, 5).toString());
         total += value ;
     }
    bill_tot.setText(Double.toString(total));
    
   /// total qty count 
   
   int numofrows = jTable1.getRowCount();
 
    double totals = 0;
    
     for (int i = 0; i < numofrows; i++) {
         
         double values = Double.valueOf(jTable1.getValueAt(i, 3).toString());
         totals += values ;
     }
    tot_qty.setText(Double.toString(totals));
    
 
 
 
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
        refreshInvoicePreview();
    }

    private void bindShortcuts() {
        jButton1.setText("Add to Cart (Enter)");
        jButton4.setText("Pay & Print (F2)");

        p_qty.addKeyListener(new java.awt.event.KeyAdapter() {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton4.doClick();
            }
        });
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
        try {
            paidAmt = Double.parseDouble(paidText);
            billTotal = Double.parseDouble(bill_tot.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Paid amount is invalid.");
            paid_amt.requestFocus();
            return;
        }

        // Block pay if any line exceeds live stock
        try {
            for (int i = 0; i < rc; i++) {
                String bar_code = dt.getValueAt(i, 2).toString();
                double qty = Double.parseDouble(dt.getValueAt(i, 3).toString());
                // alreadyInCart=0: each line checked alone; sum same barcode lines first
                double priorSameBarcode = 0;
                for (int j = 0; j < i; j++) {
                    if (bar_code.equals(dt.getValueAt(j, 2).toString())) {
                        priorSameBarcode += Double.parseDouble(dt.getValueAt(j, 3).toString());
                    }
                }
                String error = saleService.validateSaleQty(bar_code, qty, priorSameBarcode);
                if (error != null) {
                    JOptionPane.showMessageDialog(this, "Cannot pay:\n" + error);
                    return;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock check failed: " + e.getMessage());
            return;
        }

        String inv_id;
        try {
            inv_id = saleService.allocateInvoiceNumber();
            inid.setText(inv_id);
            for (int i = 0; i < rc; i++) {
                dt.setValueAt(inv_id, i, 0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not allocate invoice number: " + e.getMessage());
            return;
        }

        // Deduct stock first so we never save a bill without stock movement
        try {
            for (int i = 0; i < rc; i++) {
                String bar_code = dt.getValueAt(i, 2).toString();
                double qty = Double.parseDouble(dt.getValueAt(i, 3).toString());
                String stockError = saleService.deductStock(bar_code, qty);
                if (stockError != null) {
                    JOptionPane.showMessageDialog(this, stockError + "\nSale was not saved.");
                    refreshInvoicePreview();
                    return;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock deduct failed: " + e.getMessage());
            refreshInvoicePreview();
            return;
        }

        try {
            for (int i = 0; i < rc; i++) {
                String P_name = dt.getValueAt(i, 1).toString();
                String bar_code = dt.getValueAt(i, 2).toString();
                String qty = dt.getValueAt(i, 3).toString();
                String un_price = dt.getValueAt(i, 4).toString();
                String line_tot = dt.getValueAt(i, 5).toString();

                Statement s = ConnectionFactory.getConnection().createStatement();
                s.executeUpdate(" INSERT INTO cart (INID, Product_Name, Bar_code, qty, Unit_Price, Total_Price) VALUES ('"
                        + inv_id + "','" + P_name + "','" + bar_code + "','" + qty + "','" + un_price + "','" + line_tot + "') ");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not save cart lines: " + e.getMessage());
            System.out.println(e);
            refreshInvoicePreview();
            return;
        }

        try {
            String cname = com_cus.getSelectedItem().toString();
            String totqty = tot_qty.getText();
            String tot_bil = bill_tot.getText();
            tot();
            String blnc = balance.getText();
            String Status = SaleService.paymentStatus(billTotal, paidAmt);

            Statement ss = ConnectionFactory.getConnection().createStatement();
            ss.executeUpdate("INSERT INTO sales(INID, Cid, Customer_Name, Total_Qty, Total_Bill, Status, Balance) VALUES('"
                    + inv_id + "','" + cus_id + "','" + cname + "','" + totqty + "','" + tot_bil + "','" + Status + "','" + blnc + "')");
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not save sale: " + e.getMessage());
            System.out.println(e);
            refreshInvoicePreview();
            return;
        }

        JOptionPane.showMessageDialog(this, "Sale saved. Invoice " + inv_id);

        try {
            HashMap para = new HashMap();
            para.put("inv_id", inv_id);
            ReportView r = new ReportView(ReportPaths.jasper("print.jasper"), para);
            r.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }

        clearCartAfterPay();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void com_cusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_cusActionPerformed
        // get cid 
        
        String  name =com_cus.getSelectedItem().toString();
        try {
            
            Statement s = ConnectionFactory.getConnection().createStatement();
            ResultSet rs = s.executeQuery("SELECT cid,customer_name FROM customer  WHERE customer_name ='"+name+"'  ");
            if (rs.next()) {
                 
               
               cus_id = (rs.getString("cid"));
               
                
                
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
