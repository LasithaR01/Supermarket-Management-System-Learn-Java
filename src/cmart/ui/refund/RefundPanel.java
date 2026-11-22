package cmart.ui.refund;

import cmart.service.ReturnService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RefundPanel extends JPanel {

    private final ReturnService returnService = new ReturnService();
    private final JTextField inidField = new JTextField(10);
    private final JTextField qtyField = new JTextField("1", 6);
    private final JTextField reasonField = new JTextField(20);
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[] {"Product", "Barcode", "Qty", "Unit", "Total"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public RefundPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel title = new JLabel("Returns / refunds");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        add(title, BorderLayout.NORTH);

        JPanel top = new JPanel(new GridLayout(2, 1, 6, 6));
        top.setOpaque(false);
        JPanel row1 = new JPanel();
        row1.setOpaque(false);
        row1.add(new JLabel("Invoice No"));
        row1.add(inidField);
        JButton load = new JButton("Load invoice lines");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { loadLines(); }
        });
        row1.add(load);
        JPanel row2 = new JPanel();
        row2.setOpaque(false);
        row2.add(new JLabel("Return qty"));
        row2.add(qtyField);
        row2.add(new JLabel("Reason"));
        row2.add(reasonField);
        JButton refund = new JButton("Refund selected line");
        refund.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { doRefund(); }
        });
        row2.add(refund);
        top.add(row1);
        top.add(row2);
        add(top, BorderLayout.PAGE_START);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadLines() {
        try {
            List<Object[]> lines = returnService.loadInvoiceLines(inidField.getText());
            model.setRowCount(0);
            for (Object[] line : lines) {
                model.addRow(line);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void doRefund() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a cart line.");
            return;
        }
        try {
            double qty = Double.parseDouble(qtyField.getText().trim());
            returnService.processReturn(
                    inidField.getText(),
                    String.valueOf(model.getValueAt(row, 1)),
                    qty,
                    reasonField.getText());
            JOptionPane.showMessageDialog(this, "Return saved and stock restocked.");
            loadLines();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
