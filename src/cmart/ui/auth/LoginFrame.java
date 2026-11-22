package cmart.ui.auth;

import cmart.config.SettingKeys;
import cmart.model.AppUser;
import cmart.service.AuthService;
import cmart.service.SettingService;
import cmart.ui.shell.HomeFrame;
import cmart.ui.theme.UiTheme;
import cmart.util.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Sign-in gate styled like the Home shell (green sidebar + soft page + white card).
 */
public class LoginFrame extends JFrame {

    private static final Color BRAND_SOFT = new Color(0x95D5B2);
    private static final Color BRAND_MUTED = new Color(0xB7E4C7);
    private static final Color FIELD_BORDER = new Color(0xC5D0C8);
    private static final Color FIELD_FOCUS = new Color(0x2D6A4F);
    private static final Color HINT = new Color(0x9AA5A0);
    private static final char ECHO_CHAR = '\u25CF';
    private static final String USERNAME_HINT = "Enter username";
    private static final String PASSWORD_HINT = "Enter password";

    private final AuthService authService = new AuthService();
    private final SettingService settingService = new SettingService();

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel shopLabel = new JLabel("CMart", SwingConstants.LEFT);
    private final JLabel tagLabel = new JLabel("Supermarket desk", SwingConstants.LEFT);
    private final JLabel errorLabel = new JLabel(" ");
    private JButton signInButton;
    private JToggleButton showPasswordToggle;
    private boolean passwordShowingHint = true;

