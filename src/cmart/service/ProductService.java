package cmart.service;

import cmart.dao.ProductDao;
import cmart.model.Product;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductDao dao = new ProductDao();

    public List<Product> listAll() throws SQLException {
        return dao.findAll();
    }

    public Product getById(String id) throws SQLException {
        return dao.findById(id);
    }

    public Product getByName(String name) throws SQLException {
        return dao.findByName(name);
    }

    public Product getByBarcode(String barcode) throws SQLException {
        return dao.findByBarcode(barcode);
    }

    public List<Product> searchByName(String name) throws SQLException {
        return dao.findByNameLike(name);
    }

    public List<Product> listLowStock(double threshold) throws SQLException {
        return dao.findLowStock(threshold);
    }

    public void save(Product product) throws SQLException {
        SessionContext.require(Permission.PRODUCT);
        validate(product, false);
        dao.insert(product);
    }

    public void update(Product product) throws SQLException {
        SessionContext.require(Permission.PRODUCT);
        if (Validation.isBlank(product.getId())) {
            throw new IllegalArgumentException("Select a product id to update.");
        }
        validate(product, true);
        dao.update(product);
    }

    public void delete(String id) throws SQLException {
        SessionContext.require(Permission.PRODUCT);
        if (Validation.isBlank(id)) {
            throw new IllegalArgumentException("Select a product id to delete.");
        }
        dao.delete(id.trim());
        new AuditService().record(AuditService.PRODUCT_DELETE, "", "Deleted product id " + id.trim());
    }

    public boolean deductStockByBarcode(String barcode, double qty) throws SQLException {
        return dao.deductStockByBarcode(barcode, qty);
    }

    private void validate(Product product, boolean requireId) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required.");
        }
        if (requireId && Validation.isBlank(product.getId())) {
            throw new IllegalArgumentException("Product id is required.");
        }
        if (Validation.isBlank(product.getName())) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (Validation.isBlank(product.getBarcode())) {
            throw new IllegalArgumentException("Barcode is required.");
        }
        if (!Validation.isPositiveNumber(product.getPrice()) && !isZeroOrPositive(product.getPrice())) {
            throw new IllegalArgumentException("Price must be a valid number >= 0.");
        }
        if (!isZeroOrPositive(product.getQty())) {
            throw new IllegalArgumentException("Qty must be a valid number >= 0.");
        }
        if (Validation.isBlank(product.getSupplierId())) {
            throw new IllegalArgumentException("Supplier id is required.");
        }
    }

    private static boolean isZeroOrPositive(String value) {
        if (Validation.isBlank(value)) {
            return false;
        }
        try {
            return Double.parseDouble(value.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
