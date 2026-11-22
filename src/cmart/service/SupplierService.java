package cmart.service;

import cmart.dao.SupplierDao;
import cmart.model.PartyFilter;
import cmart.model.Supplier;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.List;

public class SupplierService {

    private final SupplierDao supplierDao = new SupplierDao();

    public List<Supplier> listAll() throws SQLException {
        return supplierDao.findAll();
    }

    public Supplier getById(String id) throws SQLException {
        return supplierDao.findById(id);
    }

    public List<Supplier> searchByName(String name) throws SQLException {
        return supplierDao.findByNameLike(name);
    }

    public List<Supplier> search(PartyFilter filter) throws SQLException {
        return supplierDao.findByFilter(filter);
    }

    public void save(Supplier supplier) throws SQLException {
        SessionContext.require(Permission.SUPPLIER);
        validate(supplier);
        supplierDao.insert(supplier);
    }

    public void update(Supplier supplier) throws SQLException {
        SessionContext.require(Permission.SUPPLIER);
        if (Validation.isBlank(supplier.getId())) {
            throw new IllegalArgumentException("Select a supplier id to update.");
        }
        validate(supplier);
        supplierDao.update(supplier);
    }

    public void delete(String id) throws SQLException {
        SessionContext.require(Permission.SUPPLIER);
        if (Validation.isBlank(id)) {
            throw new IllegalArgumentException("Select a supplier id to delete.");
        }
        supplierDao.delete(id.trim());
    }

    private void validate(Supplier supplier) {
        if (supplier == null || Validation.isBlank(supplier.getName())) {
            throw new IllegalArgumentException("Supplier name is required.");
        }
        if (Validation.isBlank(supplier.getPhone())) {
            throw new IllegalArgumentException("Phone number is required.");
        }
    }
}
