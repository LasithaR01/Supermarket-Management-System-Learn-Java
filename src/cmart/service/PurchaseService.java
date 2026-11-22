package cmart.service;

import cmart.dao.PurchaseDao;
import cmart.model.CartItem;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.List;

public class PurchaseService {

    private final PurchaseDao purchaseDao = new PurchaseDao();

    public int createOrder(String supplierId, String supplierName, List<CartItem> lines) throws SQLException {
        SessionContext.require(Permission.PURCHASE);
        if (Validation.isBlank(supplierName)) {
            throw new IllegalArgumentException("Supplier is required.");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Add at least one product line.");
        }
        String by = SessionContext.currentUser() == null ? "" : SessionContext.currentUser().getUsername();
        int poId = purchaseDao.insertOrder(supplierId == null ? "" : supplierId, supplierName.trim(), by);
        for (CartItem line : lines) {
            purchaseDao.insertLine(poId, line.getBarcode(), line.getProductName(), line.getQty(),
                    line.getUnitPrice() == null ? "0" : line.getUnitPrice());
        }
        return poId;
    }

    public List<Object[]> listOpen() throws SQLException {
        SessionContext.require(Permission.PURCHASE);
        return purchaseDao.findOpenOrders();
    }

    public List<Object[]> listAll() throws SQLException {
        SessionContext.require(Permission.PURCHASE);
        return purchaseDao.findAllOrders();
    }

    public void receive(int poId) throws SQLException {
        SessionContext.require(Permission.PURCHASE);
        List<Object[]> lines = purchaseDao.findLines(poId);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Purchase order has no lines.");
        }
        for (Object[] line : lines) {
            double qty = Double.parseDouble(String.valueOf(line[2]));
            purchaseDao.increaseStockByBarcode(String.valueOf(line[0]), qty);
        }
        purchaseDao.markReceived(poId);
    }
}
