package cmart.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads JDBC settings from config/db.properties (preferred) or classpath defaults.
 */
public final class DatabaseConfig {

    private static final Properties PROPS = new Properties();

    static {
        load();
    }

    private DatabaseConfig() {
    }

    private static void load() {
        File file = new File("config/db.properties");
        if (file.isFile()) {
            try (InputStream in = new FileInputStream(file)) {
                PROPS.load(in);
                return;
            } catch (IOException e) {
                System.out.println("Could not read config/db.properties: " + e.getMessage());
            }
        }
        // Safe local defaults for first run if the file is missing
        PROPS.setProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
        PROPS.setProperty("jdbc.url", "jdbc:mysql://localhost:3307/cmartdb");
        PROPS.setProperty("jdbc.user", "root");
        PROPS.setProperty("jdbc.password", "");
    }

    public static String driver() {
        return PROPS.getProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
    }

    public static String url() {
        return PROPS.getProperty("jdbc.url");
    }

    public static String user() {
        return PROPS.getProperty("jdbc.user", "root");
    }

    public static String password() {
        return PROPS.getProperty("jdbc.password", "");
    }
}
