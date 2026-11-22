package cmart.service;

import cmart.config.SettingKeys;
import cmart.dao.DashboardDao;
import cmart.model.Product;
import cmart.security.Permission;
import cmart.security.SessionContext;
import java.sql.SQLException;
import java.util.List;

public class DashboardService {

    private final DashboardDao dashboardDao = new DashboardDao();
    private final ProductService productService = new ProductService();
    private final SettingService settingService = new SettingService();

    public double recentSalesTotal() throws SQLException {
        SessionContext.require(Permission.DASHBOARD);
        return dashboardDao.todaySalesTotal();
    }

    public int duesCount() throws SQLException {
        SessionContext.require(Permission.DASHBOARD);
        return dashboardDao.unpaidCount();
    }

    public List<String[]> topProducts(int limit) throws SQLException {
        SessionContext.require(Permission.DASHBOARD);
        return dashboardDao.topProducts(limit);
    }

    public List<Product> lowStock() throws SQLException {
        SessionContext.require(Permission.DASHBOARD);
        double threshold = settingService.getDouble(SettingKeys.STOCK_LOW_THRESHOLD, 5);
        return productService.listLowStock(threshold);
    }
}
