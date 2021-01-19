package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;

/**
 * Main class simply starts the application by loading the LoginScreen, the language resource bundle, and initializing
 * the database
 */
public class Main extends Application {
    /**
     *Sets the resource bundle for the translation of the application and starts the LoginScreen
     *
     * @param primaryStage the main screen of the application
     *
     * @throws Exception if there is a problem loading the resource bundle or LoginScreen
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        primaryStage = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"), languageBundle);
        primaryStage.show();
    }

    /**
     *  Initializes the database
     *
     * @param args
     *
     * @throws SQLException if there is a problem initializing the database
     */
    public static void main(String[] args) throws SQLException {
        try {
            Database.initializeDB();
        } catch (SQLSyntaxErrorException created) {
            System.err.println(created);
        }
        launch(args);
    }
}
