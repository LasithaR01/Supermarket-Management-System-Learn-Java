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

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Go
 */
public class SalePanel extends javax.swing.JPanel {

    public static String barcode_c = "0" ;
    public static String cus_id = "0";
    
    public SalePanel() {
        initComponents();
       data_load();
       stylePanel();
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
      
      // load last invoice number
      
      try {
          
        Statement s = ConnectionFactory.getConnection().createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM extra WHERE exid =1");
          
          if (rs.next()) {
              
              inid.setText(rs.getString("val"));
              
          }
          
      } catch (Exception e) {
      }
     
      // pluss new invoice
      int i = Integer.valueOf(inid.getText());
      i++;
      inid.setText(String.valueOf(i));
      
      
      
      
  }
    
 public void pro_tot_cal(){
 
  // product calculation
         
        Double qt = Double.valueOf(p_qty.getText());
        Double price = Double.valueOf(u_price.getText());
        Double tot ;
        
        tot = qt * price;
        
        tot_price.setText(String.valueOf(tot));
 
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
     
 Double paid = Double.valueOf(paid_amt.getText());
       Double tot = Double.valueOf(bill_tot.getText());
       Double due ;
       
       due =  paid -tot ;
       
       balance.setText(String.valueOf(due));
 
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
        // load unit price
       
        String  name =com_pro.getSelectedItem().toString();
        try {
            
            Statement s = ConnectionFactory.getConnection().createStatement();
            ResultSet rs = s.executeQuery("SELECT Bar_code,Price FROM product  WHERE Product_Name ='"+name+"'  ");
            if (rs.next()) {
                 
               
                u_price.setText(rs.getString("Price"));
                br_code.setText(rs.getString("Bar_code"));
             
                
                
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
        //add  cart to product details 
        
       
       DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        
        Vector v = new Vector();
        
        v.add(inid.getText()); // invoice id
        v.add(com_pro.getSelectedItem().toString()); // product name
        v.add(br_code.getText()); // barcode
        v.add(p_qty.getText()); // p qyt
        v.add(u_price.getText()); // unit price
        v.add(tot_price.getText()); // get totle price
        
        dt.addRow(v);
        
        cart_total();
         tot(); 
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // selected remove
        try {
            
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            int rw = jTable1.getSelectedRow();
           
            dt.removeRow(rw);
            
            
        } catch (Exception e) {
        }
        
        cart_total(); 
         tot(); 
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // remove all
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        dt.setRowCount(0);
        
         cart_total();
         tot(); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void paid_amtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paid_amtKeyReleased
        
      tot(); 
        
        
    }//GEN-LAST:event_paid_amtKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // data send to databace
        
        try {
            
            // `cartid`, `INID`, `Product_Name`, `Bar_code`, `qty`, `Unit_Price`, `Total_Price`
            
          DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
          int rc = dt.getRowCount();
          
            for (int i = 0; i < rc; i++) {
                
                String inid = dt.getValueAt(i, 0).toString(); // get inid
                String P_name = dt.getValueAt(i, 1).toString(); // get product name
                String bar_code = dt.getValueAt(i, 2).toString(); // get barcode
                String qty = dt.getValueAt(i, 3).toString(); // get product qty
                String un_price = dt.getValueAt(i, 4).toString(); // get product unit price
                String tot_price = dt.getValueAt(i, 5).toString(); // get product total Price
            
                // cart DB
             Statement s = ConnectionFactory.getConnection().createStatement();
             s.executeUpdate(" INSERT INTO cart (INID, Product_Name, Bar_code, qty, Unit_Price, Total_Price) VALUES ('"+inid+"','"+P_name+"','"+bar_code+"','"+qty+"','"+un_price+"','"+tot_price+"') ");
           
            }
            
                JOptionPane.showMessageDialog(null, "Saved");
            
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
  
        try {
            
            
            // sales DB
             
             //`saleid`, `INID`, `Cid`, `Customer_Name`, `Total_Qty`, `Total_Bill`, `Status`, `Balance`
            String inv_id = inid.getText();
            String cname  = com_cus.getSelectedItem().toString();
            String totqty = tot_qty.getText();
            String tot_bil = bill_tot.getText();           
            String blnc = balance.getText();
            
             // paid check
             
             Double tot = Double.valueOf(bill_tot.getText());
             Double pid = Double.valueOf(paid_amt.getText());
             String Status = null;
             if (pid.equals(0.0)) {
                 
                Status = "UnPaid";
                
            }else if (tot>pid) {
                 Status = "Partial";
                 
            }else if (tot<=pid) {
                Status = "Paid";
            }
             
            
            
             
             Statement ss = ConnectionFactory.getConnection().createStatement();
             ss.executeUpdate("INSERT INTO sales(INID, Cid, Customer_Name, Total_Qty, Total_Bill, Status, Balance) VALUES('"+inv_id+"','"+cus_id+"','"+cname+"','"+totqty+"','"+tot_bil+"','"+Status+"','"+blnc+"')");
             
            
            
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e);
        }
  
        // save las inid number
        try {
            
           String id = inid.getText(); 
            Statement s = ConnectionFactory.getConnection().createStatement();
            s.executeUpdate("UPDATE  extra SET val='"+id+"' WHERE exid = 1");
            
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        
       
        // Print or view ireport bill
        
        
        try {
            
           
        HashMap para = new HashMap();
        
        para.put("inv_id", inid.getText());  // inv_id  is ireport parameter name
        
        ReportView r =new ReportView(ReportPaths.jasper("print.jasper"), para);
        r.setVisible(true);  
            
            
            
        } catch (Exception e) {
        }
        
        
        
        
        
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
