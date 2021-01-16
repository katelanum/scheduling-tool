package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;

import java.util.*;
import java.util.stream.Collectors;


//colors: https://material.io/resources/color/#!/?view.left=0&view.right=1&primary.color=1A237E&secondary.color=761a7e

public class Main extends Application {
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
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        primaryStage = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"), languageBundle);
//        getUserLocation();
//        setLoginLocationText();
        primaryStage.show();
    }

    // md5 hash password (ideally), can't do due to class project restrictions


    public static void main(String[] args) throws SQLException {
        try {
            Database.initializeDB();
        } catch (SQLSyntaxErrorException created) {
            System.err.println(created);
        }
        Database.test();
        launch(args);

        List<Customer> customers = new ArrayList<>();

        for (Customer customer : customers) {
            if (!customer.getCustomerName().equalsIgnoreCase("bob")) {
                customers.remove(customer);
            }
        }

        customers = customers.stream().filter((customer) ->  customer.getCustomerName().equalsIgnoreCase("bob")).collect(Collectors.toList());
    }

}
