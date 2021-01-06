package project;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    public void appointmentAddClick(ActionEvent actionEvent) {
        addDeleteModStatus = "add";
    }

    public void appointmentUpdateClick(ActionEvent actionEvent) {
        addDeleteModStatus = "update";
    }

    public void appointmentDeleteClick(ActionEvent actionEvent) {
        addDeleteModStatus = "delete";
    }

    public void contactComboSelect(ActionEvent actionEvent) {
    }

    public void appointmentSaveClick(ActionEvent actionEvent) {
        if (addDeleteModStatus.equalsIgnoreCase("add")) {
            Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("delete")) {
            Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("update")) {
            Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else {
            appointmentScreenAlert.setTitle(languageBundle.getString("unexpErrorTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("unexpErrorHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("unexpErrorContent"));
            appointmentScreenAlert.showAndWait();
        }
    }

    public void appointmentStartSelect(ActionEvent actionEvent) {
    }

    public void appointmentEndSelect(ActionEvent actionEvent) {
    }
}
