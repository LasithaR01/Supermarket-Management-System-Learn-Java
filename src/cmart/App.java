package cmart;

import cmart.ui.shell.HomeFrame;
import cmart.ui.theme.UiTheme;

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
                new HomeFrame().setVisible(true);
            }
        });
    }
}
