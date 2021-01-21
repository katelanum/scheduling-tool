package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 * LoginController acts as the interface between the database and LoginScreen.fxml
 *
 * @author katelanum
 */
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
    private final HashMap<String, String> loginMap = new HashMap<>();
    private final ZonedDateTime currentDT = ZonedDateTime.now();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    private final ObservableList<Appointment> appList = FXCollections.observableArrayList();
    private final Logger logInFile = Logger.getLogger("login_activity.txt");

    /**
     * This function gets the information on the user's ZoneId from their computer and then sets the loginLocationText
     * to show the user's location
     */
    public void setLoginLocationText() {
        ZoneId location = ZoneId.systemDefault();
        loginLocationText.setText(String.valueOf(location));
    }

    /**
     *  This populates the loginMap from the users in the database, it also populates the appList from the appointments
     *  in the database. The login_activity.txt file is also set up for the information collected later to be appended
     *  to it.
     *
     * @throws SQLException if there is a problem getting either the appointment information to populate appList or the
     * user information to populate loginMap from the database
     */
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

    /**
     *  Upon the user clicking on the logInButton, checks that the input from the usernameBox is in the loginMap, if
     *  not, then the user receives an error message. If the username input is in the loginMap, then it checks that the
     *  value for the password for that username in the loginMap matches the password input from the passwordBox. If
     *  these passwords do not match, the user gets an error message.  If both the userename and the password match the
     *  pair from the loginMap, then the user is alerted if there are any appointments within 15 minutes of them
     *  logging in. This is from the information pulled from the appList and includes any in progress appointments. If
     *  there are no appointments within 15 minutes of the user logging in, they are alerted. The ScheduleScreen is
     *  then loaded and the LoginScreen is closed.
     *
     * @throws IOException if there is a problem dealing with the mouse clicks on either the login button or the
     * confirmation button
     *
     * @throws SQLException if getting either the user or appointment information from the database causes an error
     */
    public void logIn() throws IOException, SQLException {
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
                    String fifteenAppContent = languageBundle.getString("appointmentIDWords") + ": " + appointment.getAppointmentId() +
                            " " + languageBundle.getString("start") + ": " + appointment.getStart().format(format) +
                            " " + languageBundle.getString("end") + ": " + appointment.getEnd().format(format);
                    fifteenMinList.add(fifteenAppContent);
                    }
                }
            if (fifteenMinList.isEmpty()) {
                confirmationAlert.setHeaderText(languageBundle.getString("noFifteenAppHeader"));
                Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
                if (confirmation.get() == ButtonType.OK) {
                    Stage currentStage = (Stage) logInButton.getScene().getWindow();
                    currentStage.close();
                    ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources",
                            Locale.getDefault());
                    Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
                    LocalTime current = LocalTime.now();
                    Database.timeCheck(current);
                    loader.show();
                }
            }
            else {
                StringBuilder listContent = new StringBuilder();
                confirmationAlert.setHeaderText(languageBundle.getString("fifteenAppHeader"));
                for (String s : fifteenMinList) {
                    listContent.append(s).append("\n");
                }
                confirmationAlert.setContentText(String.valueOf(listContent));
                Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
                if (confirmation.get() == ButtonType.OK) {
                    Stage currentStage = (Stage) logInButton.getScene().getWindow();
                    currentStage.close();
                    ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources",
                            Locale.getDefault());
                    Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"), languageBundle);
                    LocalTime current = LocalTime.now();
                    Database.timeCheck(current);
                    loader.show();
                }
            }
        }
    }

    /**
     * Logs that there was a failed login attempt, including time information
     */
    private void logLoginFailure() {
        String logString = languageBundle.getString("loginFailure") + ": " + "Username Input: " + usernameBox.getText() + "\t" +
                "Password Input: " + passwordBox.getText() + "\t" + "Date and Time: " + currentDT.format(format) +
                "\n";
        logInFile.log(Level.INFO, logString);
    }

    /**
     * Logs that there was a successful login attempt
     *
     * @throws SQLException if pulling the user Id from the database fails
     */
    private void logLoginSuccess() throws SQLException {
        String logString = languageBundle.getString("loginSuccess") + ": " + "User ID: " +
                Database.getUserId(usernameBox.getText()) + "\t" + "Username: " + usernameBox.getText() + "\t" +
                "Date and Time: " + currentDT.format(format) + "\n";
        logInFile.log(Level.INFO, logString);
    }

    /**
     *  Upon the user releasing the Enter button, checks that the input from the usernameBox is in the loginMap, if not,
     *  then the user receives an error message. If the username input is in the loginMap, then it checks that the value
     *  for the password for that username in the loginMap matches the password input from the passwordBox. If these
     *  passwords do not match, the user gets an error message.  If both the userename and the password match the pair
     *  from the loginMap, then the user is alerted if there are any appointments within 15 minutes of them logging in.
     *  This is from the information pulled from the appList and includes any in progress appointments. If there are no
     *  appointments within 15 minutes of the user logging in, they are alerted. The ScheduleScreen is then loaded and
     *  the LoginScreen is closed.
     *
     * @param keyEvent the Enter key being pressed
     *
     * @throws IOException if getting the key press information fails
     *
     * @throws SQLException if getting the user or appointment information from the database causes an error
     */
    public void keyRelease(KeyEvent keyEvent) throws IOException, SQLException {
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
                        String fifteenAppContent = languageBundle.getString("appointmentIDWords") + ": " + appointment.getAppointmentId() + " " +
                                languageBundle.getString("start") + ": " + appointment.getStart().format(format) + " " +
                                languageBundle.getString("end") + ": " + appointment.getEnd().format(format);
                        confirmationAlert.setHeaderText(languageBundle.getString("fifteenAppHeader"));
                        confirmationAlert.setContentText(fifteenAppContent);
                        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
                        if (confirmation.get() == ButtonType.OK) {
                            Stage currentStage = (Stage) logInButton.getScene().getWindow();
                            currentStage.close();
                            ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources",
                                    Locale.getDefault());
                            Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"),
                                    languageBundle);
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
                    ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources",
                            Locale.getDefault());
                    Stage loader = FXMLLoader.load(getClass().getResource("ScheduleScreen.fxml"),
                            languageBundle);
                    LocalTime current = LocalTime.now();
                    Database.timeCheck(current);
                    loader.show();
                }
            }
        }
    }
}

