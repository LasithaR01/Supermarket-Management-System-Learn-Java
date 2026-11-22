package cmart.ui.admin;

import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.service.UserAdminService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Store Owner / Super Admin screen to create staff and reset passwords — no support call needed.
 */
public class UsersPanel extends JPanel {

    private final UserAdminService userAdminService = new UserAdminService();

    private final JTextField usernameField = new JTextField(16);
    private final JTextField displayNameField = new JTextField(16);
    private final JComboBox roleBox = new JComboBox();
    private final JPasswordField passwordField = new JPasswordField(16);
    private final JCheckBox activeBox = new JCheckBox("Active", true);
    private final JLabel selectedLabel = new JLabel("Selected: (none)");
    private final JLabel roleHelp = new JLabel(" ");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] {"ID", "Username", "Display name", "Role", "Active", "Must change"}, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    private Integer selectedUserId;

    public UsersPanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        buildUi();
        if (!SessionContext.can(Permission.USER_ADMIN)) {
            JOptionPane.showMessageDialog(this, "You do not have permission to manage users.");
            return;
        }
        reloadRoles();
        reload();
    }

    private void buildUi() {
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        JLabel title = new JLabel("Users & access");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        JLabel subtitle = new JLabel(
                "Create staff here. Passwords need 8+ characters with a letter and a number.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(UiTheme.MUTED);
        north.add(title, BorderLayout.NORTH);
        north.add(subtitle, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(java.awt.Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;
        row = addRow(form, gc, row, "Username", usernameField);
        row = addRow(form, gc, row, "Display name", displayNameField);
        row = addRow(form, gc, row, "Role", roleBox);
        roleHelp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleHelp.setForeground(UiTheme.MUTED);
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        form.add(roleHelp, gc);
        row++;
        row = addRow(form, gc, row, "Password", passwordField);

        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        form.add(activeBox, gc);
        row++;

        gc.gridy = row++;
        form.add(selectedLabel, gc);

        roleBox.setRenderer(new DefaultListCellRenderer() {
            public java.awt.Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Role) {
                    setText(((Role) value).displayLabel());
                }
                return this;
            }
        });
        roleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRoleHelp();
            }
        });

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(actionButton("Create user", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createUser();
            }
        }));
        buttons.add(actionButton("Save profile", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        }));
        buttons.add(actionButton("Reset password", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPassword();
            }
        }));
        buttons.add(actionButton("Clear", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        }));
        buttons.add(actionButton("Refresh", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        }));

        gc.gridy = row;
        form.add(buttons, gc);

        add(form, BorderLayout.WEST);

        table.setRowHeight(30);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    return;
                }
                selectedUserId = Integer.valueOf(tableModel.getValueAt(r, 0).toString());
                usernameField.setText(String.valueOf(tableModel.getValueAt(r, 1)));
                usernameField.setEditable(false);
                displayNameField.setText(String.valueOf(tableModel.getValueAt(r, 2)));
                Role role = Role.fromStorage(String.valueOf(tableModel.getValueAt(r, 3)));
                roleBox.setSelectedItem(role);
                activeBox.setSelected("Yes".equals(String.valueOf(tableModel.getValueAt(r, 4))));
                passwordField.setText("");
                selectedLabel.setText("Selected: #" + selectedUserId + " " + usernameField.getText());
                updateRoleHelp();
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UiTheme.LINE));
        add(scroll, BorderLayout.CENTER);
        UiTheme.apply(this);
    }

    private void reloadRoles() {
        roleBox.removeAllItems();
        Role[] roles = userAdminService.assignableRoles();
        for (int i = 0; i < roles.length; i++) {
            roleBox.addItem(roles[i]);
        }
        if (roleBox.getItemCount() > 0) {
            roleBox.setSelectedItem(Role.CASHIER);
        }
        updateRoleHelp();
    }

    private void updateRoleHelp() {
        Object selected = roleBox.getSelectedItem();
        if (selected instanceof Role) {
            roleHelp.setText(((Role) selected).shortDescription());
        } else {
            roleHelp.setText(" ");
        }
    }

    private int addRow(JPanel form, GridBagConstraints gc, int row, String caption, java.awt.Component field) {
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel label = new JLabel(caption);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(label, gc);
        gc.gridx = 1;
        form.add(field, gc);
        return row + 1;
    }

    private JButton actionButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.addActionListener(listener);
        return button;
    }

    private void reload() {
        try {
            tableModel.setRowCount(0);
            List<AppUser> users = userAdminService.listUsers();
            for (AppUser user : users) {
                tableModel.addRow(new Object[] {
                    Integer.valueOf(user.getId()),
                    user.getUsername(),
                    user.getDisplayName(),
                    user.getRole().name(),
                    user.isActive() ? "Yes" : "No",
                    user.isMustChangePassword() ? "Yes" : "No"
                });
            }
            clearForm();
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load users: " + e.getMessage());
        }
    }

    private void createUser() {
        char[] password = passwordField.getPassword();
        try {
            usernameField.setEditable(true);
            userAdminService.createUser(
                    usernameField.getText(),
                    displayNameField.getText(),
                    (Role) roleBox.getSelectedItem(),
                    password);
            JOptionPane.showMessageDialog(this,
                    "User created. They must change the password on first sign-in.");
            reload();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not create user: " + e.getMessage());
        } finally {
            Arrays.fill(password, '\0');
            passwordField.setText("");
        }
    }

    private void saveProfile() {
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }
        try {
            userAdminService.updateUser(
                    selectedUserId.intValue(),
                    displayNameField.getText(),
                    (Role) roleBox.getSelectedItem(),
                    activeBox.isSelected());
            JOptionPane.showMessageDialog(this, "Profile saved.");
            reload();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not save profile: " + e.getMessage());
        }
    }

    private void resetPassword() {
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Reset password for " + usernameField.getText() + "?\nThey must change it on next sign-in.",
                "Reset password",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        char[] password = passwordField.getPassword();
        try {
            userAdminService.changePassword(selectedUserId.intValue(), password);
            JOptionPane.showMessageDialog(this, "Password reset. User must change it on next login.");
            passwordField.setText("");
            reload();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not reset password: " + e.getMessage());
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private void clearForm() {
        selectedUserId = null;
        usernameField.setEditable(true);
        usernameField.setText("");
        displayNameField.setText("");
        passwordField.setText("");
        if (roleBox.getItemCount() > 0) {
            roleBox.setSelectedItem(Role.CASHIER);
        }
        activeBox.setSelected(true);
        selectedLabel.setText("Selected: (none)");
        updateRoleHelp();
    }
}
