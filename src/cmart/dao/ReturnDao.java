package cmart.dao;

import cmart.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReturnDao {

    public void insert(String inid, String barcode, String productName, String qty,
                       String refundAmount, String reason, String createdBy) throws SQLException {
        String sql = "INSERT INTO sale_return (original_inid, barcode, product_name, qty, refund_amount, reason, created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, inid);
            ps.setString(2, barcode);
            ps.setString(3, productName);
            ps.setString(4, qty);
            ps.setString(5, refundAmount);
            ps.setString(6, reason);
            ps.setString(7, createdBy);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public List<String[]> findByInvoice(String inid) throws SQLException {
        String sql = "SELECT return_id, original_inid, barcode, product_name, qty, refund_amount, reason, created_by "
                + "FROM sale_return WHERE original_inid = ? ORDER BY return_id DESC";
        List<String[]> list = new ArrayList<String[]>();
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, inid);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[] {
                    rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)
                });
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<Object[]> findCartLinesByInvoice(String inid) throws SQLException {
        String sql = "SELECT Product_Name, Bar_code, qty, Unit_Price, Total_Price FROM cart WHERE INID = ?";
        List<Object[]> list = new ArrayList<Object[]>();
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, inid);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                    rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)
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
