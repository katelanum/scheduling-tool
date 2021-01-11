package project;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentController {
    public Stage appointmentStage;
    public Scene appointmentScene;
    public AnchorPane appointmentPane;
    public RadioButton appointmentAddRadio;
    public ToggleGroup appointmentRadioGroup;
    public RadioButton appointmentUpdateRadio;
    public RadioButton appointmentDeleteRadio;
    public Text appointmentsTitle;
    public TextField appointmentTitleTextBox;
    public Text appointmentTitleText;
    public Text descriptionText;
    public TextField addressTextBox;
    public TextField locationTextBox;
    public Text locationText;
    public Text customerText;
    public TextField customerTextBox;
    public TextField appointmentIdTextBox;
    public Text appointmentIdText;
    public Text contactText;
    public ComboBox contactComboBox;
    public Text startDateText;
    public DatePicker startDate;
    public DatePicker endDate;
    public Text endDateText;
    public Button appointmentSaveButton;
    private final Alert appointmentScreenAlert = new Alert(Alert.AlertType.WARNING);
    String addDeleteModStatus = "add";
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
    private boolean isValidTime = false;
    private Date appointmentStart = new Date();
    private Date appointmentEnd = new Date();
    private Appointment tempApp;
    private Appointment currentApp;
    private int appID;

    public void appointmentAddClick(ActionEvent actionEvent) {
        addDeleteModStatus = "add";
    }

    public void appointmentUpdateClick(ActionEvent actionEvent) {
        addDeleteModStatus = "update";
        appID = Integer.parseInt(appointmentIdTextBox.getText());
        tempApp = Database.getAppointment(appID);
        appointmentIdTextBox.setText(tempApp.getTitle());
        //addressTextBox.setText(tempApp.get);
        locationTextBox.setText(tempApp.getLocation());
        customerTextBox.setText(String.valueOf(tempApp.getCustomerId()));
    }

    private void setCurrentApp(int appointmentId) {
        //get title from database
        //currentApp.setTitle(appointmentTitleTextBox.getText());
        currentApp.setAppointmentId(Integer.parseInt(appointmentIdTextBox.getText()));
        // get contact ID from database
        //currentApp.setContactId(Integer.parseInt(contactComboBox.toString()));
        // get customer ID from database
        //currentApp.setCustomerId(customerTextBox);
        // get description from database
        //currentApp.setDescription();
        // get end from database
        //currentApp.setEnd(appointmentEnd);
        // get location from database
        //currentApp.setLocation(locationTextBox.getText());
        // get type from database
        //tempApp.setType();
        // get start from database
        //currentApp.setStart(appointmentStart);
        // pull customer ID from database
        //currentApp.setCustomerId();
    }

    public void tempAppMaker() {
        tempApp.setTitle(appointmentTitleTextBox.getText());
        tempApp.setAppointmentId(Integer.parseInt(appointmentIdTextBox.getText()));
        // get contact ID from database
        //tempApp.setContactId(Integer.parseInt(contactComboBox.toString()));
        // get customer ID from database
        //tempApp.setCustomerId(customerTextBox);
        //tempApp.setDescription();
        tempApp.setEnd(appointmentEnd);
        tempApp.setLocation(locationTextBox.getText());
        //tempApp.setType();
        tempApp.setStart(appointmentStart);
        // pull customer ID from database
        //tempApp.setCustomerId();
    }

    public void appointmentDeleteClick(ActionEvent actionEvent) {
        addDeleteModStatus = "delete";
        appID = Integer.parseInt(appointmentIdTextBox.getText());
        tempApp = Database.getAppointment(appID);
    }

    public void contactComboSelect(ActionEvent actionEvent) {
    }

    private void timeCheck(Date startTime, Date endDate) {
        // check start and end against business hours
        if (startTime.getHours() < 8 || endDate.getHours() > 22) {
            appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("notWorkHoursHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("notWorkHoursContent"));
            appointmentScreenAlert.showAndWait();
            isValidTime = false;
        }
//        else if (!startTime.before() && !startTime.after() ) {
//            appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
//            appointmentScreenAlert.setHeaderText(languageBundle.getString("overbookHeader"));
//            appointmentScreenAlert.setContentText(languageBundle.getString("overbookContent"));
//            appointmentScreenAlert.showAndWait();
//            isValidTime = false;
//        }
        else {
            isValidTime = true;
        }

    }

    public void appointmentSaveClick(ActionEvent actionEvent) {
        appointmentStart = java.sql.Date.valueOf(startDate.getValue());
        appointmentEnd = java.sql.Date.valueOf(endDate.getValue());
        tempAppMaker();
        if (addDeleteModStatus.equalsIgnoreCase("add")) {
            timeCheck(appointmentStart,appointmentEnd);
            if (isValidTime) {
                Database.addAppointment(tempApp);
                Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
                currentStage.close();
            }
        }
        else if(addDeleteModStatus.equalsIgnoreCase("delete")) {
            Database.deleteAppointment(appID);
            Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("update")) {
            timeCheck(appointmentStart,appointmentEnd);
            if (isValidTime) {
                Database.deleteAppointment(appID);
                Database.updateAppointment(tempApp);
                Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
                currentStage.close();
            }
        }
        else {
            appointmentScreenAlert.setTitle(languageBundle.getString("unexpErrorTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("unexpErrorHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("unexpErrorContent"));
            appointmentScreenAlert.showAndWait();
        }
        tempApp = null;
    }

    public void appointmentStartSelect(ActionEvent actionEvent) {
    }

    public void appointmentEndSelect(ActionEvent actionEvent) {
    }
}
