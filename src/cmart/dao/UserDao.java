package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.AppUser;
import cmart.security.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String COLS =
            "user_id, username, display_name, password_hash, password_salt, role, active, "
                    + "must_change_password, failed_login_count, locked_until";

    public int countUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM app_user";
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

    public int countByRole(Role role) throws SQLException {
        String sql = "SELECT COUNT(*) FROM app_user WHERE role = ? AND active = 1";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, role.name());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public AppUser findById(int id) throws SQLException {
        String sql = "SELECT " + COLS + " FROM app_user WHERE user_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public AppUser findByUsername(String username) throws SQLException {
        String sql = "SELECT " + COLS + " FROM app_user WHERE username = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<AppUser> findAll() throws SQLException {
        String sql = "SELECT " + COLS + " FROM app_user ORDER BY username";
        List<AppUser> list = new ArrayList<AppUser>();
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return list;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void insert(AppUser user) throws SQLException {
        String sql = "INSERT INTO app_user (username, display_name, password_hash, password_salt, role, active, "
                + "must_change_password, failed_login_count) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getDisplayName());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPasswordSalt());
            ps.setString(5, user.getRole().name());
            ps.setInt(6, user.isActive() ? 1 : 0);
            ps.setInt(7, user.isMustChangePassword() ? 1 : 0);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void updatePassword(int userId, String hash, String salt, boolean mustChangePassword) throws SQLException {
        String sql = "UPDATE app_user SET password_hash = ?, password_salt = ?, must_change_password = ?, "
                + "failed_login_count = 0, locked_until = NULL WHERE user_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, hash);
            ps.setString(2, salt);
            ps.setInt(3, mustChangePassword ? 1 : 0);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void setMustChangePassword(int userId, boolean mustChange) throws SQLException {
        String sql = "UPDATE app_user SET must_change_password = ? WHERE user_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, mustChange ? 1 : 0);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void updateProfile(int userId, String displayName, Role role, boolean active) throws SQLException {
        String sql = "UPDATE app_user SET display_name = ?, role = ?, active = ? WHERE user_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, displayName);
            ps.setString(2, role.name());
            ps.setInt(3, active ? 1 : 0);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void clearLoginFailures(int userId) throws SQLException {
        String sql = "UPDATE app_user SET failed_login_count = 0, locked_until = NULL WHERE user_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void recordLoginFailure(int userId, int failedCount, Timestamp lockedUntil) throws SQLException {
        String sql = "UPDATE app_user SET failed_login_count = ?, locked_until = ? WHERE user_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, failedCount);
            if (lockedUntil == null) {
                ps.setNull(2, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(2, lockedUntil);
            }
            ps.setInt(3, userId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public int countLockedNow() throws SQLException {
        String sql = "SELECT COUNT(*) FROM app_user WHERE locked_until IS NOT NULL AND locked_until > NOW()";
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

    public List<AppUser> findLockedNow() throws SQLException {
        String sql = "SELECT " + COLS + " FROM app_user "
                + "WHERE locked_until IS NOT NULL AND locked_until > NOW() ORDER BY locked_until";
        List<AppUser> list = new ArrayList<AppUser>();
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return list;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void unlockUser(int userId) throws SQLException {
        clearLoginFailures(userId);
    }

    private AppUser map(ResultSet rs) throws SQLException {
        AppUser user = new AppUser();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setDisplayName(rs.getString("display_name"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPasswordSalt(rs.getString("password_salt"));
        user.setRole(Role.fromStorage(rs.getString("role")));
        user.setActive(rs.getInt("active") == 1);
        try {
            user.setMustChangePassword(rs.getInt("must_change_password") == 1);
        } catch (SQLException e) {
            user.setMustChangePassword(false);
        }
        try {
            user.setFailedLoginCount(rs.getInt("failed_login_count"));
        } catch (SQLException e) {
            user.setFailedLoginCount(0);
        }
        try {
            user.setLockedUntil(rs.getTimestamp("locked_until"));
        } catch (SQLException e) {
            user.setLockedUntil(null);
        }
        return user;
    }

    private Connection requireConnection() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        return con;
    }
}
