package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftDao {

    public boolean hasOpenShift(String username) throws SQLException {
        return findOpenShiftId(username) != null;
    }

    public Integer findOpenShiftId(String username) throws SQLException {
        String sql = "SELECT shift_id FROM cashier_shift WHERE username = ? AND status = 'OPEN' LIMIT 1";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Integer.valueOf(rs.getInt(1));
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void openShift(String username, String openFloat) throws SQLException {
        String sql = "INSERT INTO cashier_shift (username, open_float, status) VALUES (?, ?, 'OPEN')";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, openFloat);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void closeShift(int shiftId, String closeCash, String expectedCash, String difference)
            throws SQLException {
        String sql = "UPDATE cashier_shift SET closed_at = CURRENT_TIMESTAMP, close_cash = ?, expected_cash = ?, "
                + "difference_amt = ?, status = 'CLOSED' WHERE shift_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, closeCash);
            ps.setString(2, expectedCash);
            ps.setString(3, difference);
            ps.setInt(4, shiftId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public String[] findOpenShiftRow(String username) throws SQLException {
        String sql = "SELECT shift_id, username, opened_at, open_float FROM cashier_shift "
                + "WHERE username = ? AND status = 'OPEN' LIMIT 1";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new String[] {
                    rs.getString(1), rs.getString(2), String.valueOf(rs.getTimestamp(3)), rs.getString(4)
                };
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public double sumPaidSalesSince(String openedAt) throws SQLException {
        // Approximate cash intake: paid bills since shift open (Balance can be change given)
        String sql = "SELECT Total_Bill, Balance, Status FROM sales WHERE saleid > 0";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        double sum = 0;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("Status");
                if ("Paid".equalsIgnoreCase(status) || "Partial".equalsIgnoreCase(status)) {
                    try {
                        sum += Double.parseDouble(rs.getString("Total_Bill"));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return sum;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    private Connection requireConnection() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        return con;
    }
}
