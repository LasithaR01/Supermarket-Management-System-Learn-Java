package cmart.service;

import cmart.db.ConnectionFactory;
import cmart.dao.ProductDao;
import cmart.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sale rules that should not live inside Swing panels.
 */
public class SaleService {

    private final ProductDao productDao = new ProductDao();

    /**
     * Preview of the next invoice number (does not reserve it).
     */
    public String peekNextInvoiceNumber() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
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
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { if (con != null) con.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * Atomically reserves the next invoice number so two tills cannot get the same INID.
     * Uses row lock ({@code SELECT ... FOR UPDATE}) then increments {@code extra.val}.
     */
    public String allocateInvoiceNumber() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
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
            try { con.rollback(); } catch (SQLException ignored) {}
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            throw new SQLException("Invalid invoice counter value", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (lockPs != null) lockPs.close(); } catch (SQLException ignored) {}
            try { if (updPs != null) updPs.close(); } catch (SQLException ignored) {}
            try {
                con.setAutoCommit(originalAutoCommit);
            } catch (SQLException ignored) {}
            try { if (con != null) con.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * @return error message, or null if the line qty is allowed
     */
    public String validateSaleQty(String barcode, double requestQty, double alreadyInCart) throws SQLException {
        if (requestQty <= 0) {
            return "Quantity must be greater than zero.";
        }
        if (barcode == null || barcode.trim().isEmpty() || "-".equals(barcode.trim())) {
            return "Select a product first.";
        }
        Product product = productDao.findByBarcode(barcode.trim());
        if (product == null) {
            return "Product not found for barcode: " + barcode;
        }
        double stock;
        try {
            stock = Double.parseDouble(product.getQty().trim());
        } catch (NumberFormatException e) {
            return "Product stock value is invalid.";
        }
        double needed = alreadyInCart + requestQty;
        if (needed > stock) {
            return "Not enough stock. Available: " + trimQty(stock)
                    + ", already in cart: " + trimQty(alreadyInCart)
                    + ", requested: " + trimQty(requestQty) + ".";
        }
        return null;
    }

    /**
     * Deduct stock for one sold line. Returns an error message, or null on success.
     */
    public String deductStock(String barcode, double qty) throws SQLException {
        if (qty <= 0) {
            return "Invalid quantity for stock deduct.";
        }
        boolean ok = productDao.deductStockByBarcode(barcode, qty);
        if (!ok) {
            return "Could not deduct stock for barcode " + barcode
                    + " (not enough Qty or product missing).";
        }
        return null;
    }

    public static String paymentStatus(double billTotal, double paid) {
        if (paid <= 0.0) {
            return "UnPaid";
        }
        if (billTotal > paid) {
            return "Partial";
        }
        return "Paid";
    }

    private static String trimQty(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
