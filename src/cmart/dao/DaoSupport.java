package cmart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Shared JDBC cleanup for DAOs.
 */
final class DaoSupport {

    private DaoSupport() {
    }

    static void close(ResultSet rs, Statement st, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ignored) {
        }
    }

    static void close(ResultSet rs, PreparedStatement ps, Connection con) {
        close(rs, (Statement) ps, con);
    }
}
