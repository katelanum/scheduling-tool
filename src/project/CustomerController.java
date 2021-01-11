package project;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Locale;
import java.util.Optional;
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
    public Stage customerStage;
    public TableView customerTableView;
    public Button customerCancelButton;
    String addDeleteModStatus = "add";
    private final Alert customerScreenAlert = new Alert(Alert.AlertType.WARNING);
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
    private int custId;
    private Customer tempCust;
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    public void customerAddClick(ActionEvent actionEvent) {
        addDeleteModStatus = "add";
    }

    public void tempCustCreator() {
        //tempCust.setCustomerCountry(countryComboBox);
        tempCust.setCustomerId(Integer.parseInt(customerIdTextBox.getText()));
        tempCust.setCustomerName(nameTextBox.getText());
        tempCust.setCustomerPhone(phoneTextBox.getText());
        tempCust.setCustomerPostal(zipTextBox.getText());
        //empCust.setCustomerStateProv(stateComboBox);
        tempCust.setCustomerStreetAddress(addressTextBox.getText());
    }

    public void customerUpdateClick(ActionEvent actionEvent) {
        addDeleteModStatus = "update";
        custId = Integer.parseInt(customerIdTextBox.getText());
        tempCust = Database.getCustomer(custId);
    }

    public void customerDeleteClick(ActionEvent actionEvent) {
        addDeleteModStatus = "delete";
        custId = Integer.parseInt(customerIdTextBox.getText());
        tempCust = Database.getCustomer(custId);
    }

    public void countryComboSelect(ActionEvent actionEvent) {
    }

    public void stateComboSelect(ActionEvent actionEvent) {
    }

    public void customerSaveClick(ActionEvent actionEvent) {
        if (addDeleteModStatus.equalsIgnoreCase("add")) {
            Database.addCustomer(tempCust);
            Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("delete")) {
            Database.deleteCustomer(custId);
            Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else if(addDeleteModStatus.equalsIgnoreCase("update")) {
            Database.updateCustomer(tempCust);
            Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
            currentStage.close();
        }
        else {
            customerScreenAlert.setTitle(languageBundle.getString("unexpErrorTitle"));
            customerScreenAlert.setHeaderText(languageBundle.getString("unexpErrorHeader"));
            customerScreenAlert.setContentText(languageBundle.getString("unexpErrorContent"));
            customerScreenAlert.showAndWait();
        }
        tempCust = null;
    }

    public void customerCancelClick(ActionEvent actionEvent) {
        confirmationAlert.setTitle(languageBundle.getString("cancel"));
        confirmationAlert.setHeaderText(languageBundle.getString("cancelActions"));
        confirmationAlert.setContentText(languageBundle.getString("continue"));
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if (confirmation.get() == ButtonType.OK) {
            Stage currentStage = (Stage) customerCancelButton.getScene().getWindow();
            currentStage.close();
        }
    }
}
