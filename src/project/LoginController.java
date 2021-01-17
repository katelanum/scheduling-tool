package project;

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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
    private ResourceBundle languageBundle;
    private final Alert loginScreenAlert = new Alert(Alert.AlertType.WARNING);
    private AppointmentController appointmentController;
    private CustomerController customerController;
    private ScheduleController scheduleController;
    private final HashMap<String, String> loginMap = new HashMap<>();
    ZonedDateTime currentDT = ZonedDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    ObservableList<Appointment> appList = FXCollections.observableArrayList();
    Logger logInFile = Logger.getLogger("login_activity.txt");

    public Text setLoginLocationText() {
        ZoneId location = ZoneId.systemDefault();
        loginLocationText.setText(String.valueOf(location));
        return loginLocationText;
    }

    public void initialize() throws SQLException {
        Database.popLogInHash(loginMap);
        languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        setLoginLocationText();
        Database.initializeAppointmentList(appList);
        try {
            FileHandler handler = new FileHandler("login_activity.txt",true);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            logInFile.addHandler(handler);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void logIn(ActionEvent actionEvent) throws IOException {
        String userId = usernameBox.getText();
        String pass = passwordBox.getText();
        if(!loginMap.containsKey(userId)) {
            logLoginFailure();
            loginScreenAlert.setTitle(languageBundle.getString("invalidLoginErrorTitle"));
            loginScreenAlert.setHeaderText(languageBundle.getString("invalidLoginErrorHeader"));
            loginScreenAlert.setContentText(languageBundle.getString("invalidLoginErrorContent"));
            loginScreenAlert.showAndWait();
        }
        else if (!loginMap.get(userId).equals(pass)) {
            logLoginFailure();
            loginScreenAlert.setTitle(languageBundle.getString("invalidPassErrorTitle"));
            loginScreenAlert.setHeaderText(languageBundle.getString("invalidPassErrorHeader"));
            loginScreenAlert.setContentText(languageBundle.getString("invalidPassErrorContent"));
            loginScreenAlert.showAndWait();
        }
        else {
            logLoginSuccess();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            ObservableList<String> fifteenMinList = FXCollections.observableArrayList();
            for (Appointment appointment : appList) {
                ZonedDateTime fifteenMin = currentDT.plusMinutes(15);
                if (appointment.getEnd().isAfter(currentDT) && appointment.getStart().isBefore(fifteenMin)) {
                    String fifteenAppContent = languageBundle.getString("appointmentIDWords") + ": " + String.valueOf(appointment.getAppointmentId()) +
                            languageBundle.getString("start") + ": " + appointment.getStart().format(format) +
                            languageBundle.getString("end") + ": " + appointment.getEnd().format(format);
                    fifteenMinList.add(fifteenAppContent);

                    }
                }
            if (fifteenMinList.isEmpty()) {
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
            else {
                confirmationAlert.setHeaderText(languageBundle.getString("fifteenAppHeader"));
                for (int i = 0; i < fifteenMinList.size(); i++) {
                    String content = fifteenMinList.get(i) + "/n";
                    confirmationAlert.setContentText(content);
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

    public void setAppointmentController(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
    }

    private void logLoginFailure() {
        String logString = languageBundle.getString("loginFailure") + ": " + currentDT.format(format);
        logInFile.log(Level.INFO, logString);
    }

    private void logLoginSuccess() {
        String logString = languageBundle.getString("loginSuccess") + ": " + currentDT.format(format);
        logInFile.log(Level.INFO, logString);
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
                logLoginFailure();
                loginScreenAlert.setTitle(languageBundle.getString("invalidLoginErrorTitle"));
                loginScreenAlert.setHeaderText(languageBundle.getString("invalidLoginErrorHeader"));
                loginScreenAlert.setContentText(languageBundle.getString("invalidLoginErrorContent"));
                loginScreenAlert.showAndWait();
            }
            else if (!loginMap.get(userId).equals(pass)) {
                logLoginFailure();
                loginScreenAlert.setTitle(languageBundle.getString("invalidPassErrorTitle"));
                loginScreenAlert.setHeaderText(languageBundle.getString("invalidPassErrorHeader"));
                loginScreenAlert.setContentText(languageBundle.getString("invalidPassErrorContent"));
                loginScreenAlert.showAndWait();
            }
            else {
                logLoginSuccess();
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                for (Appointment appointment : appList) {
                    ZonedDateTime fifteenMin = currentDT.plusMinutes(15);
                    if (appointment.getEnd().isAfter(currentDT) && appointment.getStart().isBefore(fifteenMin)) {
                        String fifteenAppContent = languageBundle.getString("appointmentIDWords") + ": " + String.valueOf(appointment.getAppointmentId()) + " " +
                                languageBundle.getString("start") + ": " + appointment.getStart().format(format) + " " +
                                languageBundle.getString("end") + ": " + appointment.getEnd().format(format);
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

