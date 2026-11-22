package cmart.ui.invoice;

import cmart.ui.report.ReportView;
import cmart.ui.theme.UiTheme;

import cmart.model.InvoiceFilter;
import cmart.model.Sale;
import cmart.service.InvoiceService;
import cmart.util.ReportPaths;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Go
 */
public class InvoicePanel extends javax.swing.JPanel {

    private final InvoiceService invoiceService = new InvoiceService();

    /**
     * Creates new form Invoice
     */
    public InvoicePanel() {
        initComponents();
        installReprintControls();
        datalad();
        stylePanel();
    }

    private void installReprintControls() {
        JButton reprint = new JButton("Reprint selected (or double-click)");
        reprint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reprintSelected();
            }
        });
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setOpaque(false);
        south.add(reprint);
        add(south, BorderLayout.PAGE_END);

        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    reprintSelected();
                }
            }
        });
    }

    private void reprintSelected() {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an invoice row first.");
            return;
        }
        String invId = String.valueOf(jTable1.getValueAt(row, 1));
        try {
            HashMap para = new HashMap();
            para.put("inv_id", invId);
            ReportView r = new ReportView(ReportPaths.jasper("print.jasper"), para);
            r.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open receipt: " + e.getMessage());
        }
    }

    
  public void datalad(){
      try {
          fillTable(invoiceService.listAll());
      } catch (SecurityException e) {
          JOptionPane.showMessageDialog(this, e.getMessage());
      } catch (SQLException e) {
          System.out.println(e);
          JOptionPane.showMessageDialog(this, "Could not load invoices: " + e.getMessage());
      }
  }

  private void fillTable(List<Sale> sales) {
      DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
      dt.setRowCount(0);
      for (Sale sale : sales) {
          Vector v = new Vector();
          v.add(sale.getSaleId());
          v.add(sale.getInvoiceId());
          v.add(sale.getCustomerId());
          v.add(sale.getCustomerName());
          v.add(sale.getTotalQty());
          v.add(sale.getTotalBill());
          v.add(sale.getStatus());
          v.add(sale.getBalance());
          dt.addRow(v);
      }
  }
    
 public void search_para(){
     InvoiceFilter filter = new InvoiceFilter();
     filter.setInvoiceId(inid.getText());
     filter.setCustomerName(cus_name.getText());
     Object status = com_status.getSelectedItem();
     filter.setStatus(status == null ? "" : status.toString());
     try {
         fillTable(invoiceService.search(filter));
     } catch (SecurityException e) {
         JOptionPane.showMessageDialog(this, e.getMessage());
     } catch (SQLException e) {
         System.out.println(e);
         datalad();
     }
 } 

  
    
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterCard = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        inid = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cus_name = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        com_status = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(243, 247, 244));
        setLayout(new java.awt.BorderLayout(0, 12));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        filterCard.setBackground(new java.awt.Color(255, 255, 255));
        filterCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel5.setText("Invoice No");
        inid.setFont(new java.awt.Font("Segoe UI", 0, 14));
        inid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { inidKeyReleased(evt); }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel6.setText("Customer");
        cus_name.setFont(new java.awt.Font("Segoe UI", 0, 14));
        cus_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { cus_nameKeyReleased(evt); }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel7.setText("Status");
        com_status.setFont(new java.awt.Font("Segoe UI", 0, 14));
        com_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "UnPaid", "Partial", "Paid", " " }));
        com_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { com_statusActionPerformed(evt); }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton1ActionPerformed(evt); }
        });

        javax.swing.GroupLayout filterCardLayout = new javax.swing.GroupLayout(filterCard);
        filterCard.setLayout(filterCardLayout);
        filterCardLayout.setHorizontalGroup(
            filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterCardLayout.createSequentialGroup()
                .addGroup(filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(inid, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(cus_name, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(com_status, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        filterCardLayout.setVerticalGroup(
            filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterCardLayout.createSequentialGroup()
                .addGroup(filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(filterCardLayout.createSequentialGroup()
                        .addGroup(filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5).addComponent(jLabel6).addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(filterCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cus_name, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(com_status, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0))
        );

        add(filterCard, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Sale ID", "Invoice No", "Customer ID", "Customer", "Total Qty", "Total Bill", "Status", "Balance" }
        ));
        jTable1.setRowHeight(32);
        jScrollPane1.setViewportView(jTable1);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void inidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inidKeyReleased

      search_para();

    }//GEN-LAST:event_inidKeyReleased

    private void cus_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cus_nameKeyReleased
        search_para();
    }//GEN-LAST:event_cus_nameKeyReleased

    private void com_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_statusActionPerformed
      search_para();
    }//GEN-LAST:event_com_statusActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        datalad();
    }//GEN-LAST:event_jButton1ActionPerformed


    private void stylePanel() {
        setBackground(UiTheme.PAGE);
        setOpaque(true);
        UiTheme.apply(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> com_status;
    private javax.swing.JTextField cus_name;
    private javax.swing.JPanel filterCard;
    private javax.swing.JTextField inid;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
