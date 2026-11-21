/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmart.ui.supplier;

import cmart.ui.theme.UiTheme;

import cmart.ui.report.ReportView;


import cmart.util.ReportPaths;

import cmart.db.ConnectionFactory;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Go
 */
public class SupplierPanel extends javax.swing.JPanel {

    /**
     * Creates new form customer
     */
    public SupplierPanel() {
        
        initComponents();
        tb_load();
        stylePanel();
    }

  public void tb_load(){
  
  
      try {
          
          DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
          dt.setRowCount(0);
          
          Statement s = ConnectionFactory.getConnection().createStatement();
          ResultSet rs = s.executeQuery(" SELECT * FROM supplier");
          
          while (rs.next()) {              
              
              Vector v = new Vector();
              
              v.add(rs.getString(1));
              v.add(rs.getString(2));
              v.add(rs.getString(3));
              v.add(rs.getString(4));
              v.add(rs.getString(5));
              v.add(rs.getString(6));
              v.add(rs.getString(7));
              v.add(rs.getString(8));
              v.add(rs.getString(9));
              v.add(rs.getString(10));
              v.add(rs.getString(11));
              v.add(rs.getString(12));
              
              dt.addRow(v);
                          
              
              
              
          }
          
      } catch (SQLException e) {
          System.out.println(e);
      }
  
  } 
    
 public void serch(){
 
     String city = sh_city.getText();
     String tp = sh_tp.getText();
     String contact_person = sh_cp.getText();
     String person_name = sh_pn.getText();
     
     
     try {
         
         DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
         dtm.setRowCount(0);
         
         Statement s = ConnectionFactory.getConnection().createStatement();
         ResultSet rs = s.executeQuery(" SELECT * FROM supplier WHERE city LIKE  '%"+ city +"%' AND tp_number LIKE '%"+ tp +"%' AND contact_person LIKE '%"+contact_person+"%' AND person_name LIKE '%"+person_name+"%' ");
         
        // You can Use OR or AND
         
          while (rs.next()) {              
              
              Vector v = new Vector();
              
              v.add(rs.getString(1));
              v.add(rs.getString(2));
              v.add(rs.getString(3));
              v.add(rs.getString(4));
              v.add(rs.getString(5));
              v.add(rs.getString(6));
              v.add(rs.getString(7));
              v.add(rs.getString(8));
              v.add(rs.getString(9));
              v.add(rs.getString(10));
              v.add(rs.getString(11));
              v.add(rs.getString(12));
              
              dtm.addRow(v);
          }
         
         
     } catch (Exception e) {
         
         System.out.println(e);
     }
  
     
     
     
 
 }   
    
 public void clearText(){
 
    c_name.setText("");
    c_tp.setText("");
    c_billadd.setText("");
    c_shipadd.setText("");
    c_bank.setText("");
    c_city.setText("");
    cp_name.setText("");
    c_person.setText("");
    cp_tp.setText("");
    cp_email.setText("");
    cp_online.setText("");
 
 
 
 
 }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        detailsPanel = new javax.swing.JPanel();
        detailsCard = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        c_search = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        c_name = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        c_tp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        c_billadd = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        c_shipadd = new javax.swing.JTextArea();
        same = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        c_bank = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        c_city = new javax.swing.JTextField();
        jLabelContactTitle = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cp_name = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        c_person = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cp_tp = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cp_email = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cp_online = new javax.swing.JTextField();
        actionsBar = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        findPanel = new javax.swing.JPanel();
        findCard = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        c_search_tbl = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        sh_city = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        sh_tp = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        sh_cp = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        sh_pn = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        reportsPanel = new javax.swing.JPanel();
        reportsCard = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cid = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();

