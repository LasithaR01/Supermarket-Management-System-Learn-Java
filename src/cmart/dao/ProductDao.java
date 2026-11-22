package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<Product>();
        String sql = "SELECT pid, Product_Name, Bar_code, Price, Qty, Sid FROM product";
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
        } finally {
            close(rs, ps, con);
        }
        return list;
    }

    public Product findById(String id) throws SQLException {
        String sql = "SELECT pid, Product_Name, Bar_code, Price, Qty, Sid FROM product WHERE pid = ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            close(rs, ps, con);
        }
    }

    public Product findByName(String name) throws SQLException {
        String sql = "SELECT pid, Product_Name, Bar_code, Price, Qty, Sid FROM product WHERE Product_Name = ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            close(rs, ps, con);
        }
    }

    public Product findByBarcode(String barcode) throws SQLException {
        String sql = "SELECT pid, Product_Name, Bar_code, Price, Qty, Sid FROM product WHERE Bar_code = ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, barcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            close(rs, ps, con);
        }
    }

    /**
     * Atomically reduce stock when enough Qty remains.
     * @return true if a row was updated (stock deducted)
     */
    public boolean deductStockByBarcode(String barcode, double qty) throws SQLException {
        if (barcode == null || barcode.trim().isEmpty() || qty <= 0) {
            return false;
        }
        String sql = "UPDATE product SET Qty = CAST(CAST(Qty AS DECIMAL(12,2)) - ? AS CHAR) "
                + "WHERE Bar_code = ? AND CAST(Qty AS DECIMAL(12,2)) >= ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, qty);
            ps.setString(2, barcode);
            ps.setDouble(3, qty);
            return ps.executeUpdate() == 1;
        } finally {
            close(null, ps, con);
        }
    }

    public List<Product> findLowStock(double threshold) throws SQLException {
        List<Product> list = new ArrayList<Product>();
        String sql = "SELECT pid, Product_Name, Bar_code, Price, Qty, Sid FROM product "
                + "WHERE CAST(Qty AS DECIMAL(12,2)) <= ? ORDER BY CAST(Qty AS DECIMAL(12,2)) ASC";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return list;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, threshold);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } finally {
            close(rs, ps, con);
        }
    }

    public List<Product> findByNameLike(String name) throws SQLException {
        List<Product> list = new ArrayList<Product>();
        String sql = "SELECT pid, Product_Name, Bar_code, Price, Qty, Sid FROM product WHERE Product_Name LIKE ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return list;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } finally {
            close(rs, ps, con);
        }
        return list;
    }

    public void insert(Product product) throws SQLException {
        String sql = "INSERT INTO product (Product_Name, Bar_code, Price, Qty, Sid) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getBarcode());
            ps.setString(3, product.getPrice());
            ps.setString(4, product.getQty());
            ps.setString(5, product.getSupplierId());
            ps.executeUpdate();
        } finally {
            close(null, ps, con);
        }
    }

    public void update(Product product) throws SQLException {
        String sql = "UPDATE product SET Product_Name=?, Bar_code=?, Price=?, Qty=?, Sid=? WHERE pid=?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getBarcode());
            ps.setString(3, product.getPrice());
            ps.setString(4, product.getQty());
            ps.setString(5, product.getSupplierId());
            ps.setString(6, product.getId());
            ps.executeUpdate();
        } finally {
            close(null, ps, con);
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM product WHERE pid = ?";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
        } finally {
            close(null, ps, con);
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getString(1));
        p.setName(rs.getString(2));
        p.setBarcode(rs.getString(3));
        p.setPrice(rs.getString(4));
        p.setQty(rs.getString(5));
        p.setSupplierId(rs.getString(6));
        return p;
    }

    private void close(ResultSet rs, PreparedStatement ps, Connection con) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        try { if (con != null) con.close(); } catch (SQLException ignored) {}
    }
}
