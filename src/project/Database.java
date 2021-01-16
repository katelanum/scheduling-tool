package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;

public class Database {
    //connection string
    // server name: wgudb.ucertify.com
    // port: 3306
    // databse name: WJ03Pjl
    // username: U03Pjl
    // password: 53688043361

    private static Connection connection = null;

    public static void initializeDB() throws SQLException {
        final String JDBCDriver = "com.mysql.jdbc.Driver";
        final String databaseURL = "jdbc:mysql://wgudb.ucertify.com/WJ03Pjl";
        final String databaseUsername = "U03Pjl";
        final String databasePassword = "53688043361";
        try {
            Class.forName(JDBCDriver);
            connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        } catch (ClassNotFoundException | SQLException dbEx) {
            dbEx.printStackTrace();
        }
        Statement query = connection.createStatement();
        query.execute("CREATE TABLE IF NOT EXISTS Contacts (" +
                "Contact_ID INT(10) NOT NULL AUTO_INCREMENT, " +
                "Contact_Name VARCHAR(50), " +
                "Email VARCHAR(50), " +
                "PRIMARY KEY (Contact_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS Users (" +
                "User_ID INT(10) NOT NULL AUTO_INCREMENT, " +
                "User_Name VARCHAR(50) UNIQUE, " +
                "Password TEXT, " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "PRIMARY KEY (User_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS Countries (" +
                "Country_ID INT(10) NOT NULL, " +
                "Country VARCHAR(50), " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "PRIMARY KEY (Country_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS FirstLevelDivisions (" +
                "Division_ID INT(10) NOT NULL, " +
                "Division VARCHAR(50), " +
                "Country_ID INT(10), " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "PRIMARY KEY (Division_ID), " +
                "CONSTRAINT fk_firstleveldivisions_country FOREIGN KEY (Country_ID) REFERENCES Countries (Country_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS Customers (" +
                "Customer_ID INT(10) NOT NULL AUTO_INCREMENT, " +
                "Customer_Name VARCHAR(50), " +
                "Address VARCHAR(100), " +
                "Postal_Code VARCHAR(50), " +
                "Phone VARCHAR(50), " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "Division_ID INT(10), " +
                "PRIMARY KEY (Customer_ID), " +
                "CONSTRAINT fk_customers_division FOREIGN KEY (Division_ID) REFERENCES FirstLevelDivisions (Division_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS Appointments (" +
                "Appointment_ID INT(10) NOT NULL AUTO_INCREMENT, " +
                "Title VARCHAR(50), " +
                "Description VARCHAR(50), " +
                "Location VARCHAR(50), " +
                "Type VARCHAR(50), " +
                "Start DATETIME, " +
                "End DATETIME, " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "Customer_ID INT(10)  NOT NULL, " +
                "User_ID INT(10) NOT NULL, " +
                "Contact_ID INT(10) NOT NULL, " +
                "PRIMARY KEY (Appointment_ID), " +
                "CONSTRAINT fk_appointments_customer FOREIGN KEY (Customer_ID) REFERENCES Customers(Customer_ID), " +
                "CONSTRAINT fk_appointments_user FOREIGN KEY (User_ID) REFERENCES Users(User_ID), " +
                "CONSTRAINT fk_appointments_contact FOREIGN KEY (Contact_ID) REFERENCES Contacts(Contact_ID))");
        ResultSet results = query.executeQuery("SELECT COUNT(User_ID) FROM Users");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO Users VALUES (1, 'test', 'test', NOW(), 'script', NOW(), 'script')");
            query.execute("INSERT INTO Users VALUES (2, 'admin', 'admin', NOW(), 'script', NOW(), 'script')");
        }
        results = query.executeQuery("SELECT COUNT(Contact_ID) FROM Contacts");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO Contacts VALUES(1, 'Anika Costa', 'acoasta@company.com')");
            query.execute("INSERT INTO Contacts VALUES(2, 'Daniel Garcia', 'dgarcia@company.com')");
            query.execute("INSERT INTO Contacts VALUES(3, 'Li Lee', 'llee@company.com')");
        }
        results = query.executeQuery("SELECT COUNT(Country_ID) FROM Countries");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO Countries VALUES(1, 'U.S', NOW(), 'script', NOW(), 'script')");
            query.execute("INSERT INTO Countries VALUES(2, 'UK', NOW(), 'script', NOW(), 'script')");
            query.execute("INSERT INTO Countries VALUES(3,'Canada',NOW(), 'script', NOW(), 'script')");
        }
        results = query.executeQuery("SELECT COUNT(Division_ID) FROM FirstLevelDivisions");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alabama', 1, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alaska', 54, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Arizona', 02, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Arkansas', 03, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('California', 04, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Colorado', 05, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Connecticut', 06, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Delaware', 07, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('District of Columbia', 08, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Florida', 09, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Georgia', 10, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Hawaii', 52, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Idaho', 11, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Illinois', 12, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Indiana', 13, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Iowa', 14, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Kansas', 15, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Kentucky', 16, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Louisiana', 17, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Maine', 18, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Maryland', 19, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Massachusetts', 20, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Michigan', 21, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Minnesota', 22, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Mississippi', 23, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Missouri', 24, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Montana', 25, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nebraska', 26, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nevada', 27, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Hampshire', 28, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Jersey', 29, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Mexico', 30, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New York', 31, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('North Carolina', 32, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('North Dakota', 33, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Ohio', 34, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Oklahoma', 35, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Oregon', 36, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Pennsylvania', 37, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Rhode Island', 38, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('South Carolina', 39, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('South Dakota', 40, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Tennessee', 41, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Texas', 42, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Utah', 43, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Vermont', 44, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Virginia', 45, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Washington', 46, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('West Virginia', 47, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wisconsin', 48, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wyoming', 49, NOW(), 'script', NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alberta', 61, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('British Columbia', 62, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Manitoba', 63, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Brunswick', 64, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Newfoundland and Labrador', 72, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Northwest Territories', 60, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nova Scotia', 65, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nunavut', 70, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Ontario', 67, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Prince Edward Island', 66, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Québec', 68, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Saskatchewan', 69, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Yukon', 71, NOW(), 'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('England', 101, NOW(), 'script', NOW(), 'script', 2 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wales', 102, NOW(), 'script', NOW(), 'script', 2 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Scotland',103, NOW(), 'script', NOW(), 'script', 2 )");
            query.execute("INSERT INTO FirstLevelDivisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Northern Ireland', 104, NOW(), 'script', NOW(), 'script', 2 )");
            results = query.executeQuery("SELECT COUNT(Customer_ID) FROM Customers");
            results.next();
            if (results.getInt(1) == 0) {
                query.execute("INSERT INTO Customers VALUES(1, 'Daddy Warbucks', '1919 Boardwalk', '01291', '869-908-1875', NOW(), 'script', NOW(), 'script', 29)");
                query.execute("INSERT INTO Customers VALUES(2, 'Lady McAnderson', '2 Wonder Way', 'AF19B', '11-445-910-2135', NOW(), 'script', NOW(), 'script', 103)");
                query.execute("INSERT INTO Customers VALUES(3, 'Dudley Do-Right', '48 Horse Manor ', '28198', '874-916-2671', NOW(), 'script', NOW(), 'script', 60)");
            }
            results = query.executeQuery("SELECT COUNT(Appointment_ID) FROM Appointments");
            results.next();
            if (results.getInt(1) == 0) {
                query.execute("INSERT INTO Appointments VALUES(1, 'title', 'description', 'location', 'Planning Session', '2020-05-28 12:00:00', '2020-05-28 13:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 3)");
                query.execute("INSERT INTO Appointments VALUES(2, 'title', 'description', 'location', 'De-Briefing', '2020-05-29 12:00:00', '2020-05-29 13:00:00', NOW(), 'script', NOW(), 'script', 2, 2, 2)");
            }
        }
    }

    public static void popContactsList(ObservableList<Contacts> contactList) throws SQLException {
        ResultSet contactRes = null;
        Statement query = connection.createStatement();
        String sqlQuery = "SELECT * FROM Contacts";
        contactRes = query.executeQuery(sqlQuery);
        while (contactRes.next()) {
            Contacts tempContact = new Contacts();
            tempContact.setName(contactRes.getString("Contact_Name"));
            tempContact.setEmail(contactRes.getString("Email"));
            tempContact.setContactId(contactRes.getInt("Contact_ID"));
            contactList.add(tempContact);
        }
    }

    public static void initializeCustomerList(ObservableList<Customer> custList) {
        ResultSet custResults = null;
        ResultSet temp = null;
        try {
            Statement query = connection.createStatement();
            Statement qTemp = connection.createStatement();
            custResults = query.executeQuery("SELECT * FROM Customers");
            while (custResults.next()) {
                Customer tempCust = new Customer();
                tempCust.setCustomerId(custResults.getInt("Customer_ID"));
                tempCust.setCustomerName(custResults.getString("Customer_Name"));
                tempCust.setCustomerStreetAddress(custResults.getString("Address"));
                tempCust.setCustomerPostal(custResults.getString("Postal_Code"));
                tempCust.setCustomerPhone(custResults.getString("Phone"));
                tempCust.setDivisionId(custResults.getInt("Division_ID"));
                int searchID = custResults.getInt("Division_ID");
                tempCust.setDivisionId(searchID);
                temp = qTemp.executeQuery("SELECT Country_ID, Division FROM FirstLevelDivisions WHERE Division_ID = " + searchID);
                while (temp.next()) {
                    tempCust.setCustomerStateProv(temp.getString("Division"));
                    searchID = temp.getInt("Country_ID");
                }
                temp = qTemp.executeQuery("SELECT Country FROM Countries WHERE Country_ID = " + searchID);
                while (temp.next()) {
                    tempCust.setCustomerCountry(temp.getString("Country"));
                }
                custList.add(tempCust);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void initializeAppointmentList(ObservableList<Appointment> appList) {
        ResultSet appResults = null;
        try {
            Statement query = connection.createStatement();
            appResults = query.executeQuery("SELECT * FROM Appointments");
            while (appResults.next()) {
                Appointment tempApp = new Appointment();
                Customer tempCust = new Customer();
                Contacts tempContact = new Contacts();
                tempApp.setDescription(appResults.getString("Description"));
                tempApp.setTitle(appResults.getString("Title"));
                tempApp.setAppointmentId(appResults.getInt("Appointment_ID"));
                tempApp.setLocation(appResults.getString("Location"));
                tempApp.setType(appResults.getString("Type"));
                tempApp.setStart(appResults.getTimestamp("Start"));
                tempApp.setEnd(appResults.getTimestamp("End"));
                tempApp.setCustomerId(appResults.getInt("Customer_ID"));
                tempCust = getCustomer(appResults.getInt("Customer_ID"));
                tempApp.setCustomerName(tempCust.getCustomerName());
                tempApp.setUserId(appResults.getInt("User_ID"));
                tempApp.setContactId(appResults.getInt("Contact_ID"));
                tempContact = getContact(appResults.getInt("Contact_ID"));
                tempApp.setContactName(tempContact.getName());
                appList.add(tempApp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Contacts getContact(int contactID) throws SQLException {
        Contacts transferContact = new Contacts();
        ResultSet contResults = null;
        String sqlQuery = "SELECT * FROM Contacts WHERE Contact_ID = " + contactID;
        Statement query = connection.createStatement();
        contResults = query.executeQuery(sqlQuery);
        contResults.next();
        transferContact.setContactId(contactID);
        transferContact.setEmail(contResults.getString("Email"));
        transferContact.setName(contResults.getString("Contact_Name"));
        return transferContact;
    }

    public static void addCustomer(Customer custToAdd) throws SQLException {
        String sqlQuery = "INSERT INTO Customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES ('" +
                custToAdd.getCustomerName() + "' , '" +
                custToAdd.getCustomerStreetAddress() + "', '" +
                custToAdd.getCustomerPostal() + "', '" +
                custToAdd.getCustomerPhone() + "', '" +
                custToAdd.getDivisionId() + "')";
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

    public static void deleteCustomer(int customerID) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Customer_ID =" + customerID;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
        sqlQuery = "DELETE FROM Customers WHERE Customer_ID = " + customerID;
        query.execute(sqlQuery);
    }

    public static void updateCustomer(Customer custToUpdate) throws SQLException {
        String sqlQuery = "UPDATE Customers SET " +
                "Customer_Name = '" + custToUpdate.getCustomerName() +
                "', Address = '" + custToUpdate.getCustomerStreetAddress() +
                "', Postal_Code = '" + custToUpdate.getCustomerPostal() +
                "', Phone = '" + custToUpdate.getCustomerPhone() +
                "', Division_ID = '" + custToUpdate.getDivisionId() +
                "' WHERE Customer_ID = " + custToUpdate.getCustomerId();
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

    public static ObservableList<Appointment> getCustomerApps(int customerId) throws SQLException {
        ObservableList<Appointment> appList = FXCollections.observableArrayList();
        ResultSet appResults = null;
        String sqlQuery = "SELECT * FROM Appointments WHERE Customer_ID = " + customerId;
        Statement query = connection.createStatement();
        appResults = query.executeQuery(sqlQuery);
        while (appResults.next()) {
            Appointment transferApp = new Appointment();
            transferApp.setContactId(appResults.getInt("Contact_ID"));
            transferApp.setAppointmentId(appResults.getInt("Appointment_ID"));
            transferApp.setUserId(appResults.getInt("User_ID"));
            transferApp.setCustomerId(appResults.getInt("Customer_ID"));
            transferApp.setTitle(appResults.getString("Title"));
            transferApp.setDescription(appResults.getString("Description"));
            transferApp.setLocation(appResults.getString("Location"));
            transferApp.setType(appResults.getString("Type"));
            transferApp.setEnd(appResults.getTimestamp("End"));
            transferApp.setStart(appResults.getTimestamp("Start"));
            appList.add(transferApp);
        }
        return appList;
     }

        public static void addAppointment (Appointment appToAdd) throws SQLException {
        String sqlQuery = "INSERT INTO Appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES ('" +
                appToAdd.getAppointmentId() + "', '" +
                appToAdd.getTitle() + "', '" +
                appToAdd.getDescription() + "', '" +
                appToAdd.getLocation() + "', '" +
                appToAdd.getType() + "', '" +
                appToAdd.getStart() + "', '" +
                appToAdd.getEnd() + "', '" +
                appToAdd.getCustomerId() + "', '" +
                appToAdd.getUserId() + "', '" +
                appToAdd.getContactId() + "')";
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

        public static void deleteAppointment ( int appID) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Appointment_ID = " + appID;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

        public static void updateAppointment (Appointment appToUpdate) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Appointment_ID = " + appToUpdate;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

        public static Customer getCustomer ( int customerID) throws SQLException {
        Customer transferCust = new Customer();
        ResultSet custResults = null;
        String sqlQuery = "SELECT * FROM Customers WHERE Customer_ID = " + customerID;
        Statement query = connection.createStatement();
        custResults = query.executeQuery(sqlQuery);
        custResults.next();
        transferCust.setCustomerId(custResults.getInt("Customer_ID"));
        transferCust.setCustomerName(custResults.getString("Customer_Name"));
        transferCust.setCustomerStreetAddress(custResults.getString("Address"));
        transferCust.setCustomerPostal(custResults.getString("Postal_Code"));
        transferCust.setCustomerPhone(custResults.getString("Phone"));
        int searchID = custResults.getInt("Division_ID");
        transferCust.setDivisionId(searchID);
        sqlQuery = "SELECT Country_ID, Division FROM FirstLevelDivisions WHERE Division_ID = " + searchID;
        ResultSet temp = query.executeQuery(sqlQuery);
        temp.next();
        transferCust.setCustomerStateProv(temp.getString("Division"));
        searchID = temp.getInt("Country_ID");
        sqlQuery = "SELECT Country FROM Countries WHERE Country_ID = " + searchID;
        temp = query.executeQuery(sqlQuery);
        temp.next();
        transferCust.setCustomerCountry(temp.getString("Country"));
        return transferCust;
    }

        public static Appointment getAppointment ( int appointmentID) throws SQLException {
        Appointment transferApp = new Appointment();
        ResultSet appResults = null;
        String sqlQuery = "SELECT * FROM Appointments WHERE Appointment_ID = " + appointmentID;
        Statement query = connection.createStatement();
        appResults = query.executeQuery(sqlQuery);
        transferApp.setContactId(appResults.getInt("Contact_ID"));
        transferApp.setAppointmentId(appResults.getInt("Appointment_ID"));
        transferApp.setUserId(appResults.getInt("User_ID"));
        transferApp.setCustomerId(appResults.getInt("Customer_ID"));
        transferApp.setTitle(appResults.getString("Title"));
        transferApp.setDescription(appResults.getString("Description"));
        transferApp.setLocation(appResults.getString("Location"));
        transferApp.setType(appResults.getString("Type"));
        transferApp.setStart(appResults.getTimestamp("Start"));
        transferApp.setEnd(appResults.getTimestamp("End"));
        return transferApp;
    }

        public static void timeCheck (LocalTime current){
        Time now = Time.valueOf(current);
    }

        public static void deleteCustomerAppointment ( int custId) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Customer_ID = " + custId;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

        public static void getFLD (ObservableList < FirstLevelDivisions > fldList,int countryID) throws SQLException {
        ResultSet fldResults = null;
        String sqlQuery = "SELECT FirstLevelDivisions.Division, FirstLevelDivisions.Division_Id, FirstLevelDivisions.Country_ID, Countries.Country" +
                " FROM FirstLevelDivisions, Countries WHERE FirstLevelDivisions.Country_ID = Countries.Country_ID AND Countries.Country_ID =" + countryID;
        Statement query = connection.createStatement();
        Statement tempQ = null;
        fldResults = query.executeQuery(sqlQuery);
        fldList.clear();
        while (fldResults.next()) {
            FirstLevelDivisions tempFLD = new FirstLevelDivisions();
            tempFLD.setDivisionName(fldResults.getString("Division"));
            tempFLD.setDivisionId(fldResults.getInt("Division_ID"));
            int countryId = fldResults.getInt("Country_ID");
            tempFLD.setCountryId(countryId);
            tempFLD.setCountryName(fldResults.getString("Country"));
            fldList.add(tempFLD);
        }
    }

        public static FirstLevelDivisions getDivision ( int divisionID) throws SQLException {
        ResultSet fldResults = null;
        String sqlQuery = "SELECT * FROM FirstLevelDivisions WHERE Division_ID =" + divisionID;
        Statement query = connection.createStatement();
        Statement tempQ = null;
        fldResults = query.executeQuery(sqlQuery);
        FirstLevelDivisions tempFLD = new FirstLevelDivisions();
        while (fldResults.next()) {
            tempFLD.setDivisionName(fldResults.getString("Division"));
            tempFLD.setDivisionId(fldResults.getInt("Division_ID"));
            int countryId = fldResults.getInt("Country_ID");
            tempFLD.setCountryId(countryId);
            Countries tempCountry = getSingleCountry(countryId);
            tempFLD.setCountryName(tempCountry.getCountryName());
        }
        return tempFLD;
    }

        public static void getCountry (ObservableList < Countries > countryList) throws SQLException {
        ResultSet countryResults = null;
        String sqlQuery = "SELECT * FROM Countries";
        Statement query = connection.createStatement();
        countryResults = query.executeQuery(sqlQuery);
        while (countryResults.next()) {
            Countries tempCountry = new Countries();
            tempCountry.setCountryID(countryResults.getInt("Country_ID"));
            tempCountry.setCountryName(countryResults.getString("Country"));
            countryList.add(tempCountry);
        }
    }

        public static Countries getSingleCountry ( int countryId) throws SQLException {
        ResultSet countryResults = null;
        String sqlQuery = "SELECT * FROM Countries WHERE Country_ID =" + countryId;
        Statement query = connection.createStatement();
        Statement tempQ = null;
        countryResults = query.executeQuery(sqlQuery);
        Countries tempCountry = new Countries();
        while (countryResults.next()) {
            tempCountry.setCountryID(countryId);
            tempCountry.setCountryName(countryResults.getString("Country"));
        }
        return tempCountry;
    }

        public static void popLogInHash (HashMap < String, String > loginHash) throws SQLException {
        ResultSet loginResults = null;
        String sqlQuery = "SELECT User_Name, Password FROM Users";
        Statement query = connection.createStatement();
        loginResults = query.executeQuery(sqlQuery);
        while (loginResults.next()) {
            loginHash.put(loginResults.getString("User_Name"), loginResults.getString("Password"));
        }
    }

}
