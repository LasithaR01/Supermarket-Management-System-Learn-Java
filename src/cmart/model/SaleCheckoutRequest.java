package cmart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable checkout payload built by the UI and executed by SaleService.
 */
public final class SaleCheckoutRequest {

    private final String customerId;
    private final String customerName;
    private final double paidAmount;
    private final double billTotal;
    private final double totalQty;
    private final double discountAmount;
    private final double taxAmount;
    private final PayMethod payMethod;
    private final List<CartItem> lines;

    public SaleCheckoutRequest(
            String customerId,
            String customerName,
            double paidAmount,
            double billTotal,
            double totalQty,
            double discountAmount,
            double taxAmount,
            PayMethod payMethod,
            List<CartItem> lines) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.paidAmount = paidAmount;
        this.billTotal = billTotal;
        this.totalQty = totalQty;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.payMethod = payMethod == null ? PayMethod.CASH : payMethod;
        this.lines = Collections.unmodifiableList(new ArrayList<CartItem>(lines));
    }

    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public double getPaidAmount() { return paidAmount; }
    public double getBillTotal() { return billTotal; }
    public double getTotalQty() { return totalQty; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTaxAmount() { return taxAmount; }
    public PayMethod getPayMethod() { return payMethod; }
    public List<CartItem> getLines() { return lines; }
}
