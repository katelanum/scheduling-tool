package project;

import java.sql.*;

// add, update, and delete customer records
// fields are read in from customer database, appointment objects have foreign key to the customerId
public class Customer {
    // customerId will be in string format because despite being a number, it will not be getting manipulated like one,
    // formatting it as a string allows for better control of what is being done with the customerId
    private int customerId;
    private String customerName;
    private String customerStreetAddress;
    // postal code is a string under the same logic as the ids are, it will be getting manipulated as a string, it adds
    // a layer of system robustness at the cost of space
    private String customerCountry;
    private String customerStateProv;
    private String customerPostal;
    private String customerPhone;
    private Date creationDate;
    private String creator;
    private Date lastChange;
    private String whoModified;
    private int divisionId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerStreetAddress() {
        return customerStreetAddress;
    }

    public void setCustomerStreetAddress(String customerStreetAddress) {
        this.customerStreetAddress = customerStreetAddress;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerStateProv() {
        return customerStateProv;
    }

    public void setCustomerStateProv(String customerStateProv) {
        this.customerStateProv = customerStateProv;
    }

    public String getCustomerPostal() {
        return customerPostal;
    }

    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public String getWhoModified() {
        return whoModified;
    }

    public void setWhoModified(String whoModified) {
        this.whoModified = whoModified;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
