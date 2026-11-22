package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditDao {

    public void insert(String eventType, String username, String actorUsername, String detail) throws SQLException {
        String sql = "INSERT INTO security_audit (event_type, username, actor_username, detail) VALUES (?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, eventType == null ? "" : eventType);
            ps.setString(2, username == null ? "" : username);
            ps.setString(3, actorUsername == null ? "" : actorUsername);
            ps.setString(4, detail == null ? "" : detail);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public List<String[]> findRecent(int limit) throws SQLException {
        String sql = "SELECT event_type, username, actor_username, detail, created_at "
                + "FROM security_audit ORDER BY audit_id DESC LIMIT ?";
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
                    rs.getString("event_type"),
                    rs.getString("username"),
                    rs.getString("actor_username"),
                    rs.getString("detail")
                });
            }
            return rows;
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
