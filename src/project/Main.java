package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;

//colors: https://material.io/resources/color/#!/?view.left=0&view.right=1&primary.color=1A237E&secondary.color=761a7e

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        primaryStage = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"), languageBundle);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        try {
            Database.initializeDB();
        } catch (SQLSyntaxErrorException created) {
            System.err.println(created);
        }
        launch(args);
    }
}
