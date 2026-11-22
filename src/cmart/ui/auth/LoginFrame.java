package cmart.ui.auth;

import cmart.model.AppUser;
import cmart.service.AuthService;
import cmart.service.SettingService;
import cmart.config.SettingKeys;
import cmart.ui.shell.HomeFrame;
import cmart.ui.theme.UiTheme;
import cmart.util.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Sign-in gate before the main shell. Credentials come from {@code app_user}, not hardcoded checks.
 */
public class LoginFrame extends JFrame {

    private final AuthService authService = new AuthService();
    private final SettingService settingService = new SettingService();

    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JLabel shopLabel = new JLabel("CMart", SwingConstants.CENTER);
    private final JLabel hintLabel = new JLabel("Sign in to continue", SwingConstants.CENTER);

    public LoginFrame() {
        setTitle("CMart — Sign in");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(420, 320));
        setLocationRelativeTo(null);
        buildUi();
        loadShopBranding();
        getRootPane().setDefaultButton(createLoginButtonRef);
    }

    private JButton createLoginButtonRef;

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(UiTheme.PAGE);
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        shopLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        shopLabel.setForeground(UiTheme.SIDEBAR);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(UiTheme.MUTED);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(shopLabel, BorderLayout.CENTER);
        header.add(hintLabel, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        form.setBackground(Color.WHITE);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;
        form.add(label("Username"), gc);
        gc.gridx = 1;
        form.add(usernameField, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        form.add(label("Password"), gc);
        gc.gridx = 1;
        form.add(passwordField, gc);

        createLoginButtonRef = new JButton("Sign in");
        createLoginButtonRef.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createLoginButtonRef.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        });

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        form.add(createLoginButtonRef, gc);

        root.add(form, BorderLayout.CENTER);

        JLabel bootstrapHint = new JLabel(
                "<html><center>First run: <b>admin</b>/<b>admin123</b> · <b>cashier</b>/<b>cashier123</b> · <b>store</b>/<b>store123</b><br>Change passwords after install.</center></html>",
                SwingConstants.CENTER);
        bootstrapHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bootstrapHint.setForeground(UiTheme.MUTED);
        root.add(bootstrapHint, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private void loadShopBranding() {
        try {
            shopLabel.setText(settingService.get(SettingKeys.SHOP_NAME));
            hintLabel.setText(settingService.get(SettingKeys.SHOP_TAGLINE));
        } catch (SQLException e) {
            // Defaults already on labels
        }
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        if (Validation.isBlank(username) || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Enter username and password.");
            return;
        }
        try {
            AppUser user = authService.login(username, password);
            java.util.Arrays.fill(password, '\0');
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
                passwordField.setText("");
                passwordField.requestFocus();
                return;
            }
            dispose();
            HomeFrame home = new HomeFrame();
            home.setVisible(true);
        } catch (SQLException e) {
            java.util.Arrays.fill(password, '\0');
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage());
        }
    }
}
