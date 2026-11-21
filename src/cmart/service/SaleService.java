package cmart.service;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sale rules that should not live inside Swing panels.
 */
public class SaleService {

    public String nextInvoiceNumber() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT val FROM extra WHERE exid = 1");
            rs = ps.executeQuery();
            int current = 0;
            if (rs.next()) {
                current = Integer.parseInt(rs.getString("val"));
            }
            return String.valueOf(current + 1);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { if (con != null) con.close(); } catch (SQLException ignored) {}
        }
    }
}
