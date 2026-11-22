package cmart.service;

import cmart.config.SettingKeys;
import cmart.dao.HeldSaleDao;
import cmart.dao.ProductDao;
import cmart.dao.SaleDao;
import cmart.model.CartItem;
import cmart.model.HeldSale;
import cmart.model.Product;
import cmart.model.Sale;
import cmart.model.SaleCheckoutRequest;
import cmart.security.Permission;
import cmart.security.SessionContext;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sale business rules. UI must not contain SQL.
 */
public class SaleService {

    private final ProductDao productDao = new ProductDao();
    private final SaleDao saleDao = new SaleDao();
    private final HeldSaleDao heldSaleDao = new HeldSaleDao();
    private final SettingService settingService = new SettingService();

    public String peekNextInvoiceNumber() throws SQLException {
        return saleDao.peekNextInvoiceNumber();
    }

    public String allocateInvoiceNumber() throws SQLException {
        return saleDao.allocateInvoiceNumber();
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
        boolean allowNegative = settingService.getBoolean(SettingKeys.ALLOW_NEGATIVE_STOCK);
        if (!allowNegative && needed > stock) {
            return "Not enough stock. Available: " + trimQty(stock)
                    + ", already in cart: " + trimQty(alreadyInCart)
                    + ", requested: " + trimQty(requestQty) + ".";
        }
        return null;
    }

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

    /**
     * Full checkout: validate → allocate INID → deduct stock → persist cart + sale.
     *
     * @return allocated invoice id
     */
    public String completeSale(SaleCheckoutRequest request) throws SQLException {
        SessionContext.require(Permission.SALE);
        if (request == null || request.getLines() == null || request.getLines().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Select a customer.");
        }

        validateCartStock(request.getLines());

        String invoiceId = saleDao.allocateInvoiceNumber();

        for (CartItem line : request.getLines()) {
            double qty = Double.parseDouble(line.getQty().trim());
            String stockError = deductStock(line.getBarcode(), qty);
            if (stockError != null) {
                throw new SQLException(stockError);
            }
        }

        for (CartItem line : request.getLines()) {
            CartItem toSave = copyWithInvoice(line, invoiceId);
            saleDao.insertCartItem(toSave);
        }

        if (request.getDiscountAmount() < 0) {
            throw new IllegalArgumentException("Discount cannot be negative.");
        }
        if (request.getDiscountAmount() > 0) {
            cmart.security.ActionAcl.requireDiscountAllowed();
        }
        if (request.getDiscountAmount() > request.getBillTotal() + request.getDiscountAmount()) {
            throw new IllegalArgumentException("Discount cannot exceed bill subtotal.");
        }

        double balance = request.getPaidAmount() - request.getBillTotal();
        Sale sale = new Sale();
        sale.setInvoiceId(invoiceId);
        sale.setCustomerId(request.getCustomerId());
        sale.setCustomerName(request.getCustomerName());
        sale.setTotalQty(trimQty(request.getTotalQty()));
        sale.setTotalBill(String.valueOf(request.getBillTotal()));
        sale.setStatus(paymentStatus(request.getBillTotal(), request.getPaidAmount()));
        sale.setBalance(String.valueOf(balance));
        sale.setDiscountAmount(String.valueOf(request.getDiscountAmount()));
        sale.setPayMethod(request.getPayMethod().name());
        sale.setTaxAmount(String.valueOf(request.getTaxAmount()));
        saleDao.insertSale(sale);

        if (request.getDiscountAmount() > 0) {
            String by = SessionContext.currentUser() == null ? "" : SessionContext.currentUser().getUsername();
            new AuditService().record(AuditService.SALE_DISCOUNT, by,
                    "Invoice " + invoiceId + " discount " + request.getDiscountAmount());
        }

        return invoiceId;
    }

    public String holdSale(HeldSale held) throws SQLException {
        SessionContext.require(Permission.SALE);
        if (held == null || held.getLines() == null || held.getLines().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty — nothing to hold.");
        }
        String code = "H" + System.currentTimeMillis();
        held.setHoldCode(code);
        if (SessionContext.currentUser() != null) {
            held.setCreatedBy(SessionContext.currentUser().getUsername());
        }
        heldSaleDao.insert(held);
        return code;
    }

    public List<HeldSale> listHeldSales() throws SQLException {
        SessionContext.require(Permission.SALE);
        return heldSaleDao.findAll();
    }

    public HeldSale recallHeldSale(String holdCode) throws SQLException {
        SessionContext.require(Permission.SALE);
        HeldSale held = heldSaleDao.findByCode(holdCode);
        if (held == null) {
            throw new IllegalArgumentException("Hold code not found: " + holdCode);
        }
        heldSaleDao.deleteByCode(holdCode);
        return held;
    }

    private void validateCartStock(List<CartItem> lines) throws SQLException {
        Map<String, Double> priorByBarcode = new HashMap<String, Double>();
        for (CartItem line : lines) {
            String barcode = line.getBarcode();
            double qty = Double.parseDouble(line.getQty().trim());
            double prior = priorByBarcode.containsKey(barcode) ? priorByBarcode.get(barcode) : 0.0;
            String error = validateSaleQty(barcode, qty, prior);
            if (error != null) {
                throw new IllegalArgumentException(error);
            }
            priorByBarcode.put(barcode, prior + qty);
        }
    }

    private static CartItem copyWithInvoice(CartItem source, String invoiceId) {
        CartItem item = new CartItem();
        item.setInvoiceId(invoiceId);
        item.setProductName(source.getProductName());
        item.setBarcode(source.getBarcode());
        item.setQty(source.getQty());
        item.setUnitPrice(source.getUnitPrice());
        item.setTotalPrice(source.getTotalPrice());
        return item;
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
