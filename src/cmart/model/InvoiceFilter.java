package cmart.model;

/**
 * Invoice list filter criteria (invoice no / customer / payment status).
 */
public class InvoiceFilter {

    private String invoiceId = "";
    private String customerName = "";
    private String status = "";

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = nullToEmpty(invoiceId);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = nullToEmpty(customerName);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = nullToEmpty(status);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
