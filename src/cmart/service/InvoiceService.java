package cmart.service;

import cmart.dao.SaleDao;
import cmart.model.InvoiceFilter;
import cmart.model.Sale;
import cmart.security.Permission;
import cmart.security.SessionContext;
import java.sql.SQLException;
import java.util.List;

/**
 * Invoice list / search rules. UI must not contain SQL.
 */
public class InvoiceService {

    private final SaleDao saleDao = new SaleDao();

    public List<Sale> listAll() throws SQLException {
        SessionContext.require(Permission.INVOICE);
        return saleDao.findAll();
    }

    public List<Sale> search(InvoiceFilter filter) throws SQLException {
        SessionContext.require(Permission.INVOICE);
        return saleDao.findByFilter(filter);
    }
}
