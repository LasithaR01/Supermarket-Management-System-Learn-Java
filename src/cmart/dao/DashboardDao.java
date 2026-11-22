package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardDao {

    public double todaySalesTotal() throws SQLException {
        // Legacy schema has no sale date — use latest N paid sales as proxy "today activity"
        String sql = "SELECT Total_Bill, Status FROM sales ORDER BY saleid DESC LIMIT 50";
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

    public int unpaidCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sales WHERE Status = 'UnPaid' OR Status = 'Partial'";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<String[]> topProducts(int limit) throws SQLException {
        String sql = "SELECT Product_Name, SUM(CAST(qty AS DECIMAL(12,2))) AS q "
                + "FROM cart GROUP BY Product_Name ORDER BY q DESC LIMIT ?";
        List<String[]> list = new ArrayList<String[]>();
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[] { rs.getString(1), rs.getString(2) });
            }
            return list;
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
