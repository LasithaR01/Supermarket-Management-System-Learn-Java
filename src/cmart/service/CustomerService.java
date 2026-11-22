package cmart.service;

import cmart.dao.CustomerDao;
import cmart.model.Customer;
import cmart.model.PartyFilter;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private final CustomerDao customerDao = new CustomerDao();

    public List<String> listNames() throws SQLException {
        return customerDao.findAllNames();
    }

    public List<Customer> listAll() throws SQLException {
        return customerDao.findAll();
    }

    public Customer getById(String id) throws SQLException {
        return customerDao.findById(id);
    }

    public Customer getByName(String name) throws SQLException {
        return customerDao.findByName(name);
    }

    public List<Customer> searchByName(String name) throws SQLException {
        return customerDao.findByNameLike(name);
    }

    public List<Customer> search(PartyFilter filter) throws SQLException {
        return customerDao.findByFilter(filter);
    }

    public void save(Customer customer) throws SQLException {
        SessionContext.require(Permission.CUSTOMER);
        validate(customer);
        customerDao.insert(customer);
    }

    public void update(Customer customer) throws SQLException {
        SessionContext.require(Permission.CUSTOMER);
        if (Validation.isBlank(customer.getId())) {
            throw new IllegalArgumentException("Select a customer id to update.");
        }
        validate(customer);
        customerDao.update(customer);
    }

    public void delete(String id) throws SQLException {
        SessionContext.require(Permission.CUSTOMER);
        if (Validation.isBlank(id)) {
            throw new IllegalArgumentException("Select a customer id to delete.");
        }
        customerDao.delete(id.trim());
    }

    private void validate(Customer customer) {
        if (customer == null || Validation.isBlank(customer.getName())) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (Validation.isBlank(customer.getPhone())) {
            throw new IllegalArgumentException("Phone number is required.");
        }
    }
}
