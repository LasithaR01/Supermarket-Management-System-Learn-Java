package cmart.db;

import cmart.config.AppInfo;
import cmart.config.SettingKeys;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Idempotent schema upgrades for existing MySQL databases.
 * Keeps SQL out of UI code and avoids manual phpMyAdmin steps for new tables.
 */
public final class SchemaMigrator {

    private static final String[] EXPECTED_TABLES = {
        "app_user", "app_setting", "security_audit", "app_error_log", "backup_log",
        "customer", "supplier", "employee", "product", "sales", "sale_return",
        "cashier_shift", "purchase_order", "held_sale"
    };

    private SchemaMigrator() {
    }

    public static void migrate() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection — check config/db.properties");
        }
        Statement st = null;
        try {
            st = con.createStatement();
            st.execute(
                    "CREATE TABLE IF NOT EXISTS `app_user` ("
                            + "`user_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`username` VARCHAR(50) NOT NULL,"
                            + "`display_name` VARCHAR(80) NOT NULL,"
                            + "`password_hash` VARCHAR(128) NOT NULL,"
                            + "`password_salt` VARCHAR(64) NOT NULL,"
                            + "`role` VARCHAR(30) NOT NULL,"
                            + "`active` TINYINT(1) NOT NULL DEFAULT 1,"
                            + "`must_change_password` TINYINT(1) NOT NULL DEFAULT 0,"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "PRIMARY KEY (`user_id`),"
                            + "UNIQUE KEY `uk_app_user_username` (`username`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            st.execute(
                    "CREATE TABLE IF NOT EXISTS `app_setting` ("
                            + "`setting_key` VARCHAR(64) NOT NULL,"
                            + "`setting_value` VARCHAR(255) NOT NULL,"
                            + "PRIMARY KEY (`setting_key`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            // Expand legacy 3-column people tables to match the Swing forms
            ensureColumn(st, "customer", "billing_address", "VARCHAR(120) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "shipping_address", "VARCHAR(120) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "bank", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "city", "VARCHAR(60) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "person_name", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "contact_person", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "person_tp", "VARCHAR(20) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "email", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "customer", "online", "VARCHAR(40) NOT NULL DEFAULT ''");

            ensureColumn(st, "supplier", "billing_address", "VARCHAR(120) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "shipping_address", "VARCHAR(120) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "bank", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "city", "VARCHAR(60) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "person_name", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "contact_person", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "person_tp", "VARCHAR(20) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "email", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "supplier", "online", "VARCHAR(40) NOT NULL DEFAULT ''");

            ensureColumn(st, "employee", "main_address", "VARCHAR(120) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "temp_address", "VARCHAR(120) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "bank", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "city", "VARCHAR(60) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "person_name", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "contact_person", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "person_tp", "VARCHAR(20) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "email", "VARCHAR(80) NOT NULL DEFAULT ''");
            ensureColumn(st, "employee", "online", "VARCHAR(40) NOT NULL DEFAULT ''");

            ensureColumn(st, "sales", "discount_amount", "VARCHAR(20) NOT NULL DEFAULT '0'");
            ensureColumn(st, "sales", "pay_method", "VARCHAR(20) NOT NULL DEFAULT 'CASH'");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `held_sale` ("
                            + "`hold_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`hold_code` VARCHAR(20) NOT NULL,"
                            + "`customer_id` VARCHAR(20) NOT NULL DEFAULT '',"
                            + "`customer_name` VARCHAR(80) NOT NULL DEFAULT '',"
                            + "`cart_payload` MEDIUMTEXT NOT NULL,"
                            + "`created_by` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "PRIMARY KEY (`hold_id`),"
                            + "UNIQUE KEY `uk_held_sale_code` (`hold_code`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            ensureColumn(st, "sales", "tax_amount", "VARCHAR(20) NOT NULL DEFAULT '0'");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `sale_return` ("
                            + "`return_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`original_inid` VARCHAR(20) NOT NULL,"
                            + "`barcode` VARCHAR(50) NOT NULL,"
                            + "`product_name` VARCHAR(80) NOT NULL,"
                            + "`qty` VARCHAR(20) NOT NULL,"
                            + "`refund_amount` VARCHAR(20) NOT NULL,"
                            + "`reason` VARCHAR(120) NOT NULL DEFAULT '',"
                            + "`created_by` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "PRIMARY KEY (`return_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `cashier_shift` ("
                            + "`shift_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`username` VARCHAR(50) NOT NULL,"
                            + "`opened_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "`closed_at` TIMESTAMP NULL DEFAULT NULL,"
                            + "`open_float` VARCHAR(20) NOT NULL DEFAULT '0',"
                            + "`close_cash` VARCHAR(20) NULL DEFAULT NULL,"
                            + "`expected_cash` VARCHAR(20) NULL DEFAULT NULL,"
                            + "`difference_amt` VARCHAR(20) NULL DEFAULT NULL,"
                            + "`status` VARCHAR(20) NOT NULL DEFAULT 'OPEN',"
                            + "PRIMARY KEY (`shift_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `purchase_order` ("
                            + "`po_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`supplier_id` VARCHAR(20) NOT NULL,"
                            + "`supplier_name` VARCHAR(80) NOT NULL,"
                            + "`status` VARCHAR(20) NOT NULL DEFAULT 'OPEN',"
                            + "`created_by` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "`received_at` TIMESTAMP NULL DEFAULT NULL,"
                            + "PRIMARY KEY (`po_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `purchase_order_line` ("
                            + "`line_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`po_id` INT NOT NULL,"
                            + "`barcode` VARCHAR(50) NOT NULL,"
                            + "`product_name` VARCHAR(80) NOT NULL,"
                            + "`qty` VARCHAR(20) NOT NULL,"
                            + "`unit_cost` VARCHAR(20) NOT NULL DEFAULT '0',"
                            + "PRIMARY KEY (`line_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            // Auth upgrades: force password change + widen role column for SUPER_ADMIN / STORE_OWNER
            ensureColumn(st, "app_user", "must_change_password", "TINYINT(1) NOT NULL DEFAULT 0");
            ensureColumn(st, "app_user", "failed_login_count", "INT NOT NULL DEFAULT 0");
            ensureColumn(st, "app_user", "locked_until", "TIMESTAMP NULL DEFAULT NULL");
            try {
                st.execute("ALTER TABLE `app_user` MODIFY COLUMN `role` VARCHAR(30) NOT NULL");
            } catch (SQLException ignored) {
            }
            // Legacy ADMIN → STORE_OWNER (shop boss); Super Admin is a separate role for the dev team
            try {
                st.execute("UPDATE `app_user` SET `role` = 'STORE_OWNER' WHERE `role` = 'ADMIN'");
            } catch (SQLException ignored) {
            }

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `security_audit` ("
                            + "`audit_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`event_type` VARCHAR(40) NOT NULL,"
                            + "`username` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`actor_username` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`detail` VARCHAR(255) NOT NULL DEFAULT '',"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "PRIMARY KEY (`audit_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `app_error_log` ("
                            + "`error_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`level` VARCHAR(20) NOT NULL DEFAULT 'ERROR',"
                            + "`source` VARCHAR(80) NOT NULL DEFAULT '',"
                            + "`message` VARCHAR(255) NOT NULL,"
                            + "`detail` VARCHAR(500) NOT NULL DEFAULT '',"
                            + "`username` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "PRIMARY KEY (`error_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS `backup_log` ("
                            + "`backup_id` INT NOT NULL AUTO_INCREMENT,"
                            + "`status` VARCHAR(20) NOT NULL,"
                            + "`file_path` VARCHAR(255) NOT NULL DEFAULT '',"
                            + "`message` VARCHAR(255) NOT NULL DEFAULT '',"
                            + "`created_by` VARCHAR(50) NOT NULL DEFAULT '',"
                            + "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "PRIMARY KEY (`backup_id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

            try {
                st.execute("ALTER TABLE `app_error_log` MODIFY COLUMN `detail` MEDIUMTEXT");
            } catch (SQLException ignored) {
            }

            recordMigrateStamp(con);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    /** Table presence checklist for Super Admin health. */
    public static List<String> tableStatus() throws SQLException {
        List<String> lines = new ArrayList<String>();
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            lines.add("FAIL — no connection");
            return lines;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM information_schema.tables "
                            + "WHERE table_schema = DATABASE() AND table_name = ?");
            for (int i = 0; i < EXPECTED_TABLES.length; i++) {
                String table = EXPECTED_TABLES[i];
                ps.setString(1, table);
                rs = ps.executeQuery();
                boolean ok = rs.next() && rs.getInt(1) > 0;
                rs.close();
                rs = null;
                lines.add((ok ? "OK  " : "MISS") + "  " + table);
            }
            return lines;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {
                }
            }
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private static void recordMigrateStamp(Connection con) {
        try {
            String stamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            upsertSetting(con, SettingKeys.SCHEMA_LAST_MIGRATE, stamp);
            upsertSetting(con, SettingKeys.SCHEMA_VERSION, AppInfo.SCHEMA_VERSION);
            upsertSetting(con, SettingKeys.MAINTENANCE_MODE, "false");
            upsertSetting(con, SettingKeys.ACL_ALLOW_DISCOUNT, "true");
            upsertSetting(con, SettingKeys.ACL_ALLOW_PRODUCT_DELETE, "true");
            upsertSetting(con, SettingKeys.ACL_ALLOW_REFUND, "true");
        } catch (SQLException ignored) {
        }
    }

    private static void upsertSetting(Connection con, String key, String value) throws SQLException {
        // Only insert if missing — never overwrite maintenance/ACL toggles on every startup
        PreparedStatement find = null;
        ResultSet rs = null;
        PreparedStatement insert = null;
        try {
            find = con.prepareStatement("SELECT setting_value FROM app_setting WHERE setting_key = ?");
            find.setString(1, key);
            rs = find.executeQuery();
            if (rs.next()) {
                if (SettingKeys.SCHEMA_LAST_MIGRATE.equals(key) || SettingKeys.SCHEMA_VERSION.equals(key)) {
                    rs.close();
                    rs = null;
                    find.close();
                    find = null;
                    PreparedStatement up = con.prepareStatement(
                            "UPDATE app_setting SET setting_value = ? WHERE setting_key = ?");
                    try {
                        up.setString(1, value);
                        up.setString(2, key);
                        up.executeUpdate();
                    } finally {
                        up.close();
                    }
                }
                return;
            }
            insert = con.prepareStatement(
                    "INSERT INTO app_setting (setting_key, setting_value) VALUES (?, ?)");
            insert.setString(1, key);
            insert.setString(2, value == null ? "" : value);
            insert.executeUpdate();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
            if (find != null) {
                try {
                    find.close();
                } catch (SQLException ignored) {
                }
            }
            if (insert != null) {
                try {
                    insert.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private static void ensureColumn(Statement st, String table, String column, String definition) {
        try {
            st.execute("ALTER TABLE `" + table + "` ADD COLUMN `" + column + "` " + definition);
        } catch (SQLException ignored) {
            // Column already exists (or table missing on brand-new empty server — create via sql/pos.sql)
        }
    }
}
