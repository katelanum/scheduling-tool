package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController {
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
    public ComboBox<Countries> countryComboBox;
    public ComboBox<FirstLevelDivisions> stateComboBox;
    public Text stateText;
    public Button customerSaveButton;
    public Stage customerStage;
    public TableView<Customer> customerTableView;
    public Button customerCancelButton;
    public TableColumn<Customer, Integer> customerIDColumn;
    public TableColumn<Customer, String> customerNameColumn;
    public TableColumn<Customer, String> customerAddressColumn;
    public TableColumn<Customer, String> customerZipColumn;
    public TableColumn<Customer, String> customerCountryColumn;
    public TableColumn<Customer, String> customerPhoneNumber;
    public TableColumn<Customer, String> customerStateColumn;
    private String addDeleteModStatus = "add";
    private final Alert customerScreenAlert = new Alert(Alert.AlertType.WARNING);
    private static final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private int custId;
    private Customer transferCust = new Customer();
    private static final Customer tempCust = new Customer();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private final ObservableList<FirstLevelDivisions> fldList = FXCollections.observableArrayList();
    private final ObservableList<Countries> countryList = FXCollections.observableArrayList();
    private AppointmentController appointmentController;
    private LoginController loginController;
    private ScheduleController scheduleController;
    private int countryId;

    public void customerAddClick(ActionEvent actionEvent) {
        addDeleteModStatus = "add";
    }

    public void initialize() throws SQLException {
        customerList.clear();
        Database.initializeCustomerList(customerList);
        customerTableView.setPlaceholder(new Label(languageBundle.getString("noCust")));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerStreetAddress"));
        customerZipColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("customerStateProv"));
        customerTableView.setItems(customerList);
        initializeCountryCombo();
    }

    public void initializeCountryCombo() throws SQLException {
        countryList.clear();
        Database.getCountry(countryList);
        countryComboBox.setItems(countryList);
    }

    public void initializeStateCombo() throws SQLException {
        fldList.clear();
        Database.getFLD(fldList,countryId);
        stateComboBox.setItems(fldList);
    }

    private void autoPopulate() throws SQLException {
        phoneTextBox.setText(tempCust.getCustomerPhone());
        customerIdTextBox.setText(String.valueOf(tempCust.getCustomerId()));
        addressTextBox.setText(tempCust.getCustomerStreetAddress());
        nameTextBox.setText(tempCust.getCustomerName());
        zipTextBox.setText(tempCust.getCustomerPostal());
        int provId = tempCust.getDivisionId();
        FirstLevelDivisions tempDiv = Database.getDivision(provId);
        stateComboBox.setValue(tempDiv);
        countryComboBox.setValue(Database.getSingleCountry(tempDiv.getCountryId()));
    }

    public boolean tempCustCreated() {
        if (nameTextBox.getText().isEmpty() || phoneTextBox.getText().isEmpty() ||
                zipTextBox.getText().isEmpty() || stateComboBox.getValue() == null || addressTextBox.getText().isEmpty() || countryComboBox.getValue() == null) {
            return false;
        }
        if (!nameTextBox.getText().isEmpty()) {
            tempCust.setCustomerName(nameTextBox.getText());
        }
        if (!customerIdTextBox.getText().isEmpty()) {
            tempCust.setCustomerId(Integer.parseInt(customerIdTextBox.getText()));
        }
        if (!phoneTextBox.getText().isEmpty()) {
            tempCust.setCustomerPhone(phoneTextBox.getText());
        }
        if (!zipTextBox.getText().isEmpty()) {
            tempCust.setCustomerPostal(zipTextBox.getText());
        }
        if (stateComboBox.getValue() != null) {
            tempCust.setCustomerStateProv(stateComboBox.getValue().getDivisionName());
            tempCust.setDivisionId(stateComboBox.getValue().getDivisionId());
        }
        if (!addressTextBox.getText().isEmpty()) {
            tempCust.setCustomerStreetAddress(addressTextBox.getText());
        }
        if (countryComboBox.getValue() != null) {
            tempCust.setCustomerCountry(countryComboBox.getValue().getCountryName());
        }
        return true;
    }

    public void customerUpdateClick(ActionEvent actionEvent) throws SQLException {
        addDeleteModStatus = "update";
        Customer selectedCust = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCust != null) {
            custId = selectedCust.getCustomerId();
            transferCust = Database.getCustomer(custId);
            tempCust.setDivisionId(transferCust.getDivisionId());
            tempCust.setCustomerStateProv(transferCust.getCustomerStateProv());
            tempCust.setCustomerPhone(transferCust.getCustomerPhone());
            tempCust.setCustomerPostal(transferCust.getCustomerPostal());
            tempCust.setCustomerStreetAddress(transferCust.getCustomerStreetAddress());
            tempCust.setCustomerName(transferCust.getCustomerName());
            tempCust.setCustomerId(transferCust.getCustomerId());
            tempCust.setCustomerCountry(transferCust.getCustomerCountry());
            autoPopulate();
        }
    }

    public void customerDeleteClick(ActionEvent actionEvent) throws SQLException {
        addDeleteModStatus = "delete";
        Customer selectedCust = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCust != null) {
            custId = selectedCust.getCustomerId();
            transferCust = Database.getCustomer(custId);
            tempCust.setDivisionId(transferCust.getDivisionId());
            tempCust.setCustomerPhone(transferCust.getCustomerPhone());
            tempCust.setCustomerPostal(transferCust.getCustomerPostal());
            tempCust.setCustomerStreetAddress(transferCust.getCustomerStreetAddress());
            tempCust.setCustomerName(transferCust.getCustomerName());
            tempCust.setCustomerId(transferCust.getCustomerId());
            tempCust.setCustomerStateProv(transferCust.getCustomerStateProv());
            tempCust.setCustomerCountry(transferCust.getCustomerCountry());
            autoPopulate();
        }
    }

    public void countryComboSelect(ActionEvent actionEvent) throws SQLException {
        Countries selectedCountry = countryComboBox.getValue();
        countryId = selectedCountry.getCountryID();
        initializeStateCombo();
    }

    public void stateComboSelect(ActionEvent actionEvent) {
    }

    public void customerSaveClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (addDeleteModStatus.equalsIgnoreCase("add")) {
            if (tempCustCreated()) {
                Database.addCustomer(tempCust);
                scheduleController.refreshAllApps();
                Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
                currentStage.close();
            }
            else {
                customerScreenAlert.setTitle(languageBundle.getString("invalidCustTitle"));
                customerScreenAlert.setHeaderText(languageBundle.getString("invalidCustHeader"));
                customerScreenAlert.setContentText(languageBundle.getString("custEmptyBoxContent"));
                customerScreenAlert.showAndWait();
            }
        }
        else if(addDeleteModStatus.equalsIgnoreCase("delete")) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle(languageBundle.getString("delCustTitle"));
            confirmationAlert.setHeaderText(languageBundle.getString("delCustHeader"));
            confirmationAlert.setContentText(languageBundle.getString("delCustContent"));
            Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
            if (confirmation.get() == ButtonType.OK) {
                Database.deleteCustomerAppointment(custId);
                Database.deleteCustomer(custId);
                scheduleController.refreshAllApps();
                Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
                currentStage.close();
            }
        }
        else if(addDeleteModStatus.equalsIgnoreCase("update")) {
            if (tempCustCreated()) {
                Database.updateCustomer(tempCust);
                scheduleController.refreshAllApps();
                Stage currentStage = (Stage) customerSaveButton.getScene().getWindow();
                currentStage.close();
            }
            else {
                customerScreenAlert.setTitle(languageBundle.getString("invalidCustTitle"));
                customerScreenAlert.setHeaderText(languageBundle.getString("invalidCustHeader"));
                customerScreenAlert.setContentText(languageBundle.getString("custEmptyBoxContent"));
                customerScreenAlert.showAndWait();
            }
        }
        else {
            customerScreenAlert.setTitle(languageBundle.getString("unexpErrorTitle"));
            customerScreenAlert.setHeaderText(languageBundle.getString("unexpErrorHeader"));
            customerScreenAlert.setContentText(languageBundle.getString("unexpErrorContent"));
            customerScreenAlert.showAndWait();
        }
        customerList.clear();
        Database.initializeCustomerList(customerList);
    }

    public void customerCancelClick(ActionEvent actionEvent) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(languageBundle.getString("cancel"));
        confirmationAlert.setHeaderText(languageBundle.getString("cancelActions"));
        confirmationAlert.setContentText(languageBundle.getString("continue"));
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if (confirmation.get() == ButtonType.OK) {
            Stage currentStage = (Stage) customerCancelButton.getScene().getWindow();
            currentStage.close();
        }
    }

    public void setAppointmentController(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setScheduleController(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }
}
