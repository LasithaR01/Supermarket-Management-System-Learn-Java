package cmart.service;

import cmart.dao.EmployeeDao;
import cmart.model.Employee;
import cmart.model.PartyFilter;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private final EmployeeDao employeeDao = new EmployeeDao();

    public List<Employee> listAll() throws SQLException {
        return employeeDao.findAll();
    }

    public Employee getById(String id) throws SQLException {
        return employeeDao.findById(id);
    }

    public List<Employee> searchByName(String name) throws SQLException {
        return employeeDao.findByNameLike(name);
    }

    public List<Employee> search(PartyFilter filter) throws SQLException {
        return employeeDao.findByFilter(filter);
    }

    public void save(Employee employee) throws SQLException {
        SessionContext.require(Permission.EMPLOYEE);
        validate(employee);
        employeeDao.insert(employee);
    }

    public void update(Employee employee) throws SQLException {
        SessionContext.require(Permission.EMPLOYEE);
        if (Validation.isBlank(employee.getId())) {
            throw new IllegalArgumentException("Select an employee id to update.");
        }
        validate(employee);
        employeeDao.update(employee);
    }

    public void delete(String id) throws SQLException {
        SessionContext.require(Permission.EMPLOYEE);
        if (Validation.isBlank(id)) {
            throw new IllegalArgumentException("Select an employee id to delete.");
        }
        employeeDao.delete(id.trim());
    }

    private void validate(Employee employee) {
        if (employee == null || Validation.isBlank(employee.getName())) {
            throw new IllegalArgumentException("Employee name is required.");
        }
        if (Validation.isBlank(employee.getPhone())) {
            throw new IllegalArgumentException("Phone number is required.");
        }
    }
}
