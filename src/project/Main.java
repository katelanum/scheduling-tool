package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;

import java.util.*;



//colors: https://material.io/resources/color/#!/?view.left=0&view.right=1&primary.color=1A237E&secondary.color=761a7e

public class Main extends Application {
    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private static ObservableList<Users> userList = FXCollections.observableArrayList();
    private static ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    private static ObservableList<Countries> countryList = FXCollections.observableArrayList();
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivisions> divisionsList = FXCollections.observableArrayList();
    private final Alert mainScreenAlert = new Alert(Alert.AlertType.WARNING);
    private Locale location;

//    public void getUserLocation() {
//        location = Locale.getDefault();
//    }
//
//    public Text setLoginLocationText() {
//        LoginController.loginLocationText.setText(location.getCountry());
//        return LoginController.loginLocationText;
//    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
        primaryStage = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"), languageBundle);
//        getUserLocation();
//        setLoginLocationText();
        primaryStage.show();
    }

    // md5 hash password (ideally), can't do due to class project restrictions


    public static void main(String[] args) throws SQLException {
        try {
            Database.initializeDB();
            Database.initializeAppointmentList(appointmentList);
            Database.initializeCustomerList(customerList);
        } catch (SQLSyntaxErrorException created) {
            System.err.println(created);
        }
        Database.test();

        launch(args);
    }

    public static ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ObservableList<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public ObservableList<Users> getUserList() {
        return userList;
    }

    public void setUserList(ObservableList<Users> userList) {
        this.userList = userList;
    }

    public ObservableList<Contacts> getContactList() {
        return contactList;
    }

    public void setContactList(ObservableList<Contacts> contactList) {
        this.contactList = contactList;
    }

    public ObservableList<Countries> getCountryList() {
        return countryList;
    }

    public void setCountryList(ObservableList<Countries> countryList) {
        this.countryList = countryList;
    }

    public ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(ObservableList<Customer> customerList) {
        this.customerList = customerList;
    }

    public ObservableList<FirstLevelDivisions> getDivisionsList() {
        return divisionsList;
    }

    public void setDivisionsList(ObservableList<FirstLevelDivisions> divisionsList) {
        this.divisionsList = divisionsList;
    }
}
