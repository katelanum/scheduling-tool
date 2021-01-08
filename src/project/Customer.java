package project;

import java.sql.*;

// add, update, and delete customer records
// fields are read in from customer database, appointment objects have foreign key to the customerId
public class Customer {
    // customerId will be in string format because despite being a number, it will not be getting manipulated like one,
    // formatting it as a string allows for better control of what is being done with the customerId
    int customerId;
    String customerName;
    String customerStreetAddress;
    // postal code is a string under the same logic as the ids are, it will be getting manipulated as a string, it adds
    // a layer of system robustness at the cost of space
    String customerCountry;
    String customerStateProv;
    String customerPostal;
    String customerPhone;
    Date creationDate;
    String creator;
    Date lastchange;
    String whoModified;
    int divisionId;
}
