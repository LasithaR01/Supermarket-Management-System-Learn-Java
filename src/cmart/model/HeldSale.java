package cmart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parked cart that can be recalled later (hold bill).
 */
public class HeldSale {

    private int id;
    private String holdCode;
    private String customerId;
    private String customerName;
    private List<CartItem> lines = new ArrayList<CartItem>();
    private String createdBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoldCode() {
        return holdCode;
    }

    public void setHoldCode(String holdCode) {
        this.holdCode = holdCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CartItem> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void setLines(List<CartItem> lines) {
        this.lines = lines == null ? new ArrayList<CartItem>() : new ArrayList<CartItem>(lines);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
