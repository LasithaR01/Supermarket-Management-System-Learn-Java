package cmart.model;

public class Supplier {

    private String id;
    private String name;
    private String phone;
    private String billingAddress;
    private String shippingAddress;
    private String bank;
    private String city;
    private String personName;
    private String contactPerson;
    private String personPhone;
    private String email;
    private String online;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getBank() { return bank; }
    public void setBank(String bank) { this.bank = bank; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getPersonPhone() { return personPhone; }
    public void setPersonPhone(String personPhone) { this.personPhone = personPhone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOnline() { return online; }
    public void setOnline(String online) { this.online = online; }
}
