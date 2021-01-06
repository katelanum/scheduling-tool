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
import java.util.*;

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
    private Locale location;
    private ResourceBundle languageBundle;

    private final Alert loginScreenAlert = new Alert(Alert.AlertType.WARNING);

    private HashMap<String, String> loginMap = new HashMap<>();

//    public void getUserLocation() {
//        location = Locale.getDefault();
//    }

    public Text setLoginLocationText() {
        location = Locale.getDefault();
        String country = String.valueOf(location.getCountry());
        loginLocationText.setText(country);
        return loginLocationText;
    }

    public void initialize(){
        //getUserLocation();
        languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
        setLoginLocationText();
    }

    public void logIn(ActionEvent actionEvent) throws IOException {
        loginMap.put("test","test");
        String userId = usernameBox.getText();
        String pass = passwordBox.getText();
        if(!loginMap.containsKey(userId)) {
            // error message for id not existing
            loginScreenAlert.setTitle(languageBundle.getString("invalidLoginErrorTitle"));
            loginScreenAlert.setHeaderText(languageBundle.getString("invalidLoginErrorHeader"));
            loginScreenAlert.setContentText(languageBundle.getString("invalidLoginErrorContent"));
            loginScreenAlert.showAndWait();
            return;
        }
        else if (!loginMap.get(userId).equals(pass)) {
            //incorrect password error
            loginScreenAlert.setTitle(languageBundle.getString("invalidPassErrorTitle"));
            loginScreenAlert.setHeaderText(languageBundle.getString("invalidPassErrorHeader"));
            loginScreenAlert.setContentText(languageBundle.getString("invalidPassErrorContent"));
            loginScreenAlert.showAndWait();
        }
        else {
            Stage currentStage = (Stage) logInButton.getScene().getWindow();
            currentStage.close();
            ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
            Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
            loader.show();
        }
    }
}

