package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.Customer;
import cmart.model.PartyFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    private static final String COLS =
            "cid, customer_name, Tp_Number, billing_address, shipping_address, bank, city, "
                    + "person_name, contact_person, person_tp, email, online";

    public List<String> findAllNames() throws SQLException {
        List<String> names = new ArrayList<String>();
        String sql = "SELECT customer_name FROM customer ORDER BY customer_name";
        Connection con = ConnectionFactory.getConnection();
        if (con == null) {
            return names;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("customer_name"));
            }
            return names;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<Customer> findAll() throws SQLException {
        return query("SELECT " + COLS + " FROM customer ORDER BY cid");
    }

    public Customer findById(String id) throws SQLException {
        String sql = "SELECT " + COLS + " FROM customer WHERE cid = ?";
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

    public Customer findByName(String name) throws SQLException {
        String sql = "SELECT " + COLS + " FROM customer WHERE customer_name = ?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public List<Customer> findByNameLike(String name) throws SQLException {
        String sql = "SELECT " + COLS + " FROM customer WHERE customer_name LIKE ? ORDER BY cid";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Customer> list = new ArrayList<Customer>();
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

    public List<Customer> findByFilter(PartyFilter filter) throws SQLException {
        String sql = "SELECT " + COLS + " FROM customer WHERE city LIKE ? AND Tp_Number LIKE ? "
                + "AND contact_person LIKE ? AND person_name LIKE ? ORDER BY cid";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Customer> list = new ArrayList<Customer>();
        try {
            ps = con.prepareStatement(sql);
            bindFilter(ps, filter);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } finally {
            DaoSupport.close(rs, ps, con);
        }
    }

    public void insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (customer_name, Tp_Number, billing_address, shipping_address, bank, city, "
                + "person_name, contact_person, person_tp, email, online) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            bindWritable(ps, customer);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET customer_name=?, Tp_Number=?, billing_address=?, shipping_address=?, bank=?, city=?, "
                + "person_name=?, contact_person=?, person_tp=?, email=?, online=? WHERE cid=?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            bindWritable(ps, customer);
            ps.setString(12, customer.getId());
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE cid = ?";
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

    private List<Customer> query(String sql) throws SQLException {
        List<Customer> list = new ArrayList<Customer>();
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

    private void bindFilter(PreparedStatement ps, PartyFilter filter) throws SQLException {
        PartyFilter f = filter == null ? new PartyFilter() : filter;
        ps.setString(1, "%" + f.getCity() + "%");
        ps.setString(2, "%" + f.getPhone() + "%");
        ps.setString(3, "%" + f.getContactPerson() + "%");
        ps.setString(4, "%" + f.getPersonName() + "%");
    }

    private void bindWritable(PreparedStatement ps, Customer customer) throws SQLException {
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getPhone());
        ps.setString(3, customer.getBillingAddress());
        ps.setString(4, customer.getShippingAddress());
        ps.setString(5, customer.getBank());
        ps.setString(6, customer.getCity());
        ps.setString(7, customer.getPersonName());
        ps.setString(8, customer.getContactPerson());
        ps.setString(9, customer.getPersonPhone());
        ps.setString(10, customer.getEmail());
        ps.setString(11, customer.getOnline());
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getString("cid"));
        c.setName(rs.getString("customer_name"));
        c.setPhone(rs.getString("Tp_Number"));
        c.setBillingAddress(safe(rs, "billing_address"));
        c.setShippingAddress(safe(rs, "shipping_address"));
        c.setBank(safe(rs, "bank"));
        c.setCity(safe(rs, "city"));
        c.setPersonName(safe(rs, "person_name"));
        c.setContactPerson(safe(rs, "contact_person"));
        c.setPersonPhone(safe(rs, "person_tp"));
        c.setEmail(safe(rs, "email"));
        c.setOnline(safe(rs, "online"));
        return c;
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
