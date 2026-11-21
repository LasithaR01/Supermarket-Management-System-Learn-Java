package cmart.model;

public class Sale {

    private String saleId;
    private String invoiceId;
    private String customerId;
    private String customerName;
    private String totalQty;
    private String totalBill;
    private String status;
    private String balance;

    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getTotalQty() { return totalQty; }
    public void setTotalQty(String totalQty) { this.totalQty = totalQty; }
    public String getTotalBill() { return totalBill; }
    public void setTotalBill(String totalBill) { this.totalBill = totalBill; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }
}
