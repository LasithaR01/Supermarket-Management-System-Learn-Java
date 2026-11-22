package cmart.ui.admin;

import cmart.config.SettingKeys;
import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.service.AuditService;
import cmart.service.BackupService;
import cmart.service.ErrorLogService;
import cmart.service.SystemMonitorService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Super Admin control room: health, audit, errors, users, ACL, backup/restore, schema.
 */
public class SuperAdminMonitorPanel extends JPanel {

    private final SystemMonitorService monitorService = new SystemMonitorService();
    private final AuditService auditService = new AuditService();
    private final ErrorLogService errorLogService = new ErrorLogService();
    private final BackupService backupService = new BackupService();

    private final JLabel cardDb = new JLabel("-");
    private final JLabel cardErrors = new JLabel("-");
    private final JLabel cardLocked = new JLabel("-");
    private final JLabel cardBackup = new JLabel("-");
    private final JLabel cardPin = new JLabel("-");
    private final JLabel cardVersion = new JLabel("-");
    private final JLabel warningsLabel = new JLabel(" ");
    private final JLabel maintenanceLabel = new JLabel(" ");

    private final DefaultTableModel auditModel = new DefaultTableModel(
            new Object[] {"When", "Event", "User", "Actor", "Detail"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable auditTable = new JTable(auditModel);
    private final JComboBox eventFilter = new JComboBox(new Object[] {
        "ALL",
        AuditService.LOGIN_OK,
        AuditService.LOGIN_FAIL,
        AuditService.LOGIN_LOCKED,
        AuditService.LOGOUT,
        AuditService.PASSWORD_CHANGE,
        AuditService.PASSWORD_RESET,
        AuditService.USER_CREATE,
        AuditService.USER_UPDATE,
        AuditService.USER_UNLOCK,
        AuditService.USER_FORCE_PASSWORD,
        AuditService.PRODUCT_DELETE,
        AuditService.SALE_DISCOUNT,
        AuditService.REFUND,
        AuditService.BACKUP,
        AuditService.RESTORE,
        AuditService.EXPORT,
        AuditService.ERROR_LOG_CLEAR,
        AuditService.MAINTENANCE,
        AuditService.ACL_CHANGE,
        AuditService.RECOVERY_PIN_SET
    });
    private final JTextField userFilter = new JTextField(12);

    private final DefaultTableModel errorModel = new DefaultTableModel(
            new Object[] {"ID", "When", "Level", "Source", "Message", "User"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable errorTable = new JTable(errorModel);

    private final DefaultTableModel lockedModel = new DefaultTableModel(
            new Object[] {"ID", "Username", "Role", "Locked until"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable lockedTable = new JTable(lockedModel);

    private final DefaultTableModel emergencyModel = new DefaultTableModel(
            new Object[] {"ID", "Username", "Role", "Active", "Must change pwd"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable emergencyTable = new JTable(emergencyModel);

    private final DefaultTableModel backupModel = new DefaultTableModel(
            new Object[] {"When", "Status", "Path", "Message", "By"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable backupTable = new JTable(backupModel);

    private final JLabel configLabel = new JLabel(" ");
    private final JTextArea schemaArea = new JTextArea(12, 40);
    private final JCheckBox aclDiscount = new JCheckBox("Staff may apply discounts");
    private final JCheckBox aclProductDelete = new JCheckBox("Staff may delete products");
    private final JCheckBox aclRefund = new JCheckBox("Staff may process refunds");
    private final JCheckBox maintenanceBox = new JCheckBox("Maintenance mode (block non–Super Admin login)");

    public SuperAdminMonitorPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        if (!SessionContext.can(Permission.SYSTEM)
                || SessionContext.currentUser() == null
                || SessionContext.currentUser().getRole() != Role.SUPER_ADMIN) {
            add(new JLabel("Super Admin only."), BorderLayout.CENTER);
            return;
        }

        JLabel title = new JLabel("Super Admin — System Monitor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        add(title, BorderLayout.NORTH);

        JPanel north = new JPanel(new BorderLayout(0, 8));
        north.setOpaque(false);
        north.add(buildCards(), BorderLayout.CENTER);
        warningsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        warningsLabel.setForeground(UiTheme.DANGER);
        maintenanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JPanel warnStack = new JPanel(new GridLayout(2, 1));
        warnStack.setOpaque(false);
        warnStack.add(maintenanceLabel);
        warnStack.add(warningsLabel);
        north.add(warnStack, BorderLayout.SOUTH);
        add(north, BorderLayout.PAGE_START);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Health", buildHealthTab());
        tabs.addTab("Security audit", buildAuditTab());
        tabs.addTab("Errors / faults", buildErrorsTab());
        tabs.addTab("Locked users", buildLockedTab());
        tabs.addTab("Emergency users", buildEmergencyTab());
        tabs.addTab("ACL / maintenance", buildAclTab());
        tabs.addTab("Schema", buildSchemaTab());
        tabs.addTab("Backup / ops", buildBackupTab());
        add(tabs, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setOpaque(false);
        JButton refresh = new JButton("Refresh all");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshAll();
            }
        });
        south.add(refresh);
        add(south, BorderLayout.SOUTH);

        UiTheme.apply(this);
        refreshAll();
    }

    private JPanel buildCards() {
        JPanel cards = new JPanel(new GridLayout(2, 3, 10, 10));
        cards.setOpaque(false);
        cards.add(card(cardDb));
        cards.add(card(cardErrors));
        cards.add(card(cardLocked));
        cards.add(card(cardBackup));
        cards.add(card(cardPin));
        cards.add(card(cardVersion));
        return cards;
    }

    private JPanel card(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UiTheme.TEXT);
        p.add(label, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildHealthTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        configLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        configLabel.setForeground(UiTheme.MUTED);
        configLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        panel.add(configLabel, BorderLayout.NORTH);
        JLabel help = new JLabel(
                "<html>Use this console for system health, security, faults, ACL, restore, and schema — "
                        + "not daily shop sales (that is Store Owner).<br>"
                        + "Users panel can create another Super Admin when you need a second support login.</html>");
        help.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        help.setForeground(UiTheme.MUTED);
        panel.add(help, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAuditTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        JPanel filters = new JPanel();
        filters.setOpaque(false);
        filters.add(new JLabel("Event"));
        filters.add(eventFilter);
        filters.add(new JLabel("User"));
        filters.add(userFilter);
        JButton search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadAudit();
            }
        });
        filters.add(search);
        JButton export = new JButton("Export CSV…");
        export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportAudit();
            }
        });
        filters.add(export);
        panel.add(filters, BorderLayout.NORTH);
        auditTable.setRowHeight(28);
        panel.add(new JScrollPane(auditTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildErrorsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        errorTable.setRowHeight(28);
        panel.add(new JScrollPane(errorTable), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.setOpaque(false);
        JButton detail = new JButton("View stack / detail");
        detail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showErrorDetail();
            }
        });
        JButton export = new JButton("Export CSV…");
        export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportErrors();
            }
        });
        JButton clearOld = new JButton("Clear older than 30 days");
        clearOld.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearErrors(false);
            }
        });
        JButton clearAll = new JButton("Clear all");
        clearAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearErrors(true);
            }
        });
        south.add(detail);
        south.add(export);
        south.add(clearOld);
        south.add(clearAll);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildLockedTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        lockedTable.setRowHeight(28);
        panel.add(new JScrollPane(lockedTable), BorderLayout.CENTER);
        JButton unlock = new JButton("Unlock selected");
        unlock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unlockSelected();
            }
        });
        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(unlock);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildEmergencyTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        emergencyTable.setRowHeight(28);
        panel.add(new JScrollPane(emergencyTable), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.setOpaque(false);
        JButton deactivate = new JButton("Deactivate");
        deactivate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emergencyDeactivate();
            }
        });
        JButton activate = new JButton("Activate");
        activate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emergencyActivate();
            }
        });
        JButton forcePwd = new JButton("Force password change");
        forcePwd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emergencyForcePassword();
            }
        });
        south.add(deactivate);
        south.add(activate);
        south.add(forcePwd);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAclTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JLabel hint = new JLabel(
                "<html>These flags restrict <b>Cashier / Store Keeper</b> only. "
                        + "Store Owner and Super Admin always keep full access.</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(hint);
        form.add(aclDiscount);
        form.add(aclProductDelete);
        form.add(aclRefund);
        form.add(maintenanceBox);
        JButton save = new JButton("Save ACL & maintenance");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAcl();
            }
        });
        form.add(save);
        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildSchemaTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        schemaArea.setEditable(false);
        schemaArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        schemaArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JScrollPane(schemaArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBackupTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        backupTable.setRowHeight(28);
        panel.add(new JScrollPane(backupTable), BorderLayout.CENTER);
        JButton run = new JButton("Run backup…");
        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runBackup();
            }
        });
        JButton restore = new JButton("Restore from .sql…");
        restore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runRestore();
            }
        });
        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(run);
        south.add(restore);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAll() {
        try {
            cardDb.setText("<html><b>DB</b><br>" + monitorService.dbHealth() + "</html>");
            cardErrors.setText("<html><b>Errors (24h)</b><br>" + monitorService.errorsLast24h() + "</html>");
            cardLocked.setText("<html><b>Locked users</b><br>" + monitorService.lockedUserCount() + "</html>");
            String[] latest = monitorService.latestBackup();
            if (latest == null) {
                cardBackup.setText("<html><b>Last backup</b><br>None yet</html>");
            } else {
                cardBackup.setText("<html><b>Last backup</b><br>" + latest[1] + " · " + latest[0] + "</html>");
            }
            cardPin.setText("<html><b>Recovery PIN</b><br>"
                    + (monitorService.recoveryPinConfigured() ? "Configured" : "NOT SET") + "</html>");
            cardVersion.setText("<html><b>App</b><br>" + monitorService.appVersion()
                    + "<br>" + monitorService.schemaLabel() + "</html>");
            configLabel.setText(monitorService.configHealth());

            boolean maint = monitorService.isMaintenanceMode();
            if (maint) {
                maintenanceLabel.setText("MAINTENANCE MODE ON — only Super Admin can sign in");
                maintenanceLabel.setForeground(UiTheme.DANGER);
            } else {
                maintenanceLabel.setText("Maintenance mode: off");
                maintenanceLabel.setForeground(UiTheme.PRIMARY);
            }
            maintenanceBox.setSelected(maint);

            Map<String, Boolean> acl = monitorService.aclFlags();
            aclDiscount.setSelected(Boolean.TRUE.equals(acl.get(SettingKeys.ACL_ALLOW_DISCOUNT)));
            aclProductDelete.setSelected(Boolean.TRUE.equals(acl.get(SettingKeys.ACL_ALLOW_PRODUCT_DELETE)));
            aclRefund.setSelected(Boolean.TRUE.equals(acl.get(SettingKeys.ACL_ALLOW_REFUND)));

            schemaArea.setText(monitorService.schemaStatusText());

            List<String> warnings = monitorService.demoPasswordWarnings();
            if (warnings.isEmpty()) {
                warningsLabel.setText("Demo passwords: OK (defaults changed).");
                warningsLabel.setForeground(UiTheme.PRIMARY);
            } else {
                StringBuilder sb = new StringBuilder("Security warning: ");
                for (int i = 0; i < warnings.size(); i++) {
                    if (i > 0) {
                        sb.append(" · ");
                    }
                    sb.append(warnings.get(i));
                }
                warningsLabel.setText(sb.toString());
                warningsLabel.setForeground(UiTheme.DANGER);
            }

            reloadAudit();
            reloadErrors();
            reloadLocked();
            reloadEmergency();
            reloadBackups();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void reloadAudit() {
        try {
            auditModel.setRowCount(0);
            String event = String.valueOf(eventFilter.getSelectedItem());
            List<String[]> rows = auditService.search(event, userFilter.getText(), 200);
            for (String[] row : rows) {
                auditModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void reloadErrors() {
        try {
            errorModel.setRowCount(0);
            List<String[]> rows = errorLogService.recent(100);
            for (String[] row : rows) {
                errorModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void reloadLocked() {
        try {
            lockedModel.setRowCount(0);
            List<AppUser> users = monitorService.lockedUsers();
            for (AppUser user : users) {
                lockedModel.addRow(new Object[] {
                    Integer.valueOf(user.getId()),
                    user.getUsername(),
                    user.getRole().displayLabel(),
                    user.getLockedUntil() == null ? "" : String.valueOf(user.getLockedUntil())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void reloadEmergency() {
        try {
            emergencyModel.setRowCount(0);
            List<AppUser> users = monitorService.allUsersPublic();
            for (AppUser user : users) {
                emergencyModel.addRow(new Object[] {
                    Integer.valueOf(user.getId()),
                    user.getUsername(),
                    user.getRole().displayLabel(),
                    user.isActive() ? "yes" : "no",
                    user.isMustChangePassword() ? "yes" : "no"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void reloadBackups() {
        try {
            backupModel.setRowCount(0);
            List<String[]> rows = monitorService.recentBackups(50);
            for (String[] row : rows) {
                backupModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void unlockSelected() {
        int row = lockedTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a locked user.");
            return;
        }
        int userId = Integer.parseInt(String.valueOf(lockedModel.getValueAt(row, 0)));
        String username = String.valueOf(lockedModel.getValueAt(row, 1));
        int ok = JOptionPane.showConfirmDialog(this, "Unlock " + username + "?", "Unlock",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            monitorService.unlockUser(userId);
            JOptionPane.showMessageDialog(this, "User unlocked.");
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private int selectedEmergencyUserId() {
        int row = emergencyTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a user.");
            return -1;
        }
        return Integer.parseInt(String.valueOf(emergencyModel.getValueAt(row, 0)));
    }

    private void emergencyDeactivate() {
        int userId = selectedEmergencyUserId();
        if (userId < 0) {
            return;
        }
        String username = String.valueOf(emergencyModel.getValueAt(emergencyTable.getSelectedRow(), 1));
        int ok = JOptionPane.showConfirmDialog(this, "Deactivate " + username + "?", "Deactivate",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            monitorService.deactivateUser(userId);
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void emergencyActivate() {
        int userId = selectedEmergencyUserId();
        if (userId < 0) {
            return;
        }
        try {
            monitorService.activateUser(userId);
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void emergencyForcePassword() {
        int userId = selectedEmergencyUserId();
        if (userId < 0) {
            return;
        }
        String username = String.valueOf(emergencyModel.getValueAt(emergencyTable.getSelectedRow(), 1));
        int ok = JOptionPane.showConfirmDialog(this,
                "Force " + username + " to change password at next login?",
                "Force password change", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            monitorService.forcePasswordChange(userId);
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void saveAcl() {
        try {
            monitorService.setAclFlag(SettingKeys.ACL_ALLOW_DISCOUNT, aclDiscount.isSelected());
            monitorService.setAclFlag(SettingKeys.ACL_ALLOW_PRODUCT_DELETE, aclProductDelete.isSelected());
            monitorService.setAclFlag(SettingKeys.ACL_ALLOW_REFUND, aclRefund.isSelected());
            monitorService.setMaintenanceMode(maintenanceBox.isSelected());
            JOptionPane.showMessageDialog(this, "ACL and maintenance saved.");
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void showErrorDetail() {
        int row = errorTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an error row.");
            return;
        }
        try {
            int id = Integer.parseInt(String.valueOf(errorModel.getValueAt(row, 0)));
            String[] full = errorLogService.detail(id);
            if (full == null) {
                JOptionPane.showMessageDialog(this, "Error not found.");
                return;
            }
            JTextArea area = new JTextArea(18, 60);
            area.setText(
                    "When: " + full[1] + "\nLevel: " + full[2] + "\nSource: " + full[3]
                            + "\nUser: " + full[6] + "\nMessage: " + full[4]
                            + "\n\nDetail / stack:\n" + (full[5] == null ? "" : full[5]));
            area.setCaretPosition(0);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Error #" + id,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void clearErrors(boolean all) {
        String msg = all ? "Clear the entire error log?" : "Clear errors older than 30 days?";
        int ok = JOptionPane.showConfirmDialog(this, msg, "Clear errors", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            int n = all ? errorLogService.clearAll() : errorLogService.clearOlderThanDays(30);
            JOptionPane.showMessageDialog(this, "Removed " + n + " row(s).");
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void exportAudit() {
        File file = chooseSaveCsv("cmart_audit.csv");
        if (file == null) {
            return;
        }
        try {
            String event = String.valueOf(eventFilter.getSelectedItem());
            auditService.exportCsv(file, event, userFilter.getText(), 2000);
            JOptionPane.showMessageDialog(this, "Saved:\n" + file.getAbsolutePath());
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void exportErrors() {
        File file = chooseSaveCsv("cmart_errors.csv");
        if (file == null) {
            return;
        }
        try {
            errorLogService.exportCsv(file, 500);
            JOptionPane.showMessageDialog(this, "Saved:\n" + file.getAbsolutePath());
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private File chooseSaveCsv(String defaultName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(defaultName));
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getParentFile(), file.getName() + ".csv");
        }
        return file;
    }

    private void runBackup() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            File out = backupService.backupToFolder(chooser.getSelectedFile());
            JOptionPane.showMessageDialog(this, "Backup saved:\n" + out.getAbsolutePath());
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            refreshAll();
        }
    }

    private void runRestore() {
        int warn = JOptionPane.showConfirmDialog(this,
                "Restore overwrites live database data.\n"
                        + "Turn on Maintenance mode first if staff might still sign in.\n\nContinue?",
                "Restore backup", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (warn != JOptionPane.YES_OPTION) {
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("SQL dump", "sql"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            backupService.restoreFromFile(chooser.getSelectedFile());
            JOptionPane.showMessageDialog(this,
                    "Restore finished.\nRestart the app if screens look stale.");
            refreshAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            refreshAll();
        }
    }
}
