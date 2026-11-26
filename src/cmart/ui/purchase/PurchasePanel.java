package cmart.ui.purchase;

import cmart.model.CartItem;
import cmart.model.Product;
import cmart.model.Supplier;
import cmart.service.ProductService;
import cmart.service.PurchaseService;
import cmart.service.SupplierService;
import cmart.ui.NonNegativeNumberFilter;
import cmart.ui.theme.UiTheme;
import cmart.util.Validation;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PurchasePanel extends JPanel {

    private final PurchaseService purchaseService = new PurchaseService();
    private final SupplierService supplierService = new SupplierService();
    private final ProductService productService = new ProductService();

    private final JComboBox supplierBox = new JComboBox();
    private final JComboBox productBox = new JComboBox();
    private final JTextField qtyField = new JTextField("1", 6);
    private final DefaultTableModel linesModel = new DefaultTableModel(
            new Object[] {"Product", "Barcode", "Qty", "Unit cost"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable linesTable = new JTable(linesModel);
    private final DefaultTableModel ordersModel = new DefaultTableModel(
            new Object[] {"PO", "Supplier", "Status", "By", "Created"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable ordersTable = new JTable(ordersModel);
    private List<Supplier> suppliers = new ArrayList<Supplier>();
    private List<Product> products = new ArrayList<Product>();

    public PurchasePanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        JLabel title = new JLabel("Purchase");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        add(title, BorderLayout.NORTH);

        JPanel create = new JPanel(new GridLayout(0, 1, 6, 6));
        create.setOpaque(false);
        create.add(new JLabel("Supplier"));
        create.add(supplierBox);
        create.add(new JLabel("Product"));
        create.add(productBox);
        create.add(new JLabel("Qty"));
        create.add(qtyField);
        JButton addLine = new JButton("Add line");
        addLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addLine(); }
        });
        JButton savePo = new JButton("Create PO");
        savePo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { createPo(); }
        });
        JButton receive = new JButton("Receive selected PO (stock +)");
        receive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { receivePo(); }
        });
        JButton refresh = new JButton("Refresh open POs");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { reloadOrders(); }
        });
        create.add(addLine);
        create.add(savePo);
        create.add(receive);
        create.add(refresh);

        JPanel center = new JPanel(new GridLayout(1, 2, 8, 8));
        center.setOpaque(false);
        linesTable.setRowHeight(28);
        ordersTable.setRowHeight(28);
        center.add(new JScrollPane(linesTable));
        center.add(new JScrollPane(ordersTable));
        add(create, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        NonNegativeNumberFilter.install(qtyField);
        loadLookups();
        reloadOrders();
    }

    private void loadLookups() {
        try {
            suppliers = supplierService.listAll();
            Vector names = new Vector();
            for (Supplier s : suppliers) {
                names.add(s.getName());
            }
            supplierBox.setModel(new DefaultComboBoxModel(names));
            products = productService.listAll();
            Vector pnames = new Vector();
            for (Product p : products) {
                pnames.add(p.getName());
            }
            productBox.setModel(new DefaultComboBoxModel(pnames));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addLine() {
        if (productBox.getSelectedItem() == null) {
            return;
        }
        String name = productBox.getSelectedItem().toString();
        Product match = null;
        for (Product p : products) {
            if (name.equals(p.getName())) {
                match = p;
                break;
            }
        }
        if (match == null) {
            return;
        }
        String qty = qtyField.getText() == null ? "" : qtyField.getText().trim();
        if (!Validation.isPositiveNumber(qty)) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
            qtyField.requestFocus();
            return;
        }
        linesModel.addRow(new Object[] { match.getName(), match.getBarcode(), qty, match.getPrice() });
    }

    private void createPo() {
        try {
            if (supplierBox.getSelectedItem() == null) {
                throw new IllegalArgumentException("Select supplier");
            }
            String sName = supplierBox.getSelectedItem().toString();
            String sid = "";
            for (Supplier s : suppliers) {
                if (sName.equals(s.getName())) {
                    sid = s.getId();
                    break;
                }
            }
            List<CartItem> lines = new ArrayList<CartItem>();
            for (int i = 0; i < linesModel.getRowCount(); i++) {
                CartItem line = new CartItem();
                line.setProductName(String.valueOf(linesModel.getValueAt(i, 0)));
                line.setBarcode(String.valueOf(linesModel.getValueAt(i, 1)));
                line.setQty(String.valueOf(linesModel.getValueAt(i, 2)));
                line.setUnitPrice(String.valueOf(linesModel.getValueAt(i, 3)));
                lines.add(line);
            }
            int id = purchaseService.createOrder(sid, sName, lines);
            JOptionPane.showMessageDialog(this, "Purchase order #" + id + " created.");
            linesModel.setRowCount(0);
            reloadOrders();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void receivePo() {
        int row = ordersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an open PO.");
            return;
        }
        try {
            int poId = Integer.parseInt(String.valueOf(ordersModel.getValueAt(row, 0)));
            purchaseService.receive(poId);
            JOptionPane.showMessageDialog(this, "PO received — stock updated.");
            reloadOrders();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void reloadOrders() {
        try {
            ordersModel.setRowCount(0);
            for (Object[] row : purchaseService.listOpen()) {
                ordersModel.addRow(new Object[] { row[0], row[2], row[3], row[4], row[5] });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
