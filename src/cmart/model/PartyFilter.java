package cmart.model;

/**
 * Shared search criteria for customer / supplier / employee advanced filters.
 */
public class PartyFilter {

    private String city = "";
    private String phone = "";
    private String contactPerson = "";
    private String personName = "";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = nullToEmpty(city);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = nullToEmpty(phone);
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = nullToEmpty(contactPerson);
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = nullToEmpty(personName);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
