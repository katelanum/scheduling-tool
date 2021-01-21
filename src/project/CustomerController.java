package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 *  CustomerController is the interface between the database and CustomerScreen.fxml
 *
 * @author katelanum
 */
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
    public ComboBox<Country> countryComboBox;
    public ComboBox<FirstLevelDivision> stateComboBox;
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
    private final ObservableList<FirstLevelDivision> fldList = FXCollections.observableArrayList();
    private final ObservableList<Country> countryList = FXCollections.observableArrayList();
    private ScheduleController scheduleController;
    private int countryId;

    /**
     * This sets the addDeleteModStatus variable to "add"
     */
    public void customerAddClick() {
        addDeleteModStatus = "add";
    }

    /**
     *  This adds customers to the customerList from the database and then populates the table view and the columns
     *  with the information from customerList
     *
     * @throws SQLException if there is a problem populating the customerList from the database
     */
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

    /**
     *  This populates the countryList with countries from the database and then sets the countryComboBox to be pulling
     *  its information from countryList
     *
     * @throws SQLException if there is a problem pulling the country information from the database
     */
    public void initializeCountryCombo() throws SQLException {
        countryList.clear();
        Database.getCountry(countryList);
        countryComboBox.setItems(countryList);
    }

    /**
     * This function populates the fldList with the FirstLevelDivision information from the database, it then sets the
     * stateComboBox to pull that information from the fldList
     *
     * @throws SQLException if there is an error that occurs while pulling the FirstLevelDivision from the database
     */
    public void initializeStateCombo() throws SQLException {
        fldList.clear();
        Database.getFLD(fldList,countryId);
        stateComboBox.setItems(fldList);
    }

    /**
     *  This populates the values of the text and combo boxes to be equal to the values of tempCust
     *
     * @throws SQLException is getting the information of tempCust from the database causes an error
     */
    private void autoPopulate() throws SQLException {
        phoneTextBox.setText(tempCust.getCustomerPhone());
        customerIdTextBox.setText(String.valueOf(tempCust.getCustomerId()));
        addressTextBox.setText(tempCust.getCustomerStreetAddress());
        nameTextBox.setText(tempCust.getCustomerName());
        zipTextBox.setText(tempCust.getCustomerPostal());
        int provId = tempCust.getDivisionId();
        FirstLevelDivision tempDiv = Database.getDivision(provId);
        stateComboBox.setValue(tempDiv);
        countryComboBox.setValue(Database.getSingleCountry(tempDiv.getCountryId()));
    }

    /**
     *  This function pulls the information from the text and combo boxes, tests to verify that all text and combo boxes
     *  have values, then sets the fields of tempCust to the values from the text and combo boxes
     *
     * @return false if a text or combo box is empty, returns true if all text and combo boxes have values
     */
    public boolean tempCustCreated() {
        if (nameTextBox.getText().isEmpty() || phoneTextBox.getText().isEmpty() ||
                zipTextBox.getText().isEmpty() || stateComboBox.getValue() == null || addressTextBox.getText().isEmpty()
                || countryComboBox.getValue() == null) {
            return false;
        }
        if (!nameTextBox.getText().isEmpty()) {
            tempCust.setCustomerName(nameTextBox.getText());
        }
//        if (!customerIdTextBox.getText().isEmpty()) {
//            tempCust.setCustomerId(Integer.parseInt(customerIdTextBox.getText()));
//        }
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

    /**
     *  First, it sets the addDeleteModStatus value to "update", then it sets the tempCust values to the values from
     *  the customer selected in the table view, after all of that, it calls autoPopulate to fill in the text and
     *  combo boxes with those values
     *
     * @throws SQLException if there is a problem grabbing the data about the selected customer from the database
     */
    public void customerUpdateClick() throws SQLException {
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

    /**
     * Upon selecting the delete radio button, it sets the addDeleteModStatus to "delete" and then populates the
     * tempCust with the infromation from the Customer selected in the table view
     *
     * @throws SQLException if something goes wrong while pulling the customer data from the database
     */
    public void customerDeleteClick() throws SQLException {
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

    /**
     *  Upon selection of a country from the countryComboBox, it gets the Id from that country and uses that to
     *  populate the stateComboBox
     *
     * @throws SQLException if theere is a problem getting the information about the selected country from the database
     */
    public void countryComboSelect() throws SQLException {
        Country selectedCountry = countryComboBox.getValue();
        countryId = selectedCountry.getCountryID();
        initializeStateCombo();
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void stateComboSelect() {
        // no operation occurs
    }

    /**
     * If a customer is being added, it verifies that the tempCust is properly set with the values from the text and
     * combo boxes. It then adds the customer to the database with the values from tempCust. The CustomerScreen is then
     * closed. If the tempCust is not properly filled, then the user will be shown an alert about the invalid customer.
     * <p>
     * If a customer is being deleted, the user is given a confirmation alert with the information about deleting the
     * customer's appointments as well. Once the user clicks OK on the confirmation alert, then the appointments
     * for that customer are deleted, then the customer is deleted. The appointment list on the ScheduleScreen is then
     * refreshed and the CustomerScreen is closed.
     * <p>
     * If a customer is being updated, then it verifies that the tempCust is properly set with the values from the text
     * and combo boxes. It then updates the customer in the database to have the values from tempCust. The
     * CustomerScreen is then closed. If the tempCust is not properly filled, then the user will be shown an alert
     * about the invalid customer.
     *
     * @throws SQLException if there is a problem adding, deleting, or updating the customer in the database
     */
    public void customerSaveClick() throws SQLException {
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

    /**
     *  Upon clicking the customerCancelButton, the user is given a confirmation alert to verify they want to cancel
     *  what they were doing on the customer screen. After the user clicks OK on the confirmation alert, the
     *  CustomerScreen is closed.
     */
    public void customerCancelClick() {
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

    /**
     *  This sets the scheduleController controlled in this class to be the same one that controls the ScheduleScreen.
     *  This is done to allow for the list of appointments in the ScheduleController to be refreshed from this class
     *  before the loading of the schedule screen so that the user has a more seamless experience.
     *
     * @param scheduleController the controller used to manipulate ScheduleController is passed in
     */
    public void setScheduleController(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }
}
