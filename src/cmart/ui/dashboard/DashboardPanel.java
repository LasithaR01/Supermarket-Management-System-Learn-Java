package cmart.ui.dashboard;

import cmart.model.AppUser;
import cmart.model.Product;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.service.BackupService;
import cmart.service.DashboardService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Role-aware dashboard: Super Admin (system), Store Owner (business),
 * Cashier (till snapshot), Store Keeper (stock).
 */
public class DashboardPanel extends JPanel {

    private final DashboardService dashboardService = new DashboardService();
    private final BackupService backupService = new BackupService();
    private final JLabel card1 = new JLabel("-");
    private final JLabel card2 = new JLabel("-");
    private final JLabel card3 = new JLabel("-");
    private final JTextArea details = new JTextArea(12, 40);
    private final JLabel titleLabel = new JLabel("Dashboard");
    private JButton backupButton;

    public DashboardPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(UiTheme.SIDEBAR);
        add(titleLabel, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 12));
        cards.setOpaque(false);
        cards.add(card(card1));
        cards.add(card(card2));
        cards.add(card(card3));
        add(cards, BorderLayout.PAGE_START);

        details.setEditable(false);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        backupButton = new JButton("Backup database…");
        backupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backup();
            }
        });
        actions.add(refresh);
        actions.add(backupButton);
        add(actions, BorderLayout.SOUTH);
        applyRoleChrome();
        refresh();
    }

    private void applyRoleChrome() {
        AppUser user = SessionContext.currentUser();
        Role role = user == null ? null : user.getRole();
        boolean canBackup = SessionContext.can(Permission.BACKUP);
        backupButton.setVisible(canBackup);
        if (role == Role.SUPER_ADMIN) {
            titleLabel.setText("System dashboard (Super Admin)");
        } else if (role == Role.STORE_OWNER) {
            titleLabel.setText("Owner dashboard");
        } else if (role == Role.CASHIER) {
            titleLabel.setText("Cashier snapshot");
        } else if (role == Role.STORE_KEEPER) {
            titleLabel.setText("Stock dashboard");
        } else {
            titleLabel.setText("Dashboard");
        }
    }

    private JPanel card(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(java.awt.Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(label, BorderLayout.CENTER);
        return p;
    }

    private void refresh() {
        AppUser user = SessionContext.currentUser();
        Role role = user == null ? null : user.getRole();
        try {
            if (role == Role.SUPER_ADMIN) {
                refreshSuperAdmin();
            } else if (role == Role.STORE_KEEPER) {
                refreshStoreKeeper();
            } else if (role == Role.CASHIER) {
                refreshCashier();
            } else {
                refreshOwner();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void refreshOwner() throws Exception {
        card1.setText("Recent paid sales: " + dashboardService.recentSalesTotal());
        card2.setText("Unpaid / partial bills: " + dashboardService.duesCount());
        List<Product> low = dashboardService.lowStock();
        card3.setText("Low stock items: " + low.size());
        StringBuilder sb = new StringBuilder();
        sb.append("Top products by qty sold\n");
        for (String[] row : dashboardService.topProducts(8)) {
            sb.append(" • ").append(row[0]).append(" — ").append(row[1]).append('\n');
        }
        sb.append("\nLow stock\n");
        for (Product p : low) {
            sb.append(" • ").append(p.getName()).append(" (").append(p.getQty()).append(")\n");
        }
        appendSecurityAudit(sb);
        details.setText(sb.toString());
    }

    private void refreshSuperAdmin() throws Exception {
        int users = dashboardService.userCount();
        int owners = dashboardService.activeUsersByRole(Role.STORE_OWNER);
        int cashiers = dashboardService.activeUsersByRole(Role.CASHIER);
        card1.setText("Users total: " + users);
        card2.setText("Store owners: " + owners);
        card3.setText("Cashiers: " + cashiers);
        StringBuilder sb = new StringBuilder();
        sb.append("Support checklist\n");
        sb.append(" • Confirm Store Owner can open Users & create staff\n");
        sb.append(" • Confirm recovery PIN is set in Settings\n");
        sb.append(" • Prefer Owner login for daily shop work — not Super Admin\n");
        sb.append("\nBusiness snapshot\n");
        sb.append("Recent paid sales: ").append(dashboardService.recentSalesTotal()).append('\n');
        sb.append("Unpaid bills: ").append(dashboardService.duesCount()).append('\n');
        List<Product> low = dashboardService.lowStock();
        sb.append("Low stock items: ").append(low.size()).append('\n');
        appendSecurityAudit(sb);
        details.setText(sb.toString());
    }

    private void appendSecurityAudit(StringBuilder sb) {
        try {
            java.util.List<String[]> events = dashboardService.recentSecurityEvents(8);
            if (events.isEmpty()) {
                return;
            }
            sb.append("\nSecurity audit (recent)\n");
            for (String[] row : events) {
                sb.append(" • ").append(row[0]).append(" · ").append(row[1])
                        .append(" · ").append(row[2]);
                if (row[4] != null && row[4].length() > 0) {
                    sb.append(" — ").append(row[4]);
                }
                sb.append('\n');
            }
        } catch (Exception ignored) {
            // Optional section
        }
    }

    private void refreshCashier() throws Exception {
        AppUser user = SessionContext.currentUser();
        card1.setText("Signed in: " + (user == null ? "-" : user.getDisplayName()));
        card2.setText("Recent paid sales: " + dashboardService.recentSalesTotal());
        card3.setText("Unpaid / partial: " + dashboardService.duesCount());
        StringBuilder sb = new StringBuilder();
        sb.append("Till tips\n");
        sb.append(" • Open Shift before taking cash\n");
        sb.append(" • Use Sale for checkout · Returns for refunds\n");
        sb.append(" • Forgot password? Ask Store Owner (Users → Reset password)\n");
        details.setText(sb.toString());
    }

    private void refreshStoreKeeper() throws Exception {
        List<Product> low = dashboardService.lowStock();
        card1.setText("Low stock items: " + low.size());
        card2.setText("Role: Store Keeper");
        card3.setText("Focus: stock & purchase");
        StringBuilder sb = new StringBuilder();
        sb.append("Low stock\n");
        if (low.isEmpty()) {
            sb.append(" • None below threshold\n");
        } else {
            for (Product p : low) {
                sb.append(" • ").append(p.getName()).append(" (").append(p.getQty()).append(")\n");
            }
        }
        sb.append("\nNext actions\n");
        sb.append(" • Products — update qty / barcodes\n");
        sb.append(" • Purchase — create PO / receive goods\n");
        details.setText(sb.toString());
    }

    private void backup() {
        if (!SessionContext.can(Permission.BACKUP)) {
            JOptionPane.showMessageDialog(this, "You do not have permission to backup.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            File out = backupService.backupToFolder(chooser.getSelectedFile());
            JOptionPane.showMessageDialog(this, "Backup saved:\n" + out.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
