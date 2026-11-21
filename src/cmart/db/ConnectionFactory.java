package cmart.db;

import cmart.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Single place to open MySQL connections.
 */
public final class ConnectionFactory {

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        try {
            Class.forName(DatabaseConfig.driver());
            return DriverManager.getConnection(
                    DatabaseConfig.url(),
                    DatabaseConfig.user(),
                    DatabaseConfig.password());
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database Connection Error: " + e);
            return null;
        }
    }

    /** @deprecated use {@link #getConnection()} */
    @Deprecated
    public static Connection mycon() {
        return getConnection();
    }
}
