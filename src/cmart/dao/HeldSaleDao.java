package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.HeldSale;
import cmart.util.CartCodec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HeldSaleDao {

    public void insert(HeldSale held) throws SQLException {
        String sql = "INSERT INTO held_sale (hold_code, customer_id, customer_name, cart_payload, created_by) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, held.getHoldCode());
            ps.setString(2, held.getCustomerId());
            ps.setString(3, held.getCustomerName());
            ps.setString(4, CartCodec.encode(held.getLines()));
            ps.setString(5, held.getCreatedBy());
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public List<HeldSale> findAll() throws SQLException {
        String sql = "SELECT hold_id, hold_code, customer_id, customer_name, cart_payload, created_by "
                + "FROM held_sale ORDER BY hold_id DESC";
        List<HeldSale> list = new ArrayList<HeldSale>();
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

    public HeldSale findByCode(String holdCode) throws SQLException {
        String sql = "SELECT hold_id, hold_code, customer_id, customer_name, cart_payload, created_by "
                + "FROM held_sale WHERE hold_code = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, holdCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void deleteByCode(String holdCode) throws SQLException {
        String sql = "DELETE FROM held_sale WHERE hold_code = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, holdCode);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    private HeldSale map(ResultSet rs) throws SQLException {
        HeldSale held = new HeldSale();
        held.setId(rs.getInt("hold_id"));
        held.setHoldCode(rs.getString("hold_code"));
        held.setCustomerId(rs.getString("customer_id"));
        held.setCustomerName(rs.getString("customer_name"));
        held.setLines(CartCodec.decode(rs.getString("cart_payload")));
        held.setCreatedBy(rs.getString("created_by"));
        return held;
    }

    private Connection requireConnection() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        return con;
    }
}
