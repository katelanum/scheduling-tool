package project;
// accept user name and password
//determine location and display as "loginLocationText"
//translate to French if location demands
//error control messages for login problems

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private ZoneId location;
    private String locationFormatted;
    private ResourceBundle languageBundle;
    private final Alert loginScreenAlert = new Alert(Alert.AlertType.WARNING);
    private AppointmentController appointmentController;
    private CustomerController customerController;
    private ScheduleController scheduleController;
    private HashMap<String, String> loginMap = new HashMap<>();
    ZonedDateTime currentDT = ZonedDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    ObservableList<Appointment> appList = FXCollections.observableArrayList();

    public Text setLoginLocationText() {
        location = ZoneId.systemDefault();
        loginLocationText.setText(String.valueOf(location));
        return loginLocationText;
    }

    public void initialize() throws SQLException {
        //getUserLocation();
        Database.popLogInHash(loginMap);
        languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        setLoginLocationText();
        Database.initializeAppointmentList(appList);
    }

    public void logIn(ActionEvent actionEvent) throws IOException {
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
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            for (int i = 0; i < appList.size(); i++) {
                ZonedDateTime fifteenMin = currentDT.plusMinutes(15);
                if (appList.get(i).getEnd().isAfter(currentDT) && appList.get(i).getStart().isBefore(fifteenMin)) {
                    String fifteenAppContent = languageBundle.getString("appointmentIDWords") + ": " + String.valueOf(appList.get(i).getAppointmentId()) +
                            languageBundle.getString("start") + ": " + appList.get(i).getStart().format(format) +
                            languageBundle.getString("end") + ": " + appList.get(i).getEnd().format(format);
                    confirmationAlert.setHeaderText(languageBundle.getString("fifteenAppHeader"));
                    confirmationAlert.setContentText(fifteenAppContent);
                    Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
                    if (confirmation.get() == ButtonType.OK) {
                        Stage currentStage = (Stage) logInButton.getScene().getWindow();
                        currentStage.close();
                        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
                        Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
                        LocalTime current = LocalTime.now();
                        Database.timeCheck(current);
                        loader.show();
                    }
                }
            }
            confirmationAlert.setHeaderText(languageBundle.getString("noFifteenAppHeader"));
            Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
            if (confirmation.get() == ButtonType.OK) {
                Stage currentStage = (Stage) logInButton.getScene().getWindow();
                currentStage.close();
                ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
                Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
                LocalTime current = LocalTime.now();
                Database.timeCheck(current);
                loader.show();
            }
        }
    }

    public void setAppointmentController(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setScheduleController(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }

    public void keyRelease(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
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
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                for (int i = 0; i < appList.size(); i++) {
                    ZonedDateTime fifteenMin = currentDT.plusMinutes(15);
                    if (appList.get(i).getEnd().isAfter(currentDT) && appList.get(i).getStart().isBefore(fifteenMin)) {
                        String fifteenAppContent = languageBundle.getString("appointmentIDWords") + ": " + String.valueOf(appList.get(i).getAppointmentId()) +
                                languageBundle.getString("start") + ": " + appList.get(i).getStart().format(format) +
                                languageBundle.getString("end") + ": " + appList.get(i).getEnd().format(format);
                        confirmationAlert.setHeaderText(languageBundle.getString("fifteenAppHeader"));
                        confirmationAlert.setContentText(fifteenAppContent);
                        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
                        if (confirmation.get() == ButtonType.OK) {
                            Stage currentStage = (Stage) logInButton.getScene().getWindow();
                            currentStage.close();
                            ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
                            Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
                            LocalTime current = LocalTime.now();
                            Database.timeCheck(current);
                            loader.show();
                        }
                    }
                }
                confirmationAlert.setHeaderText(languageBundle.getString("noFifteenAppHeader"));
                Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
                if (confirmation.get() == ButtonType.OK) {
                    Stage currentStage = (Stage) logInButton.getScene().getWindow();
                    currentStage.close();
                    ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
                    Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
                    LocalTime current = LocalTime.now();
                    Database.timeCheck(current);
                    loader.show();
                }
            }
        }
    }
}

