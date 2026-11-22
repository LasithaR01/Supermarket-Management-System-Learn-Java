package cmart.ui.dashboard;

import cmart.model.Product;
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

public class DashboardPanel extends JPanel {

    private final DashboardService dashboardService = new DashboardService();
    private final BackupService backupService = new BackupService();
    private final JLabel salesLabel = new JLabel("Sales: -");
    private final JLabel duesLabel = new JLabel("Dues: -");
    private final JLabel lowStockLabel = new JLabel("Low stock: -");
    private final JTextArea details = new JTextArea(12, 40);

    public DashboardPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel title = new JLabel("Owner dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 12));
        cards.setOpaque(false);
        cards.add(card(salesLabel));
        cards.add(card(duesLabel));
        cards.add(card(lowStockLabel));
        add(cards, BorderLayout.PAGE_START);

        details.setEditable(false);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refresh(); }
        });
        JButton backup = new JButton("Backup database…");
        backup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { backup(); }
        });
        actions.add(refresh);
        actions.add(backup);
        add(actions, BorderLayout.SOUTH);
        refresh();
    }

    private JPanel card(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(java.awt.Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(label, BorderLayout.CENTER);
        return p;
    }

    private void refresh() {
        try {
            salesLabel.setText("Recent paid sales: " + dashboardService.recentSalesTotal());
            duesLabel.setText("Unpaid / partial bills: " + dashboardService.duesCount());
            List<Product> low = dashboardService.lowStock();
            lowStockLabel.setText("Low stock items: " + low.size());
            StringBuilder sb = new StringBuilder();
            sb.append("Top products by qty sold\n");
            for (String[] row : dashboardService.topProducts(8)) {
                sb.append(" • ").append(row[0]).append(" — ").append(row[1]).append('\n');
            }
            sb.append("\nLow stock\n");
            for (Product p : low) {
                sb.append(" • ").append(p.getName()).append(" (").append(p.getQty()).append(")\n");
            }
            details.setText(sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void backup() {
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
