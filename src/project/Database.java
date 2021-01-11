package project;

import com.mysql.cj.protocol.Resultset;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //query.execute("CREATE DATABASE SchedulingDB");
         query.execute("CREATE TABLE IF NOT EXISTS Contacts (" +
                 "Contact_ID INT(10) NOT NULL," +
                 "Contact_Name VARCHAR(50)," +
                 "Email VARCHAR(50)," +
                 "PRIMARY KEY (Contact_ID))");
         query.execute("CREATE TABLE IF NOT EXISTS Users (" +
                 "User_ID INT(10) NOT NULL," +
                 "User_Name VARCHAR(50) UNIQUE," +
                 "Password TEXT," +
                 "PRIMARY KEY (User_ID))");
         query.execute("CREATE TABLE IF NOT EXISTS Countries (" +
                 "Country_ID INT(10) NOT NULL," +
                 "Country VARCHAR(50)," +
                 "PRIMARY KEY (Country_ID))");
         query.execute("CREATE TABLE IF NOT EXISTS FirstLevelDivisions (" +
                 "Division_ID INT(10) NOT NULL," +
                 "Division VARCHAR(50)," +
                 "Country_ID INT(10)," +
                 "PRIMARY KEY (Division_ID)," +
                 "CONSTRAINT fk_firstleveldivisions_country FOREIGN KEY (Country_ID) REFERENCES Countries (Country_ID))");
         query.execute("CREATE TABLE IF NOT EXISTS Customers (" +
                 "Customer_ID INT(10) NOT NULL," +
                 "Customer_Name VARCHAR(50)," +
                 "Address VARCHAR(100)," +
                 "Postal_Code VARCHAR(50)," +
                 "Phone VARCHAR(50)," +
                 "Division_ID INT(10)," +
                 "PRIMARY KEY (Customer_ID)," +
                 "CONSTRAINT fk_customers_division FOREIGN KEY (Division_ID) REFERENCES FirstLevelDivisions (Division_ID))");
        query.execute("CREATE TABLE Appointments (" +
                "Appointment_ID INT(10) NOT NULL," +
                "Title VARCHAR(50)," +
                "Description VARCHAR(50)," +
                "Location VARCHAR(50)," +
                "Type VARCHAR(50)," +
                "Start DATETIME," +
                "End DATETIME," +
                "Customer_ID INT(10)  NOT NULL," +
                "User_ID INT(10) NOT NULL," +
                "Contact_ID INT(10) NOT NULL," +
                "PRIMARY KEY (Appointment_ID)," +
                "CONSTRAINT fk_appointments_customer FOREIGN KEY (Customer_ID) REFERENCES Customers(Customer_ID)," +
                "CONSTRAINT fk_appointments_user FOREIGN KEY (User_ID) REFERENCES Users(User_ID)," +
                "CONSTRAINT fk_appointments_contact FOREIGN KEY (Contact_ID) REFERENCES Contacts(Contact_ID))");
    }

    public static void test() {
        try {
            Statement query = connection.createStatement();

            ResultSet appointments = query.executeQuery("SELECT * FROM Contacts");
            while(appointments.next()) {
                System.out.println(appointments.getString("Contact_ID"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public static void initializeCustomerList(ObservableList<Customer> custList) {
        ResultSet custResults = null;
        try {
            Statement query = connection.createStatement();
            custResults = query.executeQuery("SELECT * FROM Customers");
            while (custResults.next()) {
                Customer tempCust = new Customer();
                tempCust.setCustomerId(custResults.getInt("Customer_ID"));
                tempCust.setCustomerName(custResults.getString("Customer_Name"));
                tempCust.setCustomerStreetAddress(custResults.getString("Address"));
                tempCust.setCustomerPostal(custResults.getString("Postal_Code"));
                tempCust.setCustomerPhone(custResults.getString("Phone"));
                tempCust.setDivisionId(custResults.getInt("Division_ID"));
                custList.add(tempCust);
            }
        } catch (SQLException ex){
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
                tempApp.setDescription(appResults.getString("Description"));
                tempApp.setTitle(appResults.getString("Title"));
                tempApp.setAppointmentId(appResults.getInt("Appointment_ID"));
                tempApp.setLocation(appResults.getString("Location"));
                tempApp.setType(appResults.getString("Type"));
                tempApp.setStart(appResults.getDate("Start"));
                tempApp.setEnd(appResults.getDate("End"));
                tempApp.setCustomerId(appResults.getInt("Customer_ID"));
                tempApp.setUserId(appResults.getInt("User_ID"));
                tempApp.setContactId(appResults.getInt("Contact_ID"));
                appList.add(tempApp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean generalQuery(String query) {
        //Statement state = query;
        boolean isSuccessful = false;
        return isSuccessful;
    }

    public static boolean addCustomer (Customer custToAdd) {
        boolean isSuccessful = false;
        return generalQuery("");

    }

    public static boolean deleteCustomer (int customerID) {
        boolean isSuccessful = false;
        return generalQuery("");
    }

    public static boolean updateCustomer (Customer custToUdate) {
        boolean isSuccessful = false;
        return generalQuery("");
    }

    public static boolean addAppointment(Appointment appToAdd) {
        boolean isSuccessful = false;
        return generalQuery("");
    }

    public static boolean deleteAppointment (int appID) {
        boolean isSuccessful = false;
        return generalQuery("");
    }

    public static boolean updateAppointment (Appointment appToUpdate) {
        boolean isSuccessful = false;
        return generalQuery("");
    }

    public static Customer getCustomer (int customerID) {
        Customer transferCust = null;
        //query
        return transferCust;
    }

    public static Appointment getAppointment (int appointmentID) {
        Appointment transferApp = null;
        //query
        return transferApp;
    }
}

