package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

//colors: https://material.io/resources/color/#!/?view.left=0&view.right=1&primary.color=1A237E&secondary.color=761a7e

public class Main extends Application {
    private final Alert mainScreenAlert = new Alert(Alert.AlertType.WARNING);



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        primaryStage.show();
    }

    // md5 hash password (ideally), can't do due to class project restrictions


    public static void main(String[] args) {
        launch(args);
    }


}
