package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingDao {

    public Map<String, String> findAll() throws SQLException {
        Map<String, String> map = new LinkedHashMap<String, String>();
        String sql = "SELECT setting_key, setting_value FROM app_setting ORDER BY setting_key";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return map;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }
            return map;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public String findValue(String key) throws SQLException {
        String sql = "SELECT setting_value FROM app_setting WHERE setting_key = ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("setting_value");
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void upsert(String key, String value) throws SQLException {
        String sql = "INSERT INTO app_setting (setting_key, setting_value) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, key);
            ps.setString(2, value == null ? "" : value);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }
}
