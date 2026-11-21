package cmart.service;

import cmart.dao.ProductDao;
import cmart.model.Product;
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

    public List<Product> searchByName(String name) throws SQLException {
        return dao.findByNameLike(name);
    }

    public void save(Product product) throws SQLException {
        dao.insert(product);
    }

    public void update(Product product) throws SQLException {
        dao.update(product);
    }

    public void delete(String id) throws SQLException {
        dao.delete(id);
    }
}
