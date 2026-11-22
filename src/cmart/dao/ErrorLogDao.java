package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ErrorLogDao {

    public void insert(String level, String source, String message, String detail, String username)
            throws SQLException {
        String sql = "INSERT INTO app_error_log (level, source, message, detail, username) VALUES (?, ?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, truncate(level, 20));
            ps.setString(2, truncate(source, 80));
            ps.setString(3, truncate(message, 255));
            ps.setString(4, truncate(detail, 500));
            ps.setString(5, truncate(username, 50));
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public int countSinceHours(int hours) throws SQLException {
        String sql = "SELECT COUNT(*) FROM app_error_log WHERE created_at >= (NOW() - INTERVAL ? HOUR)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, Math.max(1, hours));
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<String[]> findRecent(int limit) throws SQLException {
        String sql = "SELECT created_at, level, source, message, username "
                + "FROM app_error_log ORDER BY error_id DESC LIMIT ?";
        List<String[]> rows = new ArrayList<String[]>();
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return rows;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, Math.max(1, limit));
            rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new String[] {
                    rs.getString("created_at"),
                    rs.getString("level"),
                    rs.getString("source"),
                    rs.getString("message"),
                    rs.getString("username")
                });
            }
            return rows;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    private static String truncate(String value, int max) {
        if (value == null) {
            return "";
        }
        return value.length() <= max ? value : value.substring(0, max);
    }

    private Connection requireConnection() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        return con;
    }
}
