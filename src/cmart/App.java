package cmart;

import cmart.db.SchemaMigrator;
import cmart.service.AuthService;
import cmart.service.SettingService;
import cmart.ui.auth.LoginFrame;
import cmart.ui.theme.UiTheme;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Application entry point for CMart POS.
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        UiTheme.install();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SchemaMigrator.migrate();
                    new SettingService().ensureDefaults();
                    new AuthService().ensureBootstrapAdmin();
                    new LoginFrame().setVisible(true);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Cannot start CMart.\nCheck MySQL and config/db.properties.\n\n" + e.getMessage(),
                            "Startup error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
