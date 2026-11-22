package cmart.dao;

import cmart.db.ConnectionFactory;
import cmart.model.PartyFilter;
import cmart.model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDao {

    private static final String COLS =
            "sid, supplier_Name, Tp_Number, billing_address, shipping_address, bank, city, "
                    + "person_name, contact_person, person_tp, email, online";

    public List<Supplier> findAll() throws SQLException {
        return query("SELECT " + COLS + " FROM supplier ORDER BY sid");
    }

    public Supplier findById(String id) throws SQLException {
        String sql = "SELECT " + COLS + " FROM supplier WHERE sid = ?";
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

    public List<Supplier> findByNameLike(String name) throws SQLException {
        String sql = "SELECT " + COLS + " FROM supplier WHERE supplier_Name LIKE ? ORDER BY sid";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Supplier> list = new ArrayList<Supplier>();
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

    public List<Supplier> findByFilter(PartyFilter filter) throws SQLException {
        String sql = "SELECT " + COLS + " FROM supplier WHERE city LIKE ? AND Tp_Number LIKE ? "
                + "AND contact_person LIKE ? AND person_name LIKE ? ORDER BY sid";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Supplier> list = new ArrayList<Supplier>();
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

    public void insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier (supplier_Name, Tp_Number, billing_address, shipping_address, bank, city, "
                + "person_name, contact_person, person_tp, email, online) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            bindWritable(ps, supplier);
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET supplier_Name=?, Tp_Number=?, billing_address=?, shipping_address=?, bank=?, city=?, "
                + "person_name=?, contact_person=?, person_tp=?, email=?, online=? WHERE sid=?";
        Connection con = requireConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            bindWritable(ps, supplier);
            ps.setString(12, supplier.getId());
            ps.executeUpdate();
        } finally {
            DaoSupport.close(null, ps, con);
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM supplier WHERE sid = ?";
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

    private List<Supplier> query(String sql) throws SQLException {
        List<Supplier> list = new ArrayList<Supplier>();
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

    private void bindWritable(PreparedStatement ps, Supplier supplier) throws SQLException {
        ps.setString(1, supplier.getName());
        ps.setString(2, supplier.getPhone());
        ps.setString(3, supplier.getBillingAddress());
        ps.setString(4, supplier.getShippingAddress());
        ps.setString(5, supplier.getBank());
        ps.setString(6, supplier.getCity());
        ps.setString(7, supplier.getPersonName());
        ps.setString(8, supplier.getContactPerson());
        ps.setString(9, supplier.getPersonPhone());
        ps.setString(10, supplier.getEmail());
        ps.setString(11, supplier.getOnline());
    }

    private Supplier map(ResultSet rs) throws SQLException {
        Supplier s = new Supplier();
        s.setId(rs.getString("sid"));
        s.setName(rs.getString("supplier_Name"));
        s.setPhone(rs.getString("Tp_Number"));
        s.setBillingAddress(safe(rs, "billing_address"));
        s.setShippingAddress(safe(rs, "shipping_address"));
        s.setBank(safe(rs, "bank"));
        s.setCity(safe(rs, "city"));
        s.setPersonName(safe(rs, "person_name"));
        s.setContactPerson(safe(rs, "contact_person"));
        s.setPersonPhone(safe(rs, "person_tp"));
        s.setEmail(safe(rs, "email"));
        s.setOnline(safe(rs, "online"));
        return s;
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
