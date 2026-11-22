package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.CartItem;
import cmart.model.InvoiceFilter;
import cmart.model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaleDao {

    private static final String SALE_COLS =
            "saleid, INID, Cid, Customer_Name, Total_Qty, Total_Bill, Status, Balance, "
                    + "discount_amount, pay_method, tax_amount";

    public String peekNextInvoiceNumber() throws SQLException {
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT val FROM extra WHERE exid = 1");
            rs = ps.executeQuery();
            int current = 0;
            if (rs.next()) {
                current = Integer.parseInt(rs.getString("val").trim());
            }
            return String.valueOf(current + 1);
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public String allocateInvoiceNumber() throws SQLException {
        Connection con = requireConnection();
        PreparedStatement lockPs = null;
        PreparedStatement updPs = null;
        ResultSet rs = null;
        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            lockPs = con.prepareStatement("SELECT val FROM extra WHERE exid = 1 FOR UPDATE");
            rs = lockPs.executeQuery();
            int current = 0;
            if (rs.next()) {
                current = Integer.parseInt(rs.getString("val").trim());
            } else {
                throw new SQLException("extra row exid=1 is missing");
            }
            int next = current + 1;

            updPs = con.prepareStatement("UPDATE extra SET val = ? WHERE exid = 1");
            updPs.setString(1, String.valueOf(next));
            if (updPs.executeUpdate() != 1) {
                throw new SQLException("Failed to update invoice counter");
            }
            con.commit();
            return String.valueOf(next);
        } catch (SQLException | NumberFormatException e) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            throw new SQLException("Invalid invoice counter value", e);
        } finally {
            DaoSupport.close(rs, lockPs, null);
            try {
                if (updPs != null) {
                    updPs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                con.setAutoCommit(originalAutoCommit);
            } catch (SQLException ignored) {
            }
            DaoSupport.close(null, null, con);
        }
    }

    public void insertCartItem(CartItem item) throws SQLException {
        String sql = "INSERT INTO cart (INID, Product_Name, Bar_code, qty, Unit_Price, Total_Price) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, item.getInvoiceId());
            ps.setString(2, item.getProductName());
            ps.setString(3, item.getBarcode());
            ps.setString(4, item.getQty());
            ps.setString(5, item.getUnitPrice());
            ps.setString(6, item.getTotalPrice());
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void insertSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (INID, Cid, Customer_Name, Total_Qty, Total_Bill, Status, Balance, "
                + "discount_amount, pay_method, tax_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, sale.getInvoiceId());
            ps.setString(2, sale.getCustomerId());
            ps.setString(3, sale.getCustomerName());
            ps.setString(4, sale.getTotalQty());
            ps.setString(5, sale.getTotalBill());
            ps.setString(6, sale.getStatus());
            ps.setString(7, sale.getBalance());
            ps.setString(8, sale.getDiscountAmount() == null ? "0" : sale.getDiscountAmount());
            ps.setString(9, sale.getPayMethod() == null ? "CASH" : sale.getPayMethod());
            ps.setString(10, sale.getTaxAmount() == null ? "0" : sale.getTaxAmount());
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public List<Sale> findAll() throws SQLException {
        String sql = "SELECT " + SALE_COLS + " FROM sales ORDER BY saleid DESC";
        List<Sale> list = new ArrayList<Sale>();
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
                list.add(mapSale(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<Sale> findByFilter(InvoiceFilter filter) throws SQLException {
        String sql = "SELECT " + SALE_COLS + " FROM sales WHERE CAST(INID AS CHAR) LIKE ? "
                + "AND Customer_Name LIKE ? AND Status LIKE ? ORDER BY saleid DESC";
        List<Sale> list = new ArrayList<Sale>();
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            InvoiceFilter f = filter == null ? new InvoiceFilter() : filter;
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + f.getInvoiceId() + "%");
            ps.setString(2, "%" + f.getCustomerName() + "%");
            ps.setString(3, "%" + f.getStatus() + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSale(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    private Sale mapSale(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setSaleId(rs.getString("saleid"));
        sale.setInvoiceId(rs.getString("INID"));
        sale.setCustomerId(rs.getString("Cid"));
        sale.setCustomerName(rs.getString("Customer_Name"));
        sale.setTotalQty(rs.getString("Total_Qty"));
        sale.setTotalBill(rs.getString("Total_Bill"));
        sale.setStatus(rs.getString("Status"));
        sale.setBalance(rs.getString("Balance"));
        try {
            sale.setDiscountAmount(rs.getString("discount_amount"));
        } catch (SQLException e) {
            sale.setDiscountAmount("0");
        }
        try {
            sale.setPayMethod(rs.getString("pay_method"));
        } catch (SQLException e) {
            sale.setPayMethod("CASH");
        }
        try {
            sale.setTaxAmount(rs.getString("tax_amount"));
        } catch (SQLException e) {
            sale.setTaxAmount("0");
        }
        return sale;
    }

    private Connection requireConnection() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        return con;
    }
}

