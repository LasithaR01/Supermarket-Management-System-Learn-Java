
package cmart.ui.shell;

import cmart.ui.report.ReportsPanel;

import cmart.ui.invoice.InvoicePanel;

import cmart.ui.sale.SalePanel;

import cmart.ui.product.ProductPanel;

import cmart.ui.employee.EmployeePanel;

import cmart.ui.supplier.SupplierPanel;

import cmart.ui.customer.CustomerPanel;

import cmart.ui.theme.UiTheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class HomeFrame extends javax.swing.JFrame {

    PanelLoader jpload = new PanelLoader();
    private JLabel screenTitle;

    public HomeFrame() {
        initComponents();
        buildShell();
        this.setExtendedState(HomeFrame.MAXIMIZED_BOTH);
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        home_bnt_grp = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        panel_load = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        textField1 = new java.awt.TextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        home_bnt_grp.add(jToggleButton1);
        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/customer.png"))); // NOI18N
        jToggleButton1.setText("Customers");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        home_bnt_grp.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/supplier.png"))); // NOI18N
        jToggleButton2.setText("supplier");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        home_bnt_grp.add(jToggleButton3);
        jToggleButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/product.png"))); // NOI18N
        jToggleButton3.setText("Product");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        home_bnt_grp.add(jToggleButton4);
        jToggleButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/emp.png"))); // NOI18N
        jToggleButton4.setText("employee");
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        home_bnt_grp.add(jToggleButton5);
        jToggleButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/sales_menu.png"))); // NOI18N
        jToggleButton5.setText("Sales");
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        home_bnt_grp.add(jToggleButton6);
        jToggleButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/invo.png"))); // NOI18N
        jToggleButton6.setText("Invoice");
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        home_bnt_grp.add(jToggleButton7);
        jToggleButton7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/reports.png"))); // NOI18N
        jToggleButton7.setText("Reports");
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton7)
                .addContainerGap(206, Short.MAX_VALUE))
        );

        panel_load.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panel_loadLayout = new javax.swing.GroupLayout(panel_load);
        panel_load.setLayout(panel_loadLayout);
        panel_loadLayout.setHorizontalGroup(
            panel_loadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1056, Short.MAX_VALUE)
        );
        panel_loadLayout.setVerticalGroup(
            panel_loadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textField1.setFont(new java.awt.Font("Gadugi", 1, 24)); // NOI18N
        textField1.setForeground(new java.awt.Color(0, 102, 51));
        textField1.setText("CMart");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(563, 563, 563)
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_load, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_load, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // customer load
        
        CustomerPanel cus = new CustomerPanel();
        jpload.jPanelLoader(panel_load, cus);
       
        
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
       
      SupplierPanel sp = new SupplierPanel();
      jpload.jPanelLoader(panel_load, sp);
      
        
        
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // emp load
        
        EmployeePanel emp = new EmployeePanel();
        jpload.jPanelLoader(panel_load, emp);
        
        
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // pro load
        ProductPanel pro = new ProductPanel();
        jpload.jPanelLoader(panel_load, pro);
        
        
        
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        
       SalePanel sl = new SalePanel();
       jpload.jPanelLoader(panel_load, sl);
       
        
        
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
       
        InvoicePanel inv = new InvoicePanel();
        jpload.jPanelLoader(panel_load, inv);
        
        
        
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
       
        ReportsPanel rp = new ReportsPanel();
        jpload.jPanelLoader(panel_load, rp);
        
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        UiTheme.install();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }

    private void buildShell() {
        setTitle("CMart — Supermarket");
        setMinimumSize(new Dimension(1100, 700));
        getContentPane().removeAll();
        getContentPane().setBackground(UiTheme.PAGE);
        getContentPane().setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.SIDEBAR);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel brand = new JLabel("CMart");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 26));
        brand.setForeground(Color.WHITE);
        JLabel tag = new JLabel("Supermarket desk");
        tag.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tag.setForeground(new Color(0xB7E4C7));

        JPanel brandBox = new JPanel();
        brandBox.setOpaque(false);
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        brandBox.add(brand);
        brandBox.add(Box.createVerticalStrut(2));
        brandBox.add(tag);
        brandBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        brandBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showWelcome();
            }
        });
        header.add(brandBox, BorderLayout.WEST);

        screenTitle = new JLabel("Home");
        screenTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        screenTitle.setForeground(Color.WHITE);
        header.add(screenTitle, BorderLayout.EAST);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UiTheme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(new EmptyBorder(18, 12, 18, 12));

        sidebar.add(homeButton());
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sectionLabel("SHOP"));
        sidebar.add(navButton(jToggleButton5, "Sales"));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(navButton(jToggleButton6, "Invoices"));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(navButton(jToggleButton3, "Products"));
        sidebar.add(Box.createVerticalStrut(18));
        sidebar.add(sectionLabel("PEOPLE"));
        sidebar.add(navButton(jToggleButton1, "Customers"));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(navButton(jToggleButton2, "Suppliers"));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(navButton(jToggleButton4, "Employees"));
        sidebar.add(Box.createVerticalStrut(18));
        sidebar.add(sectionLabel("OTHER"));
        sidebar.add(navButton(jToggleButton7, "Reports"));
        sidebar.add(Box.createVerticalGlue());

        panel_load.setLayout(new BorderLayout());
        panel_load.setBackground(UiTheme.PAGE);
        panel_load.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(sidebar, BorderLayout.WEST);
        body.add(panel_load, BorderLayout.CENTER);

        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
        showWelcome();
    }

    private JButton homeButton() {
        JButton home = new JButton("Home");
        home.setUI(new BasicButtonUI());
        home.setFont(UiTheme.FONT_BOLD);
        home.setForeground(new Color(0x143D2C));
        home.setBackground(new Color(0xD8F3DC));
        home.setHorizontalAlignment(SwingConstants.LEFT);
        home.setFocusPainted(false);
        home.setBorderPainted(false);
        home.setContentAreaFilled(true);
        home.setOpaque(true);
        home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        home.setBorder(new EmptyBorder(10, 12, 10, 12));
        home.setAlignmentX(Component.LEFT_ALIGNMENT);
        home.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWelcome();
            }
        });
        return home;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(0x95D5B2));
        label.setBorder(new EmptyBorder(0, 8, 8, 8));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JToggleButton navButton(final JToggleButton button, final String title) {
        button.setText(title);
        button.setUI(new BasicButtonUI());
        button.setFont(UiTheme.FONT);
        button.setForeground(new Color(0xE8F5E9));
        button.setBackground(UiTheme.SIDEBAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(12);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 12, 10, 12));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (button.isSelected()) {
                    screenTitle.setText(title);
                }
                refreshNavColors();
            }
        });
        return button;
    }

    private void refreshNavColors() {
        JToggleButton[] buttons = {
            jToggleButton1, jToggleButton2, jToggleButton3, jToggleButton4,
            jToggleButton5, jToggleButton6, jToggleButton7
        };
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isSelected()) {
                buttons[i].setBackground(UiTheme.NAV_ACTIVE);
                buttons[i].setForeground(Color.WHITE);
            } else {
                buttons[i].setBackground(UiTheme.SIDEBAR);
                buttons[i].setForeground(new Color(0xE8F5E9));
            }
        }
    }

    private void showWelcome() {
        home_bnt_grp.clearSelection();
        screenTitle.setText("Home");
        refreshNavColors();

        JPanel welcome = new JPanel();
        welcome.setOpaque(false);
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.setBorder(new EmptyBorder(12, 8, 8, 8));

        JLabel hello = new JLabel("What do you want to do?");
        hello.setFont(new Font("Segoe UI", Font.BOLD, 28));
        hello.setForeground(UiTheme.TEXT);
        hello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hint = new JLabel("Pick a task, or use the menu on the left.");
        hint.setFont(UiTheme.FONT);
        hint.setForeground(UiTheme.MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setBorder(new EmptyBorder(6, 0, 18, 0));

        JPanel grid = new JPanel(new GridLayout(0, 3, 12, 12));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(860, 280));
        grid.add(welcomeCard("New sale", "Add items and take payment", jToggleButton5));
        grid.add(welcomeCard("Invoices", "Look up a past sale", jToggleButton6));
        grid.add(welcomeCard("Products", "Prices, barcodes, and stock", jToggleButton3));
        grid.add(welcomeCard("Customers", "Names and contact details", jToggleButton1));
        grid.add(welcomeCard("Suppliers", "People you buy stock from", jToggleButton2));
        grid.add(welcomeCard("Employees", "Staff records", jToggleButton4));
        grid.add(welcomeCard("Reports", "Print sales and lists", jToggleButton7));

        welcome.add(hello);
        welcome.add(hint);
        welcome.add(grid);

        panel_load.removeAll();
        panel_load.setLayout(new BorderLayout());
        panel_load.add(welcome, BorderLayout.CENTER);
        panel_load.revalidate();
        panel_load.repaint();
    }

    private JButton welcomeCard(String title, String detail, final JToggleButton target) {
        JButton card = new JButton("<html><div style='width:180px'><b>" + title
                + "</b><br><span style='font-weight:normal;color:#5C6B73'>" + detail + "</span></div></html>");
        card.setUI(new BasicButtonUI());
        card.setHorizontalAlignment(SwingConstants.LEFT);
        card.setBackground(Color.WHITE);
        card.setForeground(UiTheme.TEXT);
        card.setFont(UiTheme.FONT);
        card.setFocusPainted(false);
        card.setBorderPainted(false);
        card.setContentAreaFilled(true);
        card.setOpaque(true);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(16, 16, 16, 16)));
        card.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                target.doClick();
            }
        });
        return card;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup home_bnt_grp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JPanel panel_load;
    private java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}
