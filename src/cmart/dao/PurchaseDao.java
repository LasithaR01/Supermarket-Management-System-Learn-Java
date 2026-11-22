package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDao {

    public int insertOrder(String supplierId, String supplierName, String createdBy) throws SQLException {
        String sql = "INSERT INTO purchase_order (supplier_id, supplier_name, status, created_by) VALUES (?, ?, 'OPEN', ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet keys = null;
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, supplierId);
            ps.setString(2, supplierName);
            ps.setString(3, createdBy);
            ps.executeUpdate();
            keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
            throw new SQLException("No purchase order id generated");
        } finally {
            DaoSupport.close(keys, ps, con);
        }
    }

    public void insertLine(int poId, String barcode, String productName, String qty, String unitCost)
            throws SQLException {
        String sql = "INSERT INTO purchase_order_line (po_id, barcode, product_name, qty, unit_cost) VALUES (?,?,?,?,?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, poId);
            ps.setString(2, barcode);
            ps.setString(3, productName);
            ps.setString(4, qty);
            ps.setString(5, unitCost);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public List<Object[]> findOpenOrders() throws SQLException {
        String sql = "SELECT po_id, supplier_id, supplier_name, status, created_by, created_at "
                + "FROM purchase_order WHERE status = 'OPEN' ORDER BY po_id DESC";
        return queryOrders(sql);
    }

    public List<Object[]> findAllOrders() throws SQLException {
        String sql = "SELECT po_id, supplier_id, supplier_name, status, created_by, created_at "
                + "FROM purchase_order ORDER BY po_id DESC";
        return queryOrders(sql);
    }

    public List<Object[]> findLines(int poId) throws SQLException {
        String sql = "SELECT barcode, product_name, qty, unit_cost FROM purchase_order_line WHERE po_id = ?";
        List<Object[]> list = new ArrayList<Object[]>();
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, poId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) });
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void markReceived(int poId) throws SQLException {
        String sql = "UPDATE purchase_order SET status = 'RECEIVED', received_at = CURRENT_TIMESTAMP WHERE po_id = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, poId);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void increaseStockByBarcode(String barcode, double qty) throws SQLException {
        String sql = "UPDATE product SET Qty = CAST(CAST(Qty AS DECIMAL(12,2)) + ? AS CHAR) WHERE Bar_code = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, qty);
            ps.setString(2, barcode);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Product not found for barcode: " + barcode);
            }
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    private List<Object[]> queryOrders(String sql) throws SQLException {
        List<Object[]> list = new ArrayList<Object[]>();
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
                list.add(new Object[] {
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), String.valueOf(rs.getTimestamp(6))
                });
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
