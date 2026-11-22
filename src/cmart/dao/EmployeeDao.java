package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.Employee;
import cmart.model.PartyFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    private static final String COLS =
            "eid, Employee_Name, Tp_Number, main_address, temp_address, bank, city, "
                    + "person_name, contact_person, person_tp, email, online";

    public List<Employee> findAll() throws SQLException {
        return query("SELECT " + COLS + " FROM employee ORDER BY eid");
    }

    public Employee findById(String id) throws SQLException {
        String sql = "SELECT " + COLS + " FROM employee WHERE eid = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<Employee> findByNameLike(String name) throws SQLException {
        String sql = "SELECT " + COLS + " FROM employee WHERE Employee_Name LIKE ? ORDER BY eid";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Employee> list = new ArrayList<Employee>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nullToEmpty(name) + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<Employee> findByFilter(PartyFilter filter) throws SQLException {
        String sql = "SELECT " + COLS + " FROM employee WHERE city LIKE ? AND Tp_Number LIKE ? "
                + "AND contact_person LIKE ? AND person_name LIKE ? ORDER BY eid";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Employee> list = new ArrayList<Employee>();
        try {
            ps = con.prepareStatement(sql);
            PartyFilter f = filter == null ? new PartyFilter() : filter;
            ps.setString(1, "%" + f.getCity() + "%");
            ps.setString(2, "%" + f.getPhone() + "%");
            ps.setString(3, "%" + f.getContactPerson() + "%");
            ps.setString(4, "%" + f.getPersonName() + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void insert(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (Employee_Name, Tp_Number, main_address, temp_address, bank, city, "
                + "person_name, contact_person, person_tp, email, online) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            bindWritable(ps, employee);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET Employee_Name=?, Tp_Number=?, main_address=?, temp_address=?, bank=?, city=?, "
                + "person_name=?, contact_person=?, person_tp=?, email=?, online=? WHERE eid=?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            bindWritable(ps, employee);
            ps.setString(12, employee.getId());
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE eid = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    private List<Employee> query(String sql) throws SQLException {
        List<Employee> list = new ArrayList<Employee>();
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
                list.add(map(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    private void bindWritable(PreparedStatement ps, Employee employee) throws SQLException {
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getPhone());
        ps.setString(3, employee.getMainAddress());
        ps.setString(4, employee.getTempAddress());
        ps.setString(5, employee.getBank());
        ps.setString(6, employee.getCity());
        ps.setString(7, employee.getPersonName());
        ps.setString(8, employee.getContactPerson());
        ps.setString(9, employee.getPersonPhone());
        ps.setString(10, employee.getEmail());
        ps.setString(11, employee.getOnline());
    }

    private Employee map(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getString("eid"));
        e.setName(firstNonNull(rs, "Employee_Name", "employee_name"));
        e.setPhone(firstNonNull(rs, "Tp_Number", "tp_number"));
        e.setMainAddress(safe(rs, "main_address"));
        e.setTempAddress(safe(rs, "temp_address"));
        e.setBank(safe(rs, "bank"));
        e.setCity(safe(rs, "city"));
        e.setPersonName(safe(rs, "person_name"));
        e.setContactPerson(safe(rs, "contact_person"));
        e.setPersonPhone(safe(rs, "person_tp"));
        e.setEmail(safe(rs, "email"));
        e.setOnline(safe(rs, "online"));
        return e;
    }

    private static String firstNonNull(ResultSet rs, String primary, String fallback) throws SQLException {
        String value = safe(rs, primary);
        if (!value.isEmpty()) {
            return value;
        }
        return safe(rs, fallback);
    }

    private static String safe(ResultSet rs, String column) throws SQLException {
        try {
            String value = rs.getString(column);
            return value == null ? "" : value;
        } catch (SQLException e) {
            return "";
        }
    }

    private Connection requireConnection() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            throw new SQLException("No database connection");
        }
        return con;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