        setBackground(new java.awt.Color(243, 247, 244));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14));

        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new java.awt.BorderLayout(0, 12));
        detailsCard.setBackground(java.awt.Color.WHITE);
        detailsCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel3.setText("Search ID");
        c_search.setFont(new java.awt.Font("Segoe UI", 0, 14)); c_search.setText("");
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/search x30.png")));
        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton3ActionPerformed(evt); }
        });
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel1.setText("Supplier Name");
        c_name.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel2.setText("Phone");
        c_tp.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel6.setText("Billing Address");
        c_billadd.setColumns(20); c_billadd.setRows(3); c_billadd.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jScrollPane2.setViewportView(c_billadd);
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel7.setText("Shipping Address");
        c_shipadd.setColumns(20); c_shipadd.setRows(3); c_shipadd.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jScrollPane4.setViewportView(c_shipadd);
        same.setFont(new java.awt.Font("Segoe UI", 0, 14)); same.setText("Shipping same as billing");
        same.setOpaque(false);
        same.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { sameActionPerformed(evt); }
        });
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel10.setText("Bank");
        c_bank.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel11.setText("City");
        c_city.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabelContactTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); jLabelContactTitle.setText("Contact person");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel8.setText("Person Name");
        cp_name.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel9.setText("Contact");
        c_person.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel12.setText("Contact Phone");
        cp_tp.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel13.setText("Email");
        cp_email.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel14.setText("Online");
        cp_online.setFont(new java.awt.Font("Segoe UI", 0, 14));

        javax.swing.GroupLayout detailsCardLayout = new javax.swing.GroupLayout(detailsCard);
        detailsCard.setLayout(detailsCardLayout);
        detailsCardLayout.setHorizontalGroup(
            detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsCardLayout.createSequentialGroup()
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3).addComponent(c_search, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1).addComponent(c_name, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(same)
                    .addComponent(jLabel10).addComponent(c_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelContactTitle)
                    .addComponent(jLabel8).addComponent(cp_name, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12).addComponent(cp_tp, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14).addComponent(cp_online, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2).addComponent(c_tp, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11).addComponent(c_city, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9).addComponent(c_person, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13).addComponent(cp_email, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        detailsCardLayout.setVerticalGroup(
            detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsCardLayout.createSequentialGroup()
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_search, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_name, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_tp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(same)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_city, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelContactTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cp_name, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cp_tp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cp_online, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(detailsCardLayout.createSequentialGroup()
                        .addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_person, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cp_email, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        detailsPanel.add(detailsCard, java.awt.BorderLayout.CENTER);
        actionsBar.setOpaque(false);
        actionsBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
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
        actionsBar.add(jButton4); actionsBar.add(jButton2); actionsBar.add(jButton1);
        detailsPanel.add(actionsBar, java.awt.BorderLayout.PAGE_END);
        jTabbedPane1.addTab("Supplier", detailsPanel);

        findPanel.setOpaque(false);
        findPanel.setLayout(new java.awt.BorderLayout(0, 12));
        findCard.setBackground(java.awt.Color.WHITE);
        findCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel5.setText("Name");
        c_search_tbl.setFont(new java.awt.Font("Segoe UI", 0, 14));
        c_search_tbl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { c_search_tblKeyReleased(evt); }
        });
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel16.setText("City");
        sh_city.setFont(new java.awt.Font("Segoe UI", 0, 14));
        sh_city.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { sh_cityKeyReleased(evt); }
        });
        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel19.setText("Telephone");
        sh_tp.setFont(new java.awt.Font("Segoe UI", 0, 14));
        sh_tp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { sh_tpKeyReleased(evt); }
        });
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel18.setText("Contact Person");
        sh_cp.setFont(new java.awt.Font("Segoe UI", 0, 14));
        sh_cp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { sh_cpKeyReleased(evt); }
        });
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel17.setText("Person Name");
        sh_pn.setFont(new java.awt.Font("Segoe UI", 0, 14));
        sh_pn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { sh_pnKeyReleased(evt); }
        });

        javax.swing.GroupLayout findCardLayout = new javax.swing.GroupLayout(findCard);
        findCard.setLayout(findCardLayout);
        findCardLayout.setHorizontalGroup(
            findCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(findCardLayout.createSequentialGroup()
                .addGroup(findCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5).addComponent(c_search_tbl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19).addComponent(sh_tp, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17).addComponent(sh_pn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(findCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16).addComponent(sh_city, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18).addComponent(sh_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        findCardLayout.setVerticalGroup(
            findCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(findCardLayout.createSequentialGroup()
                .addGroup(findCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(findCardLayout.createSequentialGroup()
                        .addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_search_tbl, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(findCardLayout.createSequentialGroup()
                        .addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sh_city, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(findCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(findCardLayout.createSequentialGroup()
                        .addComponent(jLabel19).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sh_tp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(findCardLayout.createSequentialGroup()
                        .addComponent(jLabel18).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sh_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sh_pn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        findPanel.add(findCard, java.awt.BorderLayout.PAGE_START);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] { "ID", "Supplier", "Phone", "Billing Address", "Shipping Address", "Bank", "City", "Person", "Contact", "Contact Phone", "Email", "Online" }));
        jTable1.setRowHeight(32);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { jTable1MouseClicked(evt); }
        });
        jScrollPane1.setViewportView(jTable1);
        findPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Find", findPanel);

        reportsPanel.setOpaque(false);
        reportsPanel.setLayout(new java.awt.BorderLayout());
        reportsCard.setBackground(java.awt.Color.WHITE);
        reportsCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton5.setText("All Supplier Reports");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton5ActionPerformed(evt); }
        });
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel4.setText("Supplier ID");
        cid.setFont(new java.awt.Font("Segoe UI", 0, 14)); cid.setText("");
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); jButton6.setText("View");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton6ActionPerformed(evt); }
        });
        javax.swing.GroupLayout reportsCardLayout = new javax.swing.GroupLayout(reportsCard);
        reportsCard.setLayout(reportsCardLayout);
        reportsCardLayout.setHorizontalGroup(
            reportsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(reportsCardLayout.createSequentialGroup()
                .addGroup(reportsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4).addComponent(cid, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        reportsCardLayout.setVerticalGroup(
            reportsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsCardLayout.createSequentialGroup()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(reportsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cid, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        reportsPanel.add(reportsCard, java.awt.BorderLayout.PAGE_START);
        jTabbedPane1.addTab("Reports", reportsPanel);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void c_search_tblKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_search_tblKeyReleased

        String name = c_search_tbl.getText();
        try {

            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);
            Statement s = ConnectionFactory.getConnection().createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM supplier WHERE supplier_Name LIKE '%"+name+"%' ");

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                v.add(rs.getString(8));
                v.add(rs.getString(9));
                v.add(rs.getString(10));
                v.add(rs.getString(11));
                v.add(rs.getString(12));
                

                dt.addRow(v);

            }

        } catch (Exception e) {
            tb_load();

        }

    }//GEN-LAST:event_c_search_tblKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // mouse clk & get data to textfeld

        int r = jTable1.getSelectedRow();

        String id = jTable1.getValueAt(r, 0).toString();
        String name = jTable1.getValueAt(r, 1).toString();
        String tp = jTable1.getValueAt(r, 2).toString();

        c_search.setText(id);
        c_name.setText(name);
        c_tp.setText(tp);

    }//GEN-LAST:event_jTable1MouseClicked

    private void c_search_tblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_search_tblActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_search_tblActionPerformed

    private void sh_cityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sh_cityKeyReleased
      serch();  
    }//GEN-LAST:event_sh_cityKeyReleased

    private void sh_tpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sh_tpKeyReleased
       serch(); 
    }//GEN-LAST:event_sh_tpKeyReleased

    private void sh_cpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sh_cpKeyReleased
        serch();
    }//GEN-LAST:event_sh_cpKeyReleased

    private void sh_pnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sh_pnKeyReleased
        serch();
    }//GEN-LAST:event_sh_pnKeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Open All Customers
        
        ReportView r = new ReportView(ReportPaths.jasper("Allsupplier.jasper"));
        r.setVisible(true);
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // serch customer for id
        
        HashMap para = new HashMap();
        para.put("Para_sid", cid.getText());
        
        ReportView r = new ReportView(ReportPaths.jasper("sidsupplier.jasper"), para);
        r.setVisible(true);
        
        
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void sameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sameActionPerformed
        // Same as Billing code

        if (same.isSelected()) {
            c_shipadd.setText(c_billadd.getText());
        }else{
            c_shipadd.setText("");
        }
    }//GEN-LAST:event_sameActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        String name = c_name.getText();
        String tp = c_tp.getText();

        String bill_add = c_billadd.getText();
        String shipp_add = c_shipadd.getText();
        String bank = c_bank.getText();
        String city = c_city.getText();
        String person_name = cp_name.getText();
        String contact_person = c_person.getText();
        String person_tp = cp_tp.getText();
        String email = cp_email.getText();
        String online = cp_online.getText();

        try {

            Statement s = ConnectionFactory.getConnection().createStatement();
            s.executeUpdate(" INSERT INTO supplier (supplier_Name,Tp_Number,billing_address,shipping_address,bank,city,person_name,contact_person,person_tp,email,online) "
                + "VALUES ('"+name+"','"+tp+"','"+bill_add+"','"+shipp_add+"','"+bank+"','"+city+"','"+person_name+"','"+contact_person+"','"+person_tp+"','"+email+"','"+online+"')");

            JOptionPane.showMessageDialog(null, "Saved");

        } catch (SQLException e) {

            System.out.println(e);

        }

        tb_load();
        clearText();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // search btn code
        String search = c_search.getText();
        try {

            Statement s = ConnectionFactory.getConnection().createStatement();

            ResultSet rs = s.executeQuery(" SELECT * FROM supplier WHERE sid = '"+search+"'");

            if (rs.next()) {

                c_name.setText(rs.getString("supplier_Name"));
                c_tp.setText(rs.getString("Tp_Number"));
                c_billadd.setText(rs.getString("billing_address"));
                c_shipadd.setText(rs.getString("shipping_address"));
                c_bank.setText(rs.getString("bank"));
                c_city.setText(rs.getString("city"));

                cp_name.setText(rs.getString("person_name"));
                c_person.setText(rs.getString("contact_person"));
                cp_tp.setText(rs.getString("person_tp"));
                cp_email.setText(rs.getString("email"));
                cp_online.setText(rs.getString("online"));

            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // update btn code

        String id = c_search.getText();

        String name = c_name.getText();
        String tp = c_tp.getText();

        String bill_add = c_billadd.getText();
        String shipp_add = c_shipadd.getText();
        String bank = c_bank.getText();
        String city = c_city.getText();
        String person_name = cp_name.getText();
        String contact_person = c_person.getText();
        String person_tp = cp_tp.getText();
        String email = cp_email.getText();
        String online = cp_online.getText();

        try {

            Statement s = ConnectionFactory.getConnection().createStatement();
            s.executeUpdate(" UPDATE supplier SET supplier_Name ='"+name+"'"
                + " ,Tp_Number ='"+tp+"'"
                + ",billing_address ='"+ bill_add +"'"
                + ",shipping_address ='"+ shipp_add +"' "
                + ",bank ='"+ bank +"' "
                + ",city ='"+ city +"' "
                + ",person_name ='"+ person_name +"' "
                + ",contact_person ='"+ contact_person +"' "
                + ",person_tp ='"+ person_tp +"' "
                + ",email ='"+ email +"' "
                + ",online ='"+ online +"' "
                + " WHERE sid = '"+id+"' ");
            JOptionPane.showMessageDialog(null, "Updated");

        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }

        tb_load();
        clearText();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //delete btn code

        String id = c_search.getText();
        try {

            Statement s = ConnectionFactory.getConnection().createStatement();
            s.executeUpdate("DELETE FROM supplier WHERE sid = '"+id+"'");
            JOptionPane.showMessageDialog(null, "Deleted");

        } catch (SQLException e) {
            System.out.println(e);
        }

        tb_load();
        clearText();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void c_bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_bankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_bankActionPerformed

    private void sh_pnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sh_pnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sh_pnActionPerformed


    private void stylePanel() {
        setBackground(UiTheme.PAGE);
        setOpaque(true);
        UiTheme.apply(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionsBar;
    private javax.swing.JTextField c_bank;
    private javax.swing.JTextArea c_billadd;
    private javax.swing.JTextField c_city;
    private javax.swing.JTextField c_name;
    private javax.swing.JTextField c_person;
    private javax.swing.JTextField c_search;
    private javax.swing.JTextField c_search_tbl;
    private javax.swing.JTextArea c_shipadd;
    private javax.swing.JTextField c_tp;
    private javax.swing.JTextField cid;
    private javax.swing.JTextField cp_email;
    private javax.swing.JTextField cp_name;
    private javax.swing.JTextField cp_online;
    private javax.swing.JTextField cp_tp;
    private javax.swing.JPanel detailsCard;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JPanel findCard;
    private javax.swing.JPanel findPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelContactTitle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel reportsCard;
    private javax.swing.JPanel reportsPanel;
    private javax.swing.JCheckBox same;
    private javax.swing.JTextField sh_city;
    private javax.swing.JTextField sh_cp;
    private javax.swing.JTextField sh_pn;
    private javax.swing.JTextField sh_tp;
    // End of variables declaration//GEN-END:variables
}
