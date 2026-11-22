package cmart.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Idempotent schema upgrades for existing MySQL databases.
 * Keeps SQL out of UI code and avoids manual phpMyAdmin steps for new tables.
 */
public final class SchemaMigrator {

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
                            + "`role` VARCHAR(20) NOT NULL,"
                            + "`active` TINYINT(1) NOT NULL DEFAULT 1,"
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

    private static void ensureColumn(Statement st, String table, String column, String definition) {
        try {
            st.execute("ALTER TABLE `" + table + "` ADD COLUMN `" + column + "` " + definition);
        } catch (SQLException ignored) {
            // Column already exists (or table missing on brand-new empty server — create via pos.sql)
        }
    }
}