    public LoginFrame() {
        setTitle("CMart — Sign in");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(860, 560));
        setPreferredSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UiTheme.PAGE);
        buildUi();
        loadShopBranding();
        getRootPane().setDefaultButton(signInButton);
        pack();
        setLocationRelativeTo(null);
        usernameField.requestFocusInWindow();
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.PAGE);
        root.putClientProperty(UiTheme.KEEP, Boolean.TRUE);

        root.add(buildBrandPanel(), BorderLayout.WEST);
        root.add(buildFormPanel(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildBrandPanel() {
        JPanel brand = new JPanel(new BorderLayout());
        brand.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        brand.setPreferredSize(new Dimension(320, 500));
        brand.setBackground(UiTheme.SIDEBAR);
        brand.setBorder(BorderFactory.createEmptyBorder(40, 32, 32, 32));

        JLabel eyebrow = new JLabel("SMART POS");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        eyebrow.setForeground(BRAND_SOFT);

        shopLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        shopLabel.setForeground(Color.WHITE);
        tagLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tagLabel.setForeground(BRAND_MUTED);

        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(false);
        top.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(0, 0, 10, 0);
        top.add(eyebrow, gc);
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 12, 0);
        top.add(shopLabel, gc);
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, 0, 0);
        top.add(tagLabel, gc);

        JPanel accent = new JPanel();
        accent.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        accent.setPreferredSize(new Dimension(56, 4));
        accent.setBackground(UiTheme.PRIMARY);
        accent.setOpaque(true);
        gc.gridy = 3;
        gc.insets = new Insets(22, 0, 0, 0);
        top.add(accent, gc);

        brand.add(top, BorderLayout.NORTH);
        return brand;
    }

    private JPanel buildFormPanel() {
        JPanel right = new JPanel(new GridBagLayout());
        right.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        right.setBackground(UiTheme.PAGE);
        right.setBorder(BorderFactory.createEmptyBorder(28, 40, 28, 40));

        JPanel card = new JPanel(new GridBagLayout());
        card.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        card.setOpaque(true);
        card.setBackground(UiTheme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(28, 28, 28, 28)));
        card.setPreferredSize(new Dimension(440, 400));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Sign in");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UiTheme.SIDEBAR);
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 4, 0);
        card.add(title, gc);

        JLabel subtitle = new JLabel("Use your shop account to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(UiTheme.MUTED);
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 22, 0);
        card.add(subtitle, gc);

        gc.gridy = 2;
        gc.insets = new Insets(0, 0, 6, 0);
        card.add(fieldCaption("Username"), gc);
        styleField(usernameField);
        installUsernameHint();
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, 14, 0);
        card.add(usernameField, gc);

        gc.gridy = 4;
        gc.insets = new Insets(0, 0, 6, 0);
        card.add(fieldCaption("Password"), gc);

        JPanel passwordRow = buildPasswordRow();
        installPasswordHint();
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, 8, 0);
        card.add(passwordRow, gc);

        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(UiTheme.DANGER);
        gc.gridy = 6;
        gc.insets = new Insets(0, 0, 10, 0);
        card.add(errorLabel, gc);

        signInButton = primaryButton("Sign in");
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        gc.gridy = 7;
        gc.insets = new Insets(6, 0, 10, 0);
        card.add(signInButton, gc);

        JButton forgot = linkButton("Forgot password?");
        forgot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ForgotPasswordDialog(LoginFrame.this).setVisible(true);
            }
        });
        gc.gridy = 8;
        gc.insets = new Insets(0, 0, 12, 0);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        card.add(forgot, gc);

        JLabel roleHint = new JLabel(
                "<html><div style='text-align:center'>"
                        + "Try a demo account: super/super123, owner/owner123,<br>"
                        + "cashier/cashier123, or store/store123."
                        + "</div></html>");
        roleHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleHint.setForeground(UiTheme.MUTED);
        roleHint.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 9;
        gc.insets = new Insets(4, 0, 0, 0);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        card.add(roleHint, gc);

        right.add(card);
        return right;
    }

    private void installUsernameHint() {
        showUsernameHint();
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (USERNAME_HINT.equals(usernameField.getText())) {
                    usernameField.setText("");
                    usernameField.setForeground(UiTheme.TEXT);
                }
            }

            public void focusLost(FocusEvent e) {
                if (usernameField.getText().trim().isEmpty()) {
                    showUsernameHint();
                }
            }
        });
    }

    private void showUsernameHint() {
        usernameField.setText(USERNAME_HINT);
        usernameField.setForeground(HINT);
    }

    private void installPasswordHint() {
        showPasswordHint();
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (passwordShowingHint) {
                    passwordShowingHint = false;
                    passwordField.setText("");
                    passwordField.setForeground(UiTheme.TEXT);
                    applyPasswordEcho();
                }
            }

            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    showPasswordHint();
                }
            }
        });
    }

    private void showPasswordHint() {
        passwordShowingHint = true;
        passwordField.setEchoChar((char) 0);
        passwordField.setText(PASSWORD_HINT);
        passwordField.setForeground(HINT);
        if (showPasswordToggle != null) {
            showPasswordToggle.setSelected(false);
            showPasswordToggle.setText("Show");
            showPasswordToggle.setForeground(UiTheme.NEUTRAL);
        }
    }

    private void applyPasswordEcho() {
        if (showPasswordToggle != null && showPasswordToggle.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar(ECHO_CHAR);
        }
    }

    private String readUsername() {
        String value = usernameField.getText();
        if (value == null || USERNAME_HINT.equals(value)) {
            return "";
        }
        return value.trim();
    }

    private char[] readPassword() {
        if (passwordShowingHint) {
            return new char[0];
        }
        return passwordField.getPassword();
    }

    private JPanel buildPasswordRow() {
        final JPanel row = new JPanel(new BorderLayout(0, 0));
        row.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        row.setOpaque(true);
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 0)));
        row.setPreferredSize(new Dimension(100, 40));

        passwordField.setFont(UiTheme.FONT);
        passwordField.setForeground(UiTheme.TEXT);
        passwordField.setBackground(Color.WHITE);
        passwordField.setCaretColor(UiTheme.TEXT);
        passwordField.setBorder(new EmptyBorder(8, 12, 8, 8));
        passwordField.setPreferredSize(new Dimension(100, 38));
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                row.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(FIELD_FOCUS, 2, true),
                        new EmptyBorder(0, 0, 0, 0)));
            }

            public void focusLost(FocusEvent e) {
                row.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(FIELD_BORDER, 1, true),
                        new EmptyBorder(0, 0, 0, 0)));
            }
        });

        showPasswordToggle = new JToggleButton("Show");
        showPasswordToggle.setUI(new BasicButtonUI());
        showPasswordToggle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        showPasswordToggle.setForeground(UiTheme.NEUTRAL);
        showPasswordToggle.setBackground(Color.WHITE);
        showPasswordToggle.setFocusPainted(false);
        showPasswordToggle.setBorderPainted(false);
        showPasswordToggle.setContentAreaFilled(false);
        showPasswordToggle.setOpaque(true);
        showPasswordToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPasswordToggle.setBorder(new EmptyBorder(8, 10, 8, 12));
        showPasswordToggle.setToolTipText("Show or hide password");
        showPasswordToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (passwordShowingHint) {
                    showPasswordToggle.setSelected(false);
                    return;
                }
                if (showPasswordToggle.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                    showPasswordToggle.setText("Hide");
                    showPasswordToggle.setForeground(UiTheme.PRIMARY);
                } else {
                    passwordField.setEchoChar(ECHO_CHAR);
                    showPasswordToggle.setText("Show");
                    showPasswordToggle.setForeground(UiTheme.NEUTRAL);
                }
            }
        });

        row.add(passwordField, BorderLayout.CENTER);
        row.add(showPasswordToggle, BorderLayout.EAST);
        return row;
    }

    private JLabel fieldCaption(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(UiTheme.NEUTRAL);
        return label;
    }

    private void styleField(final JTextField field) {
        field.setFont(UiTheme.FONT);
        field.setForeground(UiTheme.TEXT);
        field.setBackground(Color.WHITE);
        field.setCaretColor(UiTheme.TEXT);
        field.setPreferredSize(new Dimension(100, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(FIELD_FOCUS, 2, true),
                        new EmptyBorder(7, 11, 7, 11)));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(FIELD_BORDER, 1, true),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(UiTheme.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 18, 12, 18));
        button.setPreferredSize(new Dimension(100, 44));
        return button;
    }

    private JButton linkButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(UiTheme.INFO);
        button.setBackground(UiTheme.CARD);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(4, 4, 4, 4));
        return button;
    }

    private void loadShopBranding() {
        try {
            shopLabel.setText(settingService.get(SettingKeys.SHOP_NAME));
            tagLabel.setText(settingService.get(SettingKeys.SHOP_TAGLINE));
            setTitle(settingService.get(SettingKeys.SHOP_NAME) + " — Sign in");
        } catch (SQLException e) {
            // Defaults already on labels
        }
    }

    private void attemptLogin() {
        errorLabel.setText(" ");
        String username = readUsername();
        char[] password = readPassword();
        if (Validation.isBlank(username) || password.length == 0) {
            errorLabel.setText("Enter username and password.");
            return;
        }
        signInButton.setEnabled(false);
        try {
            AppUser user = authService.login(username, password);
            Arrays.fill(password, '\0');
            if (user == null) {
                errorLabel.setText("Invalid username or password. (5 fails lock the account for 15 minutes.)");
                showPasswordHint();
                passwordField.requestFocus();
                return;
            }
            if (user.isMustChangePassword()) {
                ChangePasswordDialog dialog = new ChangePasswordDialog(this, true);
                dialog.setVisible(true);
                if (!dialog.isSucceeded()) {
                    authService.logout();
                    errorLabel.setText("Password change required before continuing.");
                    return;
                }
            }
            dispose();
            HomeFrame home = new HomeFrame();
            home.setVisible(true);
        } catch (IllegalStateException e) {
            Arrays.fill(password, '\0');
            errorLabel.setText(e.getMessage());
            showPasswordHint();
        } catch (SQLException e) {
            Arrays.fill(password, '\0');
            errorLabel.setText("Login failed: " + e.getMessage());
        } finally {
            signInButton.setEnabled(true);
        }
    }
}
