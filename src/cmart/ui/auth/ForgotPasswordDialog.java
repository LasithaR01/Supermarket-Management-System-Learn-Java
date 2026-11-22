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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Owner / Super Admin password recovery via shop recovery PIN.
 */
public class ForgotPasswordDialog extends JDialog {

    private final AuthService authService = new AuthService();
    private final JTextField usernameField = new JTextField(16);
    private final JPasswordField pinField = new JPasswordField(16);
    private final JPasswordField newField = new JPasswordField(16);
    private final JPasswordField confirmField = new JPasswordField(16);

    public ForgotPasswordDialog(Frame owner) {
        super(owner, "Forgot password", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        root.setBackground(UiTheme.PAGE);
        root.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("Reset with recovery PIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UiTheme.SIDEBAR);

        JLabel help = new JLabel(
                "<html><body style='width:360px;color:#5C6B73'>"
                        + "<b>Store Owner / Super Admin:</b> reset with the shop recovery PIN.<br>"
                        + "<b>Cashier / Store Keeper:</b> ask the Store Owner to reset in Users."
                        + "</body></html>");
        help.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel north = new JPanel(new GridBagLayout());
        north.setOpaque(false);
        GridBagConstraints nc = new GridBagConstraints();
        nc.gridx = 0;
        nc.anchor = GridBagConstraints.WEST;
        north.add(title, nc);
        nc.gridy = 1;
        nc.insets = new Insets(8, 0, 0, 0);
        north.add(help, nc);
        root.add(north, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        form.setOpaque(true);
        form.setBackground(UiTheme.CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(14, 14, 14, 14)));

        styleField(usernameField);
        styleField(pinField);
        styleField(newField);
        styleField(confirmField);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        row = addRow(form, gc, row, "Username", usernameField);
        row = addRow(form, gc, row, "Recovery PIN", pinField);
        row = addRow(form, gc, row, "New password", newField);
        addRow(form, gc, row, "Confirm", confirmField);
        root.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton reset = primaryButton("Reset password");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });
        JButton cancel = secondaryButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttons.add(reset);
        buttons.add(cancel);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        getRootPane().setDefaultButton(reset);
    }

    private int addRow(JPanel form, GridBagConstraints gc, int row, String caption, java.awt.Component field) {
        gc.gridx = 0;
        gc.gridy = row;
        JLabel label = new JLabel(caption);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(UiTheme.NEUTRAL);
        form.add(label, gc);
        gc.gridx = 1;
        form.add(field, gc);
        return row + 1;
    }

    private void styleField(JTextField field) {
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
        char[] pin = pinField.getPassword();
        char[] neu = newField.getPassword();
        char[] confirm = confirmField.getPassword();
        try {
            if (!Arrays.equals(neu, confirm)) {
                JOptionPane.showMessageDialog(this, "New password and confirmation do not match.");
                return;
            }
            authService.resetPasswordWithRecoveryPin(usernameField.getText(), pin, neu);
            JOptionPane.showMessageDialog(this, "Password reset. You can sign in with the new password.");
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Reset failed: " + e.getMessage());
        } finally {
            Arrays.fill(pin, '\0');
            Arrays.fill(neu, '\0');
            Arrays.fill(confirm, '\0');
            pinField.setText("");
            newField.setText("");
            confirmField.setText("");
        }
    }
}
