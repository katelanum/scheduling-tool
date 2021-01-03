package project;
// accept user name and password
//determine location and display as "loginLocationText"
//translate to French if location demands
//error control messages for login problems

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class LoginController {

    public Stage logInStage;
    public Scene logInScene;
    public AnchorPane logInPane;
    public TextField usernameBox;
    public TextField passwordBox;
    public Text logInTitle;
    public Text usernameTitle;
    public Text passwordTitle;
    public Text loginLocationText;
    public Button logInButton;

    private final Alert loginScreenAlert = new Alert(Alert.AlertType.WARNING);

    private HashMap<String, String> loginMap = new HashMap<>();


    public void logIn(ActionEvent actionEvent) throws IOException {
        loginMap.put("test","test");
        String userId = usernameBox.getText();
        String pass = passwordBox.getText();
        if(!loginMap.containsKey(userId)) {
            // error message for id not existing
            loginScreenAlert.setTitle("Invalid Login");
            loginScreenAlert.setHeaderText("Invalid User ID");
            loginScreenAlert.setContentText("The user Id entered does not exist");
            loginScreenAlert.showAndWait();
            return;
        }
        else if (!loginMap.get(userId).equals(pass)) {
            //incorrect password error
            loginScreenAlert.setTitle("Invalid Login");
            loginScreenAlert.setHeaderText("Incorrect Password");
            loginScreenAlert.setContentText("The password you entered is incorrect");
            loginScreenAlert.showAndWait();
        }
        else {
            Stage currentStage = (Stage) logInButton.getScene().getWindow();
            currentStage.close();
            Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"));
            loader.show();
        }
    }
}

