/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmart.ui.employee;

import cmart.ui.theme.UiTheme;

import cmart.ui.report.ReportView;


import cmart.util.ReportPaths;

import cmart.model.Employee;
import cmart.model.PartyFilter;
import cmart.service.EmployeeService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Go
 */
public class EmployeePanel extends javax.swing.JPanel {

    private final EmployeeService employeeService = new EmployeeService();

    /**
     * Creates new form customer
     */
    public EmployeePanel() {
        
        initComponents();
        tb_load();
        stylePanel();
    }

  public void tb_load(){
      try {
          fillTable(employeeService.listAll());
      } catch (SQLException e) {
          System.out.println(e);
          JOptionPane.showMessageDialog(this, "Could not load employees: " + e.getMessage());
      }
  }

  private void fillTable(List<Employee> employees) {
      DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
      dt.setRowCount(0);
      for (Employee emp : employees) {
          Vector v = new Vector();
          v.add(emp.getId());
          v.add(emp.getName());
          v.add(emp.getPhone());
          v.add(emp.getMainAddress());
          v.add(emp.getTempAddress());
          v.add(emp.getBank());
          v.add(emp.getCity());
          v.add(emp.getPersonName());
          v.add(emp.getContactPerson());
          v.add(emp.getPersonPhone());
          v.add(emp.getEmail());
          v.add(emp.getOnline());
          dt.addRow(v);
      }
  }

  private Employee readForm() {
      Employee emp = new Employee();
      emp.setId(c_search.getText().trim());
      emp.setName(c_name.getText().trim());
      emp.setPhone(c_tp.getText().trim());
      emp.setMainAddress(c_billadd.getText().trim());
      emp.setTempAddress(c_shipadd.getText().trim());
      emp.setBank(c_bank.getText().trim());
      emp.setCity(c_city.getText().trim());
      emp.setPersonName(cp_name.getText().trim());
      emp.setContactPerson(c_person.getText().trim());
      emp.setPersonPhone(cp_tp.getText().trim());
      emp.setEmail(cp_email.getText().trim());
      emp.setOnline(cp_online.getText().trim());
      return emp;
  }

  private void writeForm(Employee emp) {
      c_name.setText(emp.getName());
      c_tp.setText(emp.getPhone());
      c_billadd.setText(emp.getMainAddress());
      c_shipadd.setText(emp.getTempAddress());
      c_bank.setText(emp.getBank());
      c_city.setText(emp.getCity());
      cp_name.setText(emp.getPersonName());
      c_person.setText(emp.getContactPerson());
      cp_tp.setText(emp.getPersonPhone());
      cp_email.setText(emp.getEmail());
      cp_online.setText(emp.getOnline());
  }
    
 public void serch(){
     PartyFilter filter = new PartyFilter();
     filter.setCity(sh_city.getText());
     filter.setPhone(sh_tp.getText());
     filter.setContactPerson(sh_cp.getText());
     filter.setPersonName(sh_pn.getText());
     try {
         fillTable(employeeService.search(filter));
     } catch (SQLException e) {
         System.out.println(e);
         tb_load();
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
        jButton7 = new javax.swing.JButton();
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
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel1.setText("Employee Name");
        c_name.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel2.setText("Phone");
        c_tp.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel6.setText("Billing Address");
        c_billadd.setColumns(20); c_billadd.setRows(3); c_billadd.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jScrollPane2.setViewportView(c_billadd);
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel7.setText("Temp Address");
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
        jTabbedPane1.addTab("Employee", detailsPanel);

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
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] { "ID", "Employee", "Phone", "Main Address", "Temp Address", "Bank", "City", "Person", "Contact", "Contact Phone", "Email", "Online" }));
        jTable1.setRowHeight(32);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { jTable1MouseClicked(evt); }
        });
        jScrollPane1.setViewportView(jTable1);
        findPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton7.setText("Edit");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton7ActionPerformed(evt); }
        });
        findPanel.add(jButton7, java.awt.BorderLayout.PAGE_END);
        jTabbedPane1.addTab("Find", findPanel);

        reportsPanel.setOpaque(false);
        reportsPanel.setLayout(new java.awt.BorderLayout());
        reportsCard.setBackground(java.awt.Color.WHITE);
        reportsCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(213, 221, 216), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton5.setText("All Employee Reports");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { jButton5ActionPerformed(evt); }
        });
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); jLabel4.setText("Employee ID");
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
        try {
            fillTable(employeeService.searchByName(c_search_tbl.getText()));
        } catch (Exception e) {
            tb_load();
        }
    }//GEN-LAST:event_c_search_tblKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // mouse clk & get data to textfeld

        

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
        
        ReportView r = new ReportView(ReportPaths.jasper("aLLCus.jasper"));
        r.setVisible(true);
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // serch customer for id
        
        HashMap para = new HashMap();
        para.put("Para_cid", cid.getText());
        
        ReportView r = new ReportView(ReportPaths.jasper("CIDCustomers.jasper"), para);
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
        try {
            employeeService.save(readForm());
            JOptionPane.showMessageDialog(this, "Saved");
            tb_load();
            clearText();
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
            Employee employee = employeeService.getById(c_search.getText().trim());
            if (employee != null) {
                writeForm(employee);
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            employeeService.update(readForm());
            JOptionPane.showMessageDialog(this, "Updated");
            tb_load();
            clearText();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not update: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this employee?", "Confirm delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            employeeService.delete(c_search.getText());
            JOptionPane.showMessageDialog(this, "Deleted");
            tb_load();
            clearText();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not delete: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void c_bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_bankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_bankActionPerformed

    private void sh_pnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sh_pnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sh_pnActionPerformed

    private void c_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_searchKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_c_searchKeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // send data to Editing From
        int r = jTable1.getSelectedRow();

        String id = jTable1.getValueAt(r, 0).toString();
        String name = jTable1.getValueAt(r, 1).toString();
        String tp = jTable1.getValueAt(r, 2).toString();
        String m_add = jTable1.getValueAt(r, 3).toString();
        String temp_add = jTable1.getValueAt(r, 4).toString();
        String bank = jTable1.getValueAt(r, 5).toString();
        String city = jTable1.getValueAt(r, 6).toString();
        String per_name = jTable1.getValueAt(r, 7).toString();
        String con_pre = jTable1.getValueAt(r, 8).toString();
        String p_tp = jTable1.getValueAt(r, 9).toString();
        String p_em = jTable1.getValueAt(r, 10).toString();
        String p_on = jTable1.getValueAt(r, 11).toString();
        

        c_search.setText(id);
        c_name.setText(name);
        c_tp.setText(tp);
        c_billadd.setText(m_add);
        c_shipadd.setText(temp_add);
        c_bank.setText(bank);
        c_city.setText(city);
        cp_name.setText(per_name);
        c_person.setText(con_pre);
        cp_tp.setText(p_tp);
        cp_email.setText(p_em);
        cp_online.setText(p_on);
    }//GEN-LAST:event_jButton7ActionPerformed


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
    private javax.swing.JButton jButton7;
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
