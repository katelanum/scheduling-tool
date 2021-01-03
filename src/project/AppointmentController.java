package project;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
            appointmentScreenAlert.setTitle("Unexpected Error");
            appointmentScreenAlert.setHeaderText("Unknown Error");
            appointmentScreenAlert.setContentText("Something did not go s expected, please close screen and retry");
            appointmentScreenAlert.showAndWait();
        }
    }

    public void appointmentStartSelect(ActionEvent actionEvent) {
    }

    public void appointmentEndSelect(ActionEvent actionEvent) {
    }
}
