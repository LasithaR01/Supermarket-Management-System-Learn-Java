package cmart.ui.shell;

import cmart.config.SettingKeys;
import cmart.model.AppUser;
import cmart.model.Product;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.service.AuthService;
import cmart.service.ProductService;
import cmart.service.SettingService;
import cmart.ui.auth.LoginFrame;
import cmart.ui.customer.CustomerPanel;
import cmart.ui.dashboard.DashboardPanel;
import cmart.ui.employee.EmployeePanel;
import cmart.ui.invoice.InvoicePanel;
import cmart.ui.product.ProductPanel;
import cmart.ui.purchase.PurchasePanel;
import cmart.ui.refund.RefundPanel;
import cmart.ui.report.ReportsPanel;
import cmart.ui.sale.SalePanel;
import cmart.ui.admin.UsersPanel;
import cmart.ui.settings.SettingsPanel;
import cmart.ui.shift.ShiftPanel;
import cmart.ui.supplier.SupplierPanel;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Main shell. Design view and Run use the same NetBeans form layout.
 */
public class HomeFrame extends javax.swing.JFrame {

    PanelLoader jpload = new PanelLoader();
    private final SettingService settingService = new SettingService();
    private final AuthService authService = new AuthService();
    private final ProductService productService = new ProductService();
    private JButton btnSettings;
    private JButton btnUsers;
    private JButton btnLogout;
    private JButton btnRefund;
    private JButton btnShift;
    private JButton btnPurchase;
    private JButton btnDashboard;
    private JLabel userLabel;

    public HomeFrame() {
        if (!SessionContext.isSignedIn()) {
            throw new IllegalStateException("HomeFrame requires a signed-in session");
        }
        initComponents();
        styleShell();
        applyBranding();
        applyRoleAccess();
        setExtendedState(HomeFrame.MAXIMIZED_BOTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        home_bnt_grp = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        brandPanel = new javax.swing.JPanel();
        brandLabel = new javax.swing.JLabel();
        tagLabel = new javax.swing.JLabel();
        screenTitle = new javax.swing.JLabel();
        bodyPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        lblShop = new javax.swing.JLabel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        lblPeople = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        lblOther = new javax.swing.JLabel();
        jToggleButton7 = new javax.swing.JToggleButton();
        panel_load = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        lblHello = new javax.swing.JLabel();
        lblHint = new javax.swing.JLabel();
        cardSale = new javax.swing.JButton();
        cardInvoice = new javax.swing.JButton();
        cardProduct = new javax.swing.JButton();
        cardCustomer = new javax.swing.JButton();
        cardSupplier = new javax.swing.JButton();
        cardEmployee = new javax.swing.JButton();
        cardReports = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CMart — Supermarket");
        setMinimumSize(new java.awt.Dimension(1100, 700));

        headerPanel.setBackground(new java.awt.Color(20, 61, 44));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        headerPanel.setLayout(new java.awt.BorderLayout());

        brandPanel.setOpaque(false);
        brandPanel.setLayout(new javax.swing.BoxLayout(brandPanel, javax.swing.BoxLayout.PAGE_AXIS));

        brandLabel.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        brandLabel.setForeground(new java.awt.Color(255, 255, 255));
        brandLabel.setText("CMart");
        brandLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 22, 0, 8));
        brandPanel.add(brandLabel);

        tagLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tagLabel.setForeground(new java.awt.Color(183, 228, 199));
        tagLabel.setText("Supermarket desk");
        tagLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 22, 10, 8));
        brandPanel.add(tagLabel);

        headerPanel.add(brandPanel, java.awt.BorderLayout.LINE_START);

        screenTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        screenTitle.setForeground(new java.awt.Color(255, 255, 255));
        screenTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        screenTitle.setText("Home");
        screenTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 22));
        headerPanel.add(screenTitle, java.awt.BorderLayout.LINE_END);

        getContentPane().add(headerPanel, java.awt.BorderLayout.PAGE_START);

        bodyPanel.setBackground(new java.awt.Color(243, 247, 244));
        bodyPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(20, 61, 44));
        jPanel1.setPreferredSize(new java.awt.Dimension(230, 0));

        btnHome.setBackground(new java.awt.Color(216, 243, 220));
        btnHome.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHome.setForeground(new java.awt.Color(20, 61, 44));
        btnHome.setText("Home");
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHome.setOpaque(true);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        lblShop.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblShop.setForeground(new java.awt.Color(149, 213, 178));
        lblShop.setText("SHOP");

        jToggleButton5.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton5);
        jToggleButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton5.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/sales_menu.png"))); // NOI18N
        jToggleButton5.setText("Sales");
        jToggleButton5.setBorderPainted(false);
        jToggleButton5.setFocusPainted(false);
        jToggleButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton5.setOpaque(true);
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jToggleButton6.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton6);
        jToggleButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton6.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/invo.png"))); // NOI18N
        jToggleButton6.setText("Invoices");
        jToggleButton6.setBorderPainted(false);
        jToggleButton6.setFocusPainted(false);
        jToggleButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton6.setOpaque(true);
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        jToggleButton3.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton3);
        jToggleButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/product.png"))); // NOI18N
        jToggleButton3.setText("Products");
        jToggleButton3.setBorderPainted(false);
        jToggleButton3.setFocusPainted(false);
        jToggleButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton3.setOpaque(true);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        lblPeople.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblPeople.setForeground(new java.awt.Color(149, 213, 178));
        lblPeople.setText("PEOPLE");

        jToggleButton1.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton1);
        jToggleButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/customer.png"))); // NOI18N
        jToggleButton1.setText("Customers");
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton1.setOpaque(true);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/supplier.png"))); // NOI18N
        jToggleButton2.setText("Suppliers");
        jToggleButton2.setBorderPainted(false);
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton2.setOpaque(true);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton4.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton4);
        jToggleButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton4.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/emp.png"))); // NOI18N
        jToggleButton4.setText("Employees");
        jToggleButton4.setBorderPainted(false);
        jToggleButton4.setFocusPainted(false);
        jToggleButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton4.setOpaque(true);
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        lblOther.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblOther.setForeground(new java.awt.Color(149, 213, 178));
        lblOther.setText("OTHER");

        jToggleButton7.setBackground(new java.awt.Color(20, 61, 44));
        home_bnt_grp.add(jToggleButton7);
        jToggleButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton7.setForeground(new java.awt.Color(232, 245, 233));
        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cmart/resources/img/reports.png"))); // NOI18N
        jToggleButton7.setText("Reports");
        jToggleButton7.setBorderPainted(false);
        jToggleButton7.setFocusPainted(false);
        jToggleButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton7.setOpaque(true);
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
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblShop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPeople, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblOther, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(lblShop)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPeople)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblOther)
                .addGap(6, 6, 6)
                .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        bodyPanel.add(jPanel1, java.awt.BorderLayout.LINE_START);

        panel_load.setBackground(new java.awt.Color(243, 247, 244));
        panel_load.setLayout(new java.awt.BorderLayout());

        welcomePanel.setOpaque(false);

        lblHello.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblHello.setForeground(new java.awt.Color(31, 41, 51));
        lblHello.setText("What do you want to do?");

        lblHint.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblHint.setForeground(new java.awt.Color(92, 107, 115));
        lblHint.setText("Pick a task, or use the menu on the left.");

        cardSale.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardSale.setText("<html><b>New sale</b><br>Add items and take payment</html>");
        cardSale.setBorderPainted(false);
        cardSale.setFocusPainted(false);
        cardSale.setOpaque(true);
        cardSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardSaleActionPerformed(evt);
            }
        });

        cardInvoice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardInvoice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardInvoice.setText("<html><b>Invoices</b><br>Look up a past sale</html>");
        cardInvoice.setBorderPainted(false);
        cardInvoice.setFocusPainted(false);
        cardInvoice.setOpaque(true);
        cardInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardInvoiceActionPerformed(evt);
            }
        });

        cardProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardProduct.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardProduct.setText("<html><b>Products</b><br>Prices, barcodes, and stock</html>");
        cardProduct.setBorderPainted(false);
        cardProduct.setFocusPainted(false);
        cardProduct.setOpaque(true);
        cardProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardProductActionPerformed(evt);
            }
        });

        cardCustomer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardCustomer.setText("<html><b>Customers</b><br>Names and contact details</html>");
        cardCustomer.setBorderPainted(false);
        cardCustomer.setFocusPainted(false);
        cardCustomer.setOpaque(true);
        cardCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardCustomerActionPerformed(evt);
            }
        });

        cardSupplier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardSupplier.setText("<html><b>Suppliers</b><br>People you buy stock from</html>");
        cardSupplier.setBorderPainted(false);
        cardSupplier.setFocusPainted(false);
        cardSupplier.setOpaque(true);
        cardSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardSupplierActionPerformed(evt);
            }
        });

        cardEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardEmployee.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardEmployee.setText("<html><b>Employees</b><br>Staff records</html>");
        cardEmployee.setBorderPainted(false);
        cardEmployee.setFocusPainted(false);
        cardEmployee.setOpaque(true);
        cardEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardEmployeeActionPerformed(evt);
            }
        });

        cardReports.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cardReports.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardReports.setText("<html><b>Reports</b><br>Print sales and lists</html>");
        cardReports.setBorderPainted(false);
        cardReports.setFocusPainted(false);
        cardReports.setOpaque(true);
        cardReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardReportsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHello)
                    .addComponent(lblHint)
                    .addGroup(welcomePanelLayout.createSequentialGroup()
                        .addComponent(cardSale, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(welcomePanelLayout.createSequentialGroup()
                        .addComponent(cardCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cardReports, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblHello)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHint)
                .addGap(18, 18, 18)
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSale, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(cardInvoice)
                    .addComponent(cardProduct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(cardSupplier)
                    .addComponent(cardEmployee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cardReports, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(194, Short.MAX_VALUE))
        );

        panel_load.add(welcomePanel, java.awt.BorderLayout.CENTER);

        bodyPanel.add(panel_load, java.awt.BorderLayout.CENTER);

        getContentPane().add(bodyPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        showWelcome();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        setScreenTitle("Customers");
        jpload.jPanelLoader(panel_load, new CustomerPanel());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        setScreenTitle("Suppliers");
        jpload.jPanelLoader(panel_load, new SupplierPanel());
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        setScreenTitle("Products");
        jpload.jPanelLoader(panel_load, new ProductPanel());
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        setScreenTitle("Employees");
        jpload.jPanelLoader(panel_load, new EmployeePanel());
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        setScreenTitle("Sales");
        jpload.jPanelLoader(panel_load, new SalePanel());
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        setScreenTitle("Invoices");
        jpload.jPanelLoader(panel_load, new InvoicePanel());
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        setScreenTitle("Reports");
        jpload.jPanelLoader(panel_load, new ReportsPanel());
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void cardSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardSaleActionPerformed
        jToggleButton5.doClick();
    }//GEN-LAST:event_cardSaleActionPerformed

    private void cardInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardInvoiceActionPerformed
        jToggleButton6.doClick();
    }//GEN-LAST:event_cardInvoiceActionPerformed

    private void cardProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardProductActionPerformed
        jToggleButton3.doClick();
    }//GEN-LAST:event_cardProductActionPerformed

    private void cardCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardCustomerActionPerformed
        jToggleButton1.doClick();
    }//GEN-LAST:event_cardCustomerActionPerformed

    private void cardSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardSupplierActionPerformed
        jToggleButton2.doClick();
    }//GEN-LAST:event_cardSupplierActionPerformed

    private void cardEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardEmployeeActionPerformed
        jToggleButton4.doClick();
    }//GEN-LAST:event_cardEmployeeActionPerformed

    private void cardReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardReportsActionPerformed
        jToggleButton7.doClick();
    }//GEN-LAST:event_cardReportsActionPerformed

    public static void main(String args[]) {
        // Prefer cmart.App — keeps migrate + login path consistent.
        cmart.App.main(args);
    }

    /**
     * Light polish only — does NOT rebuild the form, so Design and Run stay the same.
     */
    private void styleShell() {
        getContentPane().setBackground(UiTheme.PAGE);
        polishButton(btnHome, true);
        polishNav(jToggleButton5);
        polishNav(jToggleButton6);
        polishNav(jToggleButton3);
        polishNav(jToggleButton1);
        polishNav(jToggleButton2);
        polishNav(jToggleButton4);
        polishNav(jToggleButton7);
        polishCard(cardSale);
        polishCard(cardInvoice);
        polishCard(cardProduct);
        polishCard(cardCustomer);
        polishCard(cardSupplier);
        polishCard(cardEmployee);
        polishCard(cardReports);

        installSessionChrome();

        brandPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        brandPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showWelcome();
            }
        });

        ItemListener navListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                refreshNavColors();
            }
        };
        JToggleButton[] buttons = {
            jToggleButton1, jToggleButton2, jToggleButton3, jToggleButton4,
            jToggleButton5, jToggleButton6, jToggleButton7
        };
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addItemListener(navListener);
        }
        refreshNavColors();
    }

    private void installSessionChrome() {
        AppUser user = SessionContext.currentUser();
        userLabel = new JLabel();
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(0xB7E4C7));
        if (user != null) {
            userLabel.setText(user.getDisplayName() + " · " + user.getRole().name());
        }

        btnDashboard = headerOpButton("Dashboard", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                home_bnt_grp.clearSelection();
                openDashboard();
            }
        });
        btnRefund = headerOpButton("Returns", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                home_bnt_grp.clearSelection();
                openRefund();
            }
        });
        btnShift = headerOpButton("Shift", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                home_bnt_grp.clearSelection();
                openShift();
            }
        });
        btnPurchase = headerOpButton("Purchase", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                home_bnt_grp.clearSelection();
                openPurchase();
            }
        });

        btnSettings = headerOpButton("Settings", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                home_bnt_grp.clearSelection();
                openSettings();
            }
        });

        btnUsers = headerOpButton("Users", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                home_bnt_grp.clearSelection();
                openUsers();
            }
        });

        btnLogout = headerOpButton("Sign out", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signOut();
            }
        });

        JPanel sessionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 18));
        sessionBar.setOpaque(false);
        sessionBar.add(userLabel);
        sessionBar.add(btnDashboard);
        sessionBar.add(btnRefund);
        sessionBar.add(btnShift);
        sessionBar.add(btnPurchase);
        sessionBar.add(btnUsers);
        sessionBar.add(btnSettings);
        sessionBar.add(btnLogout);

        // Move title + session into LINE_END without fighting the form layout
        headerPanel.remove(screenTitle);
        JPanel east = new JPanel(new BorderLayout());
        east.setOpaque(false);
        screenTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        east.add(screenTitle, BorderLayout.CENTER);
        east.add(sessionBar, BorderLayout.EAST);
        headerPanel.add(east, BorderLayout.LINE_END);
    }

    private void applyBranding() {
        try {
            brandLabel.setText(settingService.get(SettingKeys.SHOP_NAME));
            tagLabel.setText(settingService.get(SettingKeys.SHOP_TAGLINE));
            setTitle(settingService.get(SettingKeys.SHOP_NAME) + " — Supermarket");
            lblHello.setText("Hello — " + SessionContext.currentUser().getDisplayName());
            refreshLowStockHint();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void refreshLowStockHint() {
        try {
            double threshold = settingService.getDouble(SettingKeys.STOCK_LOW_THRESHOLD, 5);
            java.util.List<Product> low = productService.listLowStock(threshold);
            if (low.isEmpty()) {
                lblHint.setText("Pick a card below, or use the menu on the left.");
            } else {
                StringBuilder names = new StringBuilder();
                int max = Math.min(3, low.size());
                for (int i = 0; i < max; i++) {
                    if (i > 0) {
                        names.append(", ");
                    }
                    names.append(low.get(i).getName()).append(" (").append(low.get(i).getQty()).append(")");
                }
                if (low.size() > max) {
                    names.append(" +").append(low.size() - max).append(" more");
                }
                lblHint.setText("Low stock (≤ " + (long) threshold + "): " + names);
            }
        } catch (SQLException e) {
            lblHint.setText("Pick a card below, or use the menu on the left.");
        }
    }

    private void applyRoleAccess() {
        setModuleVisible(Permission.SALE, jToggleButton5, cardSale);
        setModuleVisible(Permission.INVOICE, jToggleButton6, cardInvoice);
        setModuleVisible(Permission.PRODUCT, jToggleButton3, cardProduct);
        setModuleVisible(Permission.CUSTOMER, jToggleButton1, cardCustomer);
        setModuleVisible(Permission.SUPPLIER, jToggleButton2, cardSupplier);
        setModuleVisible(Permission.EMPLOYEE, jToggleButton4, cardEmployee);
        setModuleVisible(Permission.REPORT, jToggleButton7, cardReports);
        if (btnDashboard != null) {
            btnDashboard.setVisible(SessionContext.can(Permission.DASHBOARD));
        }
        if (btnRefund != null) {
            btnRefund.setVisible(SessionContext.can(Permission.REFUND));
        }
        if (btnShift != null) {
            btnShift.setVisible(SessionContext.can(Permission.SHIFT));
        }
        if (btnPurchase != null) {
            btnPurchase.setVisible(SessionContext.can(Permission.PURCHASE));
        }
        if (btnSettings != null) {
            btnSettings.setVisible(SessionContext.can(Permission.SETTINGS));
        }
        if (btnUsers != null) {
            btnUsers.setVisible(SessionContext.can(Permission.USER_ADMIN));
        }
    }

    private JButton headerOpButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        polishButton(button, false);
        button.setBackground(new Color(0x2D6A4F));
        button.addActionListener(listener);
        return button;
    }

    private void setModuleVisible(Permission permission, JToggleButton nav, JButton card) {
        boolean allowed = SessionContext.can(permission);
        nav.setVisible(allowed);
        if (card != null) {
            card.setVisible(allowed);
        }
    }

    private void openSettings() {
        if (!SessionContext.can(Permission.SETTINGS)) {
            JOptionPane.showMessageDialog(this, "You do not have permission to open Settings.");
            return;
        }
        setScreenTitle("Settings");
        jpload.jPanelLoader(panel_load, new SettingsPanel());
    }

    private void openUsers() {
        if (!SessionContext.can(Permission.USER_ADMIN)) {
            JOptionPane.showMessageDialog(this, "You do not have permission to manage users.");
            return;
        }
        setScreenTitle("Users");
        jpload.jPanelLoader(panel_load, new UsersPanel());
    }

    private void openDashboard() {
        if (!SessionContext.can(Permission.DASHBOARD)) {
            JOptionPane.showMessageDialog(this, "You do not have permission to open Dashboard.");
            return;
        }
        setScreenTitle("Dashboard");
        jpload.jPanelLoader(panel_load, new DashboardPanel());
    }

    private void openRefund() {
        if (!SessionContext.can(Permission.REFUND)) {
            JOptionPane.showMessageDialog(this, "You do not have permission for returns.");
            return;
        }
        setScreenTitle("Returns");
        jpload.jPanelLoader(panel_load, new RefundPanel());
    }

    private void openShift() {
        if (!SessionContext.can(Permission.SHIFT)) {
            JOptionPane.showMessageDialog(this, "You do not have permission for shifts.");
            return;
        }
        setScreenTitle("Shift");
        jpload.jPanelLoader(panel_load, new ShiftPanel());
    }

    private void openPurchase() {
        if (!SessionContext.can(Permission.PURCHASE)) {
            JOptionPane.showMessageDialog(this, "You do not have permission for purchase orders.");
            return;
        }
        setScreenTitle("Purchase");
        jpload.jPanelLoader(panel_load, new PurchasePanel());
    }

    private void signOut() {
        authService.logout();
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void polishButton(JButton button, boolean home) {
        button.setUI(new BasicButtonUI());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 12, 10, 12));
        if (home) {
            button.setBackground(new Color(0xD8F3DC));
            button.setForeground(new Color(0x143D2C));
        }
    }

    private void polishNav(JToggleButton button) {
        button.setUI(new BasicButtonUI());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 12, 10, 12));
        button.setIconTextGap(12);
    }

    private void polishCard(JButton card) {
        card.setUI(new BasicButtonUI());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setBackground(Color.WHITE);
        card.setForeground(UiTheme.TEXT);
    }

    private void refreshNavColors() {
        JToggleButton[] buttons = {
            jToggleButton1, jToggleButton2, jToggleButton3, jToggleButton4,
            jToggleButton5, jToggleButton6, jToggleButton7
        };
        for (int i = 0; i < buttons.length; i++) {
            if (!buttons[i].isVisible()) {
                continue;
            }
            if (buttons[i].isSelected()) {
                buttons[i].setBackground(UiTheme.NAV_ACTIVE);
                buttons[i].setForeground(Color.WHITE);
            } else {
                buttons[i].setBackground(UiTheme.SIDEBAR);
                buttons[i].setForeground(new Color(0xE8F5E9));
            }
        }
    }

    private void setScreenTitle(String title) {
        screenTitle.setText(title);
        refreshNavColors();
    }

    private void showWelcome() {
        home_bnt_grp.clearSelection();
        screenTitle.setText("Home");
        refreshNavColors();
        refreshLowStockHint();
        panel_load.removeAll();
        panel_load.setLayout(new java.awt.BorderLayout());
        panel_load.add(welcomePanel, java.awt.BorderLayout.CENTER);
        panel_load.revalidate();
        panel_load.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JLabel brandLabel;
    private javax.swing.JPanel brandPanel;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton cardCustomer;
    private javax.swing.JButton cardEmployee;
    private javax.swing.JButton cardInvoice;
    private javax.swing.JButton cardProduct;
    private javax.swing.JButton cardReports;
    private javax.swing.JButton cardSale;
    private javax.swing.JButton cardSupplier;
    private javax.swing.JPanel headerPanel;
    private javax.swing.ButtonGroup home_bnt_grp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JLabel lblHello;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblOther;
    private javax.swing.JLabel lblPeople;
    private javax.swing.JLabel lblShop;
    private javax.swing.JPanel panel_load;
    private javax.swing.JLabel screenTitle;
    private javax.swing.JLabel tagLabel;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables
}
