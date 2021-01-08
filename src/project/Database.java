package project;

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

    public static void main(String[] args) {
        final String JDBCDriver = "com.mysql.jdbc.Driver";
        final String databaseURL = "jdbc:mysql:wgudb.ucertify.com/WJ03Pjl";
        final String databaseUsername = "U03Pjl";
        final String databasePassword = "53688043361";
        try {
            Class.forName(JDBCDriver);
            connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        } catch (ClassNotFoundException | SQLException dbEx) {
            dbEx.printStackTrace();
        }
    }

    private static boolean generalQuery(String query) {
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

