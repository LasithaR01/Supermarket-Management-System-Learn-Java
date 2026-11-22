package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BackupLogDao {

    public void insert(String status, String filePath, String message, String createdBy) throws SQLException {
        String sql = "INSERT INTO backup_log (status, file_path, message, created_by) VALUES (?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, status == null ? "" : status);
            ps.setString(2, truncate(filePath, 255));
            ps.setString(3, truncate(message, 255));
            ps.setString(4, createdBy == null ? "" : createdBy);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public String[] findLatest() throws SQLException {
        String sql = "SELECT created_at, status, file_path, message, created_by "
                + "FROM backup_log ORDER BY backup_id DESC LIMIT 1";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new String[] {
                    rs.getString("created_at"),
                    rs.getString("status"),
                    rs.getString("file_path"),
                    rs.getString("message"),
                    rs.getString("created_by")
                };
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<String[]> findRecent(int limit) throws SQLException {
        String sql = "SELECT created_at, status, file_path, message, created_by "
                + "FROM backup_log ORDER BY backup_id DESC LIMIT ?";
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
                    rs.getString("status"),
                    rs.getString("file_path"),
                    rs.getString("message"),
                    rs.getString("created_by")
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
