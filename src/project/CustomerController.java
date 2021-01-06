package project;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class CustomerController {
    public Stage LogInStage;
    public Scene CustomerScene;
    public AnchorPane customerPane;
    public RadioButton customerAddRadio;
    public ToggleGroup customerRadioGroup;
    public RadioButton customerUpdateRadio;
    public RadioButton customerDeleteRadio;
    public Text customerRecordTitle;
    public TextField nameTextBox;
    public Text nameText;
    public Text addressText;
    public TextField addressTextBox;
    public TextField zipTextBox;
    public Text zipText;
    public Text phoneText;
    public TextField phoneTextBox;
    public TextField customerIdTextBox;
    public Text customerIdText;
    public Text countryText;
    public ComboBox countryComboBox;
    public ComboBox stateComboBox;
    public Text stateText;
    public Button customerSaveButton;
    String addDeleteModStatus = "add";
    private final Alert customerScreenAlert = new Alert(Alert.AlertType.WARNING);
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));

    public void customerAddClick(ActionEvent actionEvent) {
        addDeleteModStatus = "add";
    }

    public void customerUpdateClick(ActionEvent actionEvent) {
        addDeleteModStatus = "update";
    }

    public void customerDeleteClick(ActionEvent actionEvent) {
        addDeleteModStatus = "delete";
    }

    public void countryComboSelect(ActionEvent actionEvent) {
    }

    public void stateComboSelect(ActionEvent actionEvent) {
    }

    public void customerSaveClick(ActionEvent actionEvent) {
        if (addDeleteModStatus.equalsIgnoreCase("add")) {
            Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("delete")) {
            Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("update")) {
            Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else {
            customerScreenAlert.setTitle(languageBundle.getString("unexpErrorTitle"));
            customerScreenAlert.setHeaderText(languageBundle.getString("unexpErrorHeader"));
            customerScreenAlert.setContentText(languageBundle.getString("unexpErrorContent"));
            customerScreenAlert.showAndWait();
        }
    }
}
