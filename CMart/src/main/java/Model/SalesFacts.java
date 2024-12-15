package Model;

public class SalesFacts {
    private int timeId;
    private int productId;
    private int customerId;
    private int deptId;
    private int promoId;
    private int unitSales;

    public SalesFacts() {
    }

    public SalesFacts(int timeId, int productId, int customerId, int deptId, int promoId, int unitSales) {
        this.timeId = timeId;
        this.productId = productId;
        this.customerId = customerId;
        this.deptId = deptId;
        this.promoId = promoId;
        this.unitSales = unitSales;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public int getUnitSales() {
        return unitSales;
    }

    public void setUnitSales(int unitSales) {
        this.unitSales = unitSales;
    }
}
