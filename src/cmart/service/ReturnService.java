package cmart.service;

import cmart.dao.ProductDao;
import cmart.dao.ReturnDao;
import cmart.model.Product;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.List;

public class ReturnService {

    private final ReturnDao returnDao = new ReturnDao();
    private final ProductDao productDao = new ProductDao();

    public List<Object[]> loadInvoiceLines(String inid) throws SQLException {
        SessionContext.require(Permission.REFUND);
        if (Validation.isBlank(inid)) {
            throw new IllegalArgumentException("Invoice number is required.");
        }
        List<Object[]> lines = returnDao.findCartLinesByInvoice(inid.trim());
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("No cart lines for invoice " + inid);
        }
        return lines;
    }

    public void processReturn(String inid, String barcode, double qty, String reason) throws SQLException {
        SessionContext.require(Permission.REFUND);
        if (qty <= 0) {
            throw new IllegalArgumentException("Return qty must be > 0.");
        }
        Product product = productDao.findByBarcode(barcode);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for barcode.");
        }
        double unit;
        try {
            unit = Double.parseDouble(product.getPrice().trim());
        } catch (NumberFormatException e) {
            unit = 0;
        }
        double refund = unit * qty;
        restock(barcode, qty);

        String by = SessionContext.currentUser() == null ? "" : SessionContext.currentUser().getUsername();
        returnDao.insert(inid.trim(), barcode, product.getName(), String.valueOf(qty),
                String.valueOf(refund), reason == null ? "" : reason.trim(), by);
    }

    private void restock(String barcode, double qty) throws SQLException {
        Product product = productDao.findByBarcode(barcode);
        if (product == null) {
            throw new SQLException("Cannot restock missing product");
        }
        double current = Double.parseDouble(product.getQty().trim());
        product.setQty(String.valueOf(current + qty));
        productDao.update(product);
    }

    public List<String[]> historyForInvoice(String inid) throws SQLException {
        SessionContext.require(Permission.REFUND);
        return returnDao.findByInvoice(inid.trim());
    }
}
