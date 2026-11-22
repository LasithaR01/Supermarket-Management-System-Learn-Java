package cmart.ui.auth;

import cmart.service.AuthService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Self-service or forced password change dialog.
 */
public class ChangePasswordDialog extends JDialog {

    private final AuthService authService = new AuthService();
    private final JPasswordField currentField = new JPasswordField(18);
    private final JPasswordField newField = new JPasswordField(18);
    private final JPasswordField confirmField = new JPasswordField(18);
    private boolean succeeded;

    public ChangePasswordDialog(Frame owner, boolean forced) {
        super(owner, forced ? "Change password (required)" : "Change password", true);
        setDefaultCloseOperation(forced ? DO_NOTHING_ON_CLOSE : DISPOSE_ON_CLOSE);
        buildUi(forced);
        pack();
        setLocationRelativeTo(owner);
        setMinimumSize(getPreferredSize());
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    private void buildUi(final boolean forced) {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        root.setBackground(UiTheme.PAGE);
        root.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel(forced ? "Set a new password" : "Change password");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UiTheme.SIDEBAR);

        JLabel hint = new JLabel(forced
                ? "<html>You must set a new password before continuing.</html>"
                : "<html>Enter your current password, then choose a new one (min 6 characters).</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(UiTheme.MUTED);

        JPanel north = new JPanel(new GridBagLayout());
        north.setOpaque(false);
        GridBagConstraints nc = new GridBagConstraints();
        nc.gridx = 0;
        nc.anchor = GridBagConstraints.WEST;
        north.add(title, nc);
        nc.gridy = 1;
        nc.insets = new Insets(6, 0, 0, 0);
        north.add(hint, nc);
        root.add(north, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        form.setOpaque(true);
        form.setBackground(UiTheme.CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(14, 14, 14, 14)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        styleField(currentField);
        styleField(newField);
        styleField(confirmField);
        gc.gridx = 0;
        gc.gridy = 0;
        form.add(label("Current password"), gc);
        gc.gridx = 1;
        form.add(currentField, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        form.add(label("New password"), gc);
        gc.gridx = 1;
        form.add(newField, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        form.add(label("Confirm new"), gc);
        gc.gridx = 1;
        form.add(confirmField, gc);

        root.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton save = primaryButton("Update password");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });
        buttons.add(save);
        if (!forced) {
            JButton cancel = secondaryButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            buttons.add(cancel);
        }
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        getRootPane().setDefaultButton(save);
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(UiTheme.NEUTRAL);
        return lbl;
    }

    private void styleField(JPasswordField field) {
        field.setFont(UiTheme.FONT);
        field.setForeground(UiTheme.TEXT);
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(220, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xC5D0C8), 1, true),
                new EmptyBorder(6, 10, 6, 10)));
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setFont(UiTheme.FONT_BOLD);
        button.setBackground(UiTheme.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        return button;
    }

    private JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setFont(UiTheme.FONT_BOLD);
        button.setBackground(UiTheme.NEUTRAL);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        return button;
    }

    private void submit() {
        char[] current = currentField.getPassword();
        char[] neu = newField.getPassword();
        char[] confirm = confirmField.getPassword();
        try {
            if (!Arrays.equals(neu, confirm)) {
                JOptionPane.showMessageDialog(this, "New password and confirmation do not match.");
                return;
            }
            authService.changeOwnPassword(current, neu);
            succeeded = true;
            JOptionPane.showMessageDialog(this, "Password updated.");
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not update password: " + e.getMessage());
        } finally {
            Arrays.fill(current, '\0');
            Arrays.fill(neu, '\0');
            Arrays.fill(confirm, '\0');
            currentField.setText("");
            newField.setText("");
            confirmField.setText("");
        }
    }
}
