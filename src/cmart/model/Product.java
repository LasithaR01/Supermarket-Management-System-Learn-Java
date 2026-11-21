package cmart.model;

public class Product {

    private String id;
    private String name;
    private String barcode;
    private String price;
    private String qty;
    private String supplierId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getQty() { return qty; }
    public void setQty(String qty) { this.qty = qty; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
}
