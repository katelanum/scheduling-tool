package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * The Database class is the interface between the Java code and the MySQL database
 *
 * @author katelanum
 */
public class Database {

    private static Connection connection = null;

    /**
     *  This function connects with the database, then creates each table as in the ERD diagram. After the tables are
     *  created, the data provided is inserted into the database.
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
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
        query.execute("CREATE TABLE IF NOT EXISTS Contact (" +
                "Contact_ID INT(10) NOT NULL AUTO_INCREMENT, " +
                "Contact_Name VARCHAR(50), " +
                "Email VARCHAR(50), " +
                "PRIMARY KEY (Contact_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS User (" +
                "User_ID INT(10) NOT NULL AUTO_INCREMENT, " +
                "User_Name VARCHAR(50) UNIQUE, " +
                "Password TEXT, " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "PRIMARY KEY (User_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS Country (" +
                "Country_ID INT(10) NOT NULL, " +
                "Country VARCHAR(50), " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "PRIMARY KEY (Country_ID))");
        query.execute("CREATE TABLE IF NOT EXISTS FirstLevelDivision (" +
                "Division_ID INT(10) NOT NULL, " +
                "Division VARCHAR(50), " +
                "Country_ID INT(10), " +
                "Create_Date DATETIME, " +
                "Created_By VARCHAR(50), " +
                "Last_Update TIMESTAMP, " +
                "Last_Updated_By VARCHAR(50), " +
                "PRIMARY KEY (Division_ID), " +
                "CONSTRAINT fk_firstleveldivisions_country FOREIGN KEY (Country_ID) REFERENCES Country (Country_ID))");
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
                "CONSTRAINT fk_customers_division FOREIGN KEY (Division_ID) REFERENCES FirstLevelDivision " +
                "(Division_ID))");
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
                "CONSTRAINT fk_appointments_user FOREIGN KEY (User_ID) REFERENCES User(User_ID), " +
                "CONSTRAINT fk_appointments_contact FOREIGN KEY (Contact_ID) REFERENCES Contact(Contact_ID))");
        ResultSet results = query.executeQuery("SELECT COUNT(User_ID) FROM User");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO User VALUES (1, 'test', 'test', NOW(), 'script', NOW(), 'script')");
            query.execute("INSERT INTO User VALUES (2, 'admin', 'admin', NOW(), 'script', NOW(), 'script')");
        }
        results = query.executeQuery("SELECT COUNT(Contact_ID) FROM Contact");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO Contact VALUES(1, 'Anika Costa', 'acoasta@company.com')");
            query.execute("INSERT INTO Contact VALUES(2, 'Daniel Garcia', 'dgarcia@company.com')");
            query.execute("INSERT INTO Contact VALUES(3, 'Li Lee', 'llee@company.com')");
        }
        results = query.executeQuery("SELECT COUNT(Country_ID) FROM Country");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO Country VALUES(1, 'U.S', NOW(), 'script', NOW(), 'script')");
            query.execute("INSERT INTO Country VALUES(2, 'UK', NOW(), 'script', NOW(), 'script')");
            query.execute("INSERT INTO Country VALUES(3,'Canada',NOW(), 'script', NOW(), 'script')");
        }
        results = query.executeQuery("SELECT COUNT(Division_ID) FROM FirstLevelDivision");
        results.next();
        if (results.getInt(1) == 0) {
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alabama', 1, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alaska', 54, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Arizona', 02, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Arkansas', 03, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('California', 04, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Colorado', 05, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Connecticut', 06, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Delaware', 07, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('District of Columbia', 08, NOW(), 'script', " +
                    "NOW(), 'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Florida', 09, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Georgia', 10, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Hawaii', 52, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Idaho', 11, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Illinois', 12, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Indiana', 13, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Iowa', 14, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Kansas', 15, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Kentucky', 16, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Louisiana', 17, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Maine', 18, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Maryland', 19, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Massachusetts', 20, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Michigan', 21, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Minnesota', 22, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Mississippi', 23, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Missouri', 24, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Montana', 25, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nebraska', 26, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nevada', 27, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Hampshire', 28, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Jersey', 29, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Mexico', 30, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New York', 31, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('North Carolina', 32, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('North Dakota', 33, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Ohio', 34, NOW(), 'script', NOW(), 'script'," +
                    " 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Oklahoma', 35, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Oregon', 36, NOW(), 'script', NOW(), " +
                    "script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Pennsylvania', 37, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Rhode Island', 38, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('South Carolina', 39, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('South Dakota', 40, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Tennessee', 41, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Texas', 42, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Utah', 43, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Vermont', 44, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Virginia', 45, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Washington', 46, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('West Virginia', 47, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wisconsin', 48, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wyoming', 49, NOW(), 'script', NOW(), " +
                    "'script', 1 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alberta', 61, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('British Columbia', 62, NOW(), 'script', " +
                    "NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Manitoba', 63, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Brunswick', 64, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By," +
                    " Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Newfoundland and Labrador', 72, NOW(), " +
                    "'script', NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Northwest Territories', 60, NOW(), 'script', " +
                    "NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nova Scotia', 65, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nunavut', 70, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Ontario', 67, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Prince Edward Island', 66, NOW(), 'script', " +
                    "NOW(), 'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Québec', 68, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Saskatchewan', 69, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Yukon', 71, NOW(), 'script', NOW(), " +
                    "'script', 3 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('England', 101, NOW(), 'script', NOW(), " +
                    "'script', 2 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wales', 102, NOW(), 'script', NOW(), " +
                    "'script', 2 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Scotland',103, NOW(), 'script', NOW(), " +
                    "'script', 2 )");
            query.execute("INSERT INTO FirstLevelDivision(Division, Division_ID, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Northern Ireland', 104, NOW(), 'script', " +
                    "NOW(), 'script', 2 )");
            results = query.executeQuery("SELECT COUNT(Customer_ID) FROM Customers");
            results.next();
            if (results.getInt(1) == 0) {
                query.execute("INSERT INTO Customers VALUES(1, 'Daddy Warbucks', '1919 Boardwalk', '01291', " +
                        "'869-908-1875', NOW(), 'script', NOW(), 'script', 29)");
                query.execute("INSERT INTO Customers VALUES(2, 'Lady McAnderson', '2 Wonder Way', 'AF19B', " +
                        "'11-445-910-2135', NOW(), 'script', NOW(), 'script', 103)");
                query.execute("INSERT INTO Customers VALUES(3, 'Dudley Do-Right', '48 Horse Manor ', '28198', " +
                        "'874-916-2671', NOW(), 'script', NOW(), 'script', 60)");
            }
            results = query.executeQuery("SELECT COUNT(Appointment_ID) FROM Appointments");
            results.next();
            if (results.getInt(1) == 0) {
                query.execute("INSERT INTO Appointments VALUES(1, 'title', 'description', 'location', " +
                        "'Planning Session', '2020-05-21 14:00:00', '2020-05-21 15:00:00', NOW(), 'script', NOW(), " +
                        "'script', 1, 1, 3)");
                query.execute("INSERT INTO Appointments VALUES(2, 'title', 'description', 'location2', " +
                        "'De-Briefing', '2020-05-29 14:00:00', '2020-05-29 15:00:00', NOW(), 'script', NOW(), " +
                        "'script', 2, 2, 2)");
            }
        }
    }

    /**
     *  Selects all information from the Contacts table in the database and puts the Contact_Name, Email, and Contact_ID
     *  values into a temporary contact which is then added to the contactList
     *
     * @param contactList the list of contacts from the Controller that is using the list
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void popContactsList(ObservableList<Contact> contactList) throws SQLException {
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
        ResultSet contactRes;
        Statement query = connection.createStatement();
        String sqlQuery = "SELECT * FROM Contact";
        contactRes = query.executeQuery(sqlQuery);
        while (contactRes.next()) {
            Contact tempContact = new Contact();
            tempContact.setName(contactRes.getString("Contact_Name"));
            tempContact.setEmail(contactRes.getString("Email"));
            tempContact.setContactId(contactRes.getInt("Contact_ID"));
            contactList.add(tempContact);
        }
    }

    /**
     *  Selects all information from the Customers table in the database and then creates a temporary Customer with the
     *  Customer_ID, Customer_Name, Address, Postal_code, Phone,  and Division_ID from the Customers table,
     *  the Country_ID and Division from the Division table, and the Country from the Country table. The temporary
     *  Customer is then added to the list of Customers.
     *
     * @param custList the list of customers from the Controller that is using the list
     */
    public static void initializeCustomerList(ObservableList<Customer> custList) {
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
        ResultSet custResults;
        ResultSet temp;
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
                temp = qTemp.executeQuery("SELECT Country_ID, Division FROM FirstLevelDivision WHERE " +
                        "Division_ID = " + searchID);
                while (temp.next()) {
                    tempCust.setCustomerStateProv(temp.getString("Division"));
                    searchID = temp.getInt("Country_ID");
                }
                temp = qTemp.executeQuery("SELECT Country FROM Country WHERE Country_ID = " + searchID);
                while (temp.next()) {
                    tempCust.setCustomerCountry(temp.getString("Country"));
                }
                custList.add(tempCust);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  Selects all data from the Appointments table in the database then creates a temporary Appointment with the
     *  Description, Title, Appointment_ID, Location, Type, Start, End, Customer_ID, User_Id, and Contact_Id from the
     *  Appointments table. The ContactName is from a temporary Contact using the Contact_Id from the database using
     *  getContact and the CustomerName is also from a temporary Customer created with the Customer_Id from the database
     *  and the getCustomer function. The temporary appointment is then added to appList
     *
     * @param appList the list of appointments from the Controller that is using the list
     */
    public static void initializeAppointmentList(ObservableList<Appointment> appList) {
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
        ResultSet appResults;
        try {
            Statement query = connection.createStatement();
            appResults = query.executeQuery("SELECT * FROM Appointments");
            while (appResults.next()) {
                Appointment tempApp = new Appointment();
                Customer tempCust;
                Contact tempContact;
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

    /**
     *  Takes in a contactId, then queries the database to find the information on that contact. Creates transferContact,
     *  a contact with the data form the database. Returns transferContact.
     *
     * @param contactID the contactId used to query the database for contact information
     *
     * @return the contact created with the data from the database connected to the contactId that was passed in
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static Contact getContact(int contactID) throws SQLException {
        Contact transferContact = new Contact();
        ResultSet contResults;
        String sqlQuery = "SELECT * FROM Contact WHERE Contact_ID = " + contactID;
        Statement query = connection.createStatement();
        contResults = query.executeQuery(sqlQuery);
        contResults.next();
        transferContact.setContactId(contactID);
        transferContact.setEmail(contResults.getString("Email"));
        transferContact.setName(contResults.getString("Contact_Name"));
        return transferContact;
    }

    /**
     *  Takes in a Customer and inserts a row into the database with the values for Customer_Name, Address, Postal_Code,
     *  Phone, and Division coming from the values of the Customer that was taken in.
     *
     * @param custToAdd the Customer object with the variable values that need to be added to the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
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

    /**
     *  Takes in a customerID and then deletes the row containing that customerID from the database
     *
     * @param customerID the int representing customerID is used to locate the row to be deleted from the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void deleteCustomer(int customerID) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Customer_ID =" + customerID;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
        sqlQuery = "DELETE FROM Customers WHERE Customer_ID = " + customerID;
        query.execute(sqlQuery);
    }

    /**
     *  Takes in a Customer and the row in the database with the matching customerID and updates the values for
     *  Customer_Name, Address, Postal_Code, Phone, and Division coming from the values of the Customer that was taken
     *  in.
     *
     * @param custToUpdate the Customer object with the variable values that need to be added to the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
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

    /**
     *  Takes in a customerID and returns a list of their appointments by selecting all the information on appointments
     *  from the Appointments table where the Customer_ID in the appointment matches the customerId passed into the
     *  function
     *
     * @param customerId is used to find the appointments of that customer
     *
     * @return a list of the appointments that belong to that customer
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static ObservableList<Appointment> getCustomerApps(int customerId) throws SQLException {
        ObservableList<Appointment> appList = FXCollections.observableArrayList();
        ResultSet appResults;
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

    /**
     *  Takes in an Appointment and inserts a row into the database with the values for Title, Description, Location,
     *  Type, Start, End, Customer_ID, User_ID, and Contact_ID coming from the values of the Appointment that was
     *  taken in
     *
     * @param appToAdd the Appointment object with the variable values that need to be added to the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
     public static void addAppointment (Appointment appToAdd) throws SQLException {
        Timestamp end = Timestamp.valueOf(appToAdd.getEnd().toLocalDateTime());
        Timestamp start = Timestamp.valueOf(appToAdd.getStart().toLocalDateTime());
        String sqlQuery = "INSERT INTO Appointments (Appointment_ID, Title, Description, Location, Type, Start, " +
                "End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES ('" +
                appToAdd.getAppointmentId() + "', '" +
                appToAdd.getTitle() + "', '" +
                appToAdd.getDescription() + "', '" +
                appToAdd.getLocation() + "', '" +
                appToAdd.getType() + "', '" +
                start + "', '" +
                end + "', '" +
                appToAdd.getCustomerId() + "', '" +
                appToAdd.getUserId() + "', '" +
                appToAdd.getContactId() + "')";
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

    /**
     *  Takes in an appointmentID and then deletes the row containing that appointmentID from the database
     *
     * @param appID the int representing appointmentID is used to locate the row to be deleted from the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void deleteAppointment ( int appID) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Appointment_ID = " + appID;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

    /**
     * Takes in an Appointment and the row in the database with the matching appointmentID and updates the values for
     * Title, Description, Location, Type, Start, End, CustomerID, UserID, ContactID coming from the values of the
     * Appointment that was taken in
     *
     * @param appToUpdate the Appointment being passed in to get the values for the database from
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void updateAppointment (Appointment appToUpdate) throws SQLException {
        Timestamp end = Timestamp.valueOf(appToUpdate.getEnd().toLocalDateTime());
        Timestamp start = Timestamp.valueOf(appToUpdate.getStart().toLocalDateTime());
        String sqlQuery = "UPDATE Appointments SET " +
                "Title = '" + appToUpdate.getTitle() + "', " +
                "Description = '" + appToUpdate.getDescription() + "', " +
                "Location = '" + appToUpdate.getLocation() + "', " +
                "Type = '" + appToUpdate.getType() + "', " +
                "Start = '" + start + "', " +
                "End = '" + end + "', " +
                "Customer_ID = '" + appToUpdate.getCustomerId() + "', " +
                "User_ID = '" + appToUpdate.getUserId() + "', " +
                "Contact_ID = '" + appToUpdate.getContactId() +
                "' WHERE Appointment_ID = '" + appToUpdate.getAppointmentId() + "'";
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

    /**
     * Takes in a customerID, then queries the database to find the information on that customer. Creates transferCust,
     * a Customer with the data from the database. Return transferCust
     *
     * @param customerID the customerID that is used to find the customer in the database
     *
     * @return theCustomer object with the data from the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static Customer getCustomer (int customerID) throws SQLException {
        Customer transferCust = new Customer();
        ResultSet custResults;
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
        sqlQuery = "SELECT Country_ID, Division FROM FirstLevelDivision WHERE Division_ID = " + searchID;
        ResultSet temp = query.executeQuery(sqlQuery);
        temp.next();
        transferCust.setCustomerStateProv(temp.getString("Division"));
        searchID = temp.getInt("Country_ID");
        sqlQuery = "SELECT Country FROM Country WHERE Country_ID = " + searchID;
        temp = query.executeQuery(sqlQuery);
        temp.next();
        transferCust.setCustomerCountry(temp.getString("Country"));
        return transferCust;
    }

    /**
     *  Takes in a appointmentID, then queries the database to find the information on that appointment. Creates
     *  transferApp, an appointment with the data from the database. Returns transferApp
     *
     * @param appointmentID the appointmentId used to locate the appointment to get the data from
     *
     * @return transferApp Appointment which holds all of the appointment data from the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static Appointment getAppointment ( int appointmentID) throws SQLException {
        Appointment transferApp = new Appointment();
        ResultSet appResults;
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

    /**
     * Gets the current time into Time format and out of LocalTime format
     *
     * @param current the LocalTime value to be modified to a different format
     */
    public static void timeCheck (LocalTime current){
        Time now = Time.valueOf(current);
    }

    /**
     *  Accepts a customer ID and then deletes the row of that customer from the database
     *
     * @param custId the customer Id to be used to find the row to be deleted
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void deleteCustomerAppointment ( int custId) throws SQLException {
        String sqlQuery = "DELETE FROM Appointments WHERE Customer_ID = " + custId;
        Statement query = connection.createStatement();
        query.execute(sqlQuery);
    }

    /**
     *  Populates the list of FirstLevelDivision objects with the contents of rows in the FirstLevelDivision table that
     *  have a countryId that matches the one passed in
     *
     * @param fldList the list of FirstLevelDivision objects being populated
     *
     * @param countryID the ID used to find the FirstLevelDivisions to be included in the list
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void getFLD (ObservableList <FirstLevelDivision> fldList, int countryID) throws SQLException {
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
        ResultSet fldResults;
        String sqlQuery = "SELECT FirstLevelDivision.Division, FirstLevelDivision.Division_Id, " +
                "FirstLevelDivision.Country_ID, Country.Country" +
                " FROM FirstLevelDivision, Country WHERE FirstLevelDivision.Country_ID = Country.Country_ID AND " +
                "Country.Country_ID =" + countryID;
        Statement query = connection.createStatement();
        fldResults = query.executeQuery(sqlQuery);
        fldList.clear();
        while (fldResults.next()) {
            FirstLevelDivision tempFLD = new FirstLevelDivision();
            tempFLD.setDivisionName(fldResults.getString("Division"));
            tempFLD.setDivisionId(fldResults.getInt("Division_ID"));
            int countryId = fldResults.getInt("Country_ID");
            tempFLD.setCountryId(countryId);
            tempFLD.setCountryName(fldResults.getString("Country"));
            fldList.add(tempFLD);
        }
    }

    /**
     *  Accepts a divisionId, creates a temporary FirstLevelDivision object, and fills the tempFLD with the data from
     *  the database where there is a matching divisionId. That tempFLD is then returned.
     *
     * @param divisionID the divisionId used to search the database for the correct FirstLevelDivion
     *
     * @return FirstLevelDivision object with the data from the database where the DivisionId is equal to the one
     * passed in
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static FirstLevelDivision getDivision (int divisionID) throws SQLException {
        ResultSet fldResults;
        String sqlQuery = "SELECT * FROM FirstLevelDivision WHERE Division_ID =" + divisionID;
        Statement query = connection.createStatement();
        fldResults = query.executeQuery(sqlQuery);
        FirstLevelDivision tempFLD = new FirstLevelDivision();
        while (fldResults.next()) {
            tempFLD.setDivisionName(fldResults.getString("Division"));
            tempFLD.setDivisionId(fldResults.getInt("Division_ID"));
            int countryId = fldResults.getInt("Country_ID");
            tempFLD.setCountryId(countryId);
            Country tempCountry = getSingleCountry(countryId);
            tempFLD.setCountryName(tempCountry.getCountryName());
        }
        return tempFLD;
    }

    /**
     *  Accepts a list of Country objects and then populates that list with all of the different Country rows from the
     *  database
     *
     * @param countryList the list to be populated with the contents of the Country table
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void getCountry (ObservableList <Country> countryList) throws SQLException {
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
        ResultSet countryResults;
        String sqlQuery = "SELECT * FROM Country";
        Statement query = connection.createStatement();
        countryResults = query.executeQuery(sqlQuery);
        while (countryResults.next()) {
            Country tempCountry = new Country();
            tempCountry.setCountryID(countryResults.getInt("Country_ID"));
            tempCountry.setCountryName(countryResults.getString("Country"));
            countryList.add(tempCountry);
        }
    }

    /**
     *  Accepts a countryId and creates a Country object with the data from the Country table in the database where the
     *  countryId matches the one passed in. That created Country object is then returned.
     *
     * @param countryId the id used to search the database and identify the correct information to fill the Country
     * object
     *
     * @return the created Country object with the data that matches the database
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static Country getSingleCountry (int countryId) throws SQLException {
        ResultSet countryResults;
        String sqlQuery = "SELECT * FROM Country WHERE Country_ID =" + countryId;
        Statement query = connection.createStatement();
        countryResults = query.executeQuery(sqlQuery);
        Country tempCountry = new Country();
        while (countryResults.next()) {
            tempCountry.setCountryID(countryId);
            tempCountry.setCountryName(countryResults.getString("Country"));
        }
        return tempCountry;
    }

    /**
     *  Populates the HashMap of usernames and passwords for login from the users table of the database
     *
     * @param loginHash the HashMap being populated in the function
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void popLogInHash (HashMap < String, String > loginHash) throws SQLException {
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
        ResultSet loginResults;
        String sqlQuery = "SELECT User_Name, Password FROM User";
        Statement query = connection.createStatement();
        loginResults = query.executeQuery(sqlQuery);
        while (loginResults.next()) {
            loginHash.put(loginResults.getString("User_Name"), loginResults
                    .getString("Password"));
        }
    }

    /**
     *  Populates a list of user ids from the User table
     *
     * @param userList the list of that is being populated
     *
     * @throws SQLException if there is a problem with either the SQL syntax or if there is a problem running the SQL
     * commands on the database
     */
    public static void userList(ObservableList<String> userList) throws SQLException {
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
        ResultSet userRes;
        String sqlQuery = "SELECT User_ID FROM User";
        Statement query = connection.createStatement();
        userRes = query.executeQuery(sqlQuery);
        while (userRes.next()) {
            userList.add(userRes.getString("User_ID"));
        }
    }

}
