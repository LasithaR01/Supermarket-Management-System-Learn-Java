/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmart.ui.product;


import cmart.ui.NonNegativeNumberFilter;
import cmart.ui.theme.UiTheme;

import cmart.model.Product;
import cmart.service.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Go
 */
public class ProductPanel extends javax.swing.JPanel {

    private final ProductService productService = new ProductService();

    /**
     * Creates new form customer
     */
    public ProductPanel() {
        initComponents();
        NonNegativeNumberFilter.install(p_qty);
        tb_load();
        stylePanel();
    }

  public void tb_load(){
      try {
          fillTable(productService.listAll());
      } catch (SQLException e) {
          System.out.println(e);
          JOptionPane.showMessageDialog(this, "Could not load products: " + e.getMessage());
      }
  }

  private void fillTable(List<Product> products) {
      DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
      dt.setRowCount(0);
      for (Product p : products) {
          Vector v = new Vector();
          v.add(p.getId());
          v.add(p.getName());
          v.add(p.getBarcode());
          v.add(p.getPrice());
          v.add(p.getQty());
          v.add(p.getSupplierId());
          dt.addRow(v);
      }
  }

  private Product readForm() {
      Product p = new Product();
      p.setId(p_src.getText().trim());
      p.setName(p_name.getText().trim());
      p.setBarcode(p_bcode.getText().trim());
      p.setPrice(p_price.getText().trim());
      p.setQty(p_qty.getText().trim());
      p.setSupplierId(p_sid.getText().trim());
      return p;
  }

  private static String nonNegativeQty(String qty) {
      String value = qty == null ? "" : qty.trim();
      if (NonNegativeNumberFilter.isNonNegativeNumber(value)) {
          return value;
      }
      return "0";
  }

  private void writeForm(Product p) {
      p_name.setText(p.getName());
      p_bcode.setText(p.getBarcode());
      p_price.setText(p.getPrice());
      p_qty.setText(nonNegativeQty(p.getQty()));
      p_sid.setText(p.getSupplierId());
  }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formCard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        p_name = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        p_bcode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        p_price = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        p_qty = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        p_sid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        p_src = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        actionsBar = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        listPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        c_search_tbl = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(243, 247, 244));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.BorderLayout(0, 12));

        formCard.setBackground(new java.awt.Color(255, 255, 255));
        formCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel1.setText("Name");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel2.setText("Barcode");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel5.setText("Price");
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel7.setText("Qty");
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel6.setText("Supplier ID");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel3.setText("Search ID");
        jLabel4.setVisible(false);

        p_name.setFont(new java.awt.Font("Segoe UI", 0, 14)); p_name.setText("");
        p_bcode.setFont(new java.awt.Font("Segoe UI", 0, 14)); p_bcode.setText("");
        p_bcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { p_bcodeActionPerformed(evt); }
        });
        p_price.setFont(new java.awt.Font("Segoe UI", 0, 14)); p_price.setText("");
        p_qty.setFont(new java.awt.Font("Segoe UI", 0, 14)); p_qty.setText("");
        p_sid.setFont(new java.awt.Font("Segoe UI", 0, 14)); p_sid.setText("");
        p_src.setFont(new java.awt.Font("Segoe UI", 0, 14)); p_src.setText("");

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/search x30.png")));
        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton3ActionPerformed(evt); }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/save.png")));
        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton4ActionPerformed(evt); }
        });
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/update.png")));
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton2ActionPerformed(evt); }
        });
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/delete.png")));
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton1ActionPerformed(evt); }
        });

        actionsBar.setOpaque(false);
        actionsBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        actionsBar.add(jButton4);
        actionsBar.add(jButton2);
        actionsBar.add(jButton1);

        javax.swing.GroupLayout formCardLayout = new javax.swing.GroupLayout(formCard);
        formCard.setLayout(formCardLayout);
        formCardLayout.setHorizontalGroup(
            formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formCardLayout.createSequentialGroup()
                .addGroup(formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1).addComponent(p_name, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5).addComponent(p_price, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6).addComponent(p_sid, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2).addComponent(p_bcode, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7).addComponent(p_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3).addComponent(p_src, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(actionsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        formCardLayout.setVerticalGroup(
            formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formCardLayout.createSequentialGroup()
                .addGroup(formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formCardLayout.createSequentialGroup()
                        .addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_name, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_price, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_sid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formCardLayout.createSequentialGroup()
                        .addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_bcode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_src, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(actionsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        add(formCard, java.awt.BorderLayout.PAGE_START);

        listPanel.setOpaque(false);
        listPanel.setLayout(new java.awt.BorderLayout(0, 8));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel8.setText("Find by name");
        c_search_tbl.setFont(new java.awt.Font("Segoe UI", 0, 14));
        c_search_tbl.setText("");
        c_search_tbl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { c_search_tblKeyReleased(evt); }
        });
        javax.swing.JPanel findRow = new javax.swing.JPanel(new java.awt.BorderLayout(0, 4));
        findRow.setOpaque(false);
        findRow.add(jLabel8, java.awt.BorderLayout.NORTH);
        findRow.add(c_search_tbl, java.awt.BorderLayout.CENTER);
        listPanel.add(findRow, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Product", "Barcode", "Price", "Qty", "Supplier ID" }
        ));
        jTable1.setRowHeight(32);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { jTable1MouseClicked(evt); }
        });
        jScrollPane1.setViewportView(jTable1);
        listPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(listPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void p_bcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_bcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_p_bcodeActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            productService.save(readForm());
            JOptionPane.showMessageDialog(this, "Saved");
            tb_load();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not save: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            Product product = productService.getById(p_src.getText().trim());
            if (product != null) {
                writeForm(product);
            } else {
                JOptionPane.showMessageDialog(this, "Product not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            productService.update(readForm());
            JOptionPane.showMessageDialog(this, "Data Updated");
            tb_load();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not update: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            productService.delete(p_src.getText());
            JOptionPane.showMessageDialog(this, "Data Deleted");
            tb_load();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not delete: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
     
        
        
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
       int r = jTable1.getSelectedRow();
       
       String id  = jTable1.getValueAt(r, 0).toString();
       String name  = jTable1.getValueAt(r, 1).toString();
       String bcode  = jTable1.getValueAt(r, 2).toString();
       String price  = jTable1.getValueAt(r, 3).toString();
       String qty  = jTable1.getValueAt(r, 4).toString();
       String sid  = jTable1.getValueAt(r, 5).toString();
       
        p_src.setText(id);
        p_name.setText(name);
        p_bcode.setText(bcode);
        p_price.setText(price);
        p_qty.setText(nonNegativeQty(qty));
        p_sid.setText(sid);
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void c_search_tblKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_search_tblKeyReleased
        try {
            fillTable(productService.searchByName(c_search_tbl.getText()));
        } catch (Exception e) {
            tb_load();
        }
    }//GEN-LAST:event_c_search_tblKeyReleased


    private void stylePanel() {
        setBackground(UiTheme.PAGE);
        setOpaque(true);
        UiTheme.apply(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionsBar;
    private javax.swing.JTextField c_search_tbl;
    private javax.swing.JPanel formCard;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel listPanel;
    private javax.swing.JTextField p_bcode;
    private javax.swing.JTextField p_name;
    private javax.swing.JTextField p_price;
    private javax.swing.JTextField p_qty;
    private javax.swing.JTextField p_sid;
    private javax.swing.JTextField p_src;
    // End of variables declaration//GEN-END:variables
}
