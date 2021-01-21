package project;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *  AppointmentController is the interface between the database and AppointmentScreen.fxml
 *
 * @author katelanum
 */
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
    public TextField locationTextBox;
    public Text locationText;
    public Text customerText;
    public TextField customerTextBox;
    public TextField appointmentIdTextBox;
    public Text appointmentIdText;
    public Text contactText;
    public ComboBox<Contact> contactComboBox;
    public Text startDateText;
    public DatePicker startDate;
    public DatePicker endDate;
    public Text endDateText;
    public Button appointmentSaveButton;
    private final Alert appointmentScreenAlert = new Alert(Alert.AlertType.WARNING);
    public TableView<Appointment> appointmentTableView;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public Button appointmentCancelButton;
    public ChoiceBox<Integer> endHourCombo;
    public ChoiceBox<Integer> endMinCombo;
    public ChoiceBox<Integer> startHourCombo;
    public ChoiceBox<Integer> startMinCombo;
    public TextField descriptionTextBox;
    public TextField userIDTextBox;
    public Text userIDText;
    public TextField typeTextBox;
    public Text typeText;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn<Appointment, Integer>  customerIdColumn;
    public TableColumn<Appointment, String> customerColumn;
    private String addDeleteModStatus = "add";
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources",
            Locale.getDefault());
    private final Appointment tempApp = new Appointment();
    private final ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private ScheduleController scheduleController;
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private final ObservableList<Integer> minutes = FXCollections.observableArrayList();
    private final ObservableList<Integer> hours = FXCollections.observableArrayList();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     *  Populates the allApp and customer lists from the database and sets the values for the tableview and
     *  its columns.
     *  <p>
     *  A lambda is used for taking the value of the appointment's start variable, translating that to the user's
     *  timezone, and then also formatting before feeding that into the column for start date and time.
     *  <p>
     *  Another lambda is used for taking the value of the appointment's end variable, translating that to the user's
     *  timezone, and then also formatting before feeding that into the column for end date and time.
     *
     * @throws SQLException if something goes wrong in the collection of information from the database during the calls
     * to the Database class's initializeAppointmentList and initializeCustomerList functions
     */
    public void initialize() throws SQLException {
        initializeContactBox();
        Database.initializeAppointmentList(allApp);
        Database.initializeCustomerList(customers);
        appointmentTableView.setEditable(true);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(appt -> new ReadOnlyStringWrapper(appt.getValue().getStart()
                .withZoneSameInstant(ZoneOffset.systemDefault()).format(format)));
        endColumn.setCellValueFactory(appt -> new ReadOnlyStringWrapper(appt.getValue().getEnd()
                .withZoneSameInstant(ZoneOffset.systemDefault()).format(format)));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentTableView.setPlaceholder(new Label(languageBundle.getString("noApp")));
        appointmentTableView.setItems(allApp);
        initializeMinBoxes();
        initializeHoursBoxes();
    }

    /**
     *  Populates the hours list with hours 0 to 23
     */
    private void initializeHoursBoxes() {
        for (int i = 0; i <= 23; i++) {
            hours.add(i);
        }
        endHourCombo.setItems(hours);
        startHourCombo.setItems(hours);
    }

    /**
     * This populates the minutes list with 0 to 60 in increments of 15
     */
    private void initializeMinBoxes() {
        for (int i = 0; i < 60; i += 15) {
            minutes.add(i);
        }
        endMinCombo.setItems(minutes);
        startMinCombo.setItems(minutes);
    }

    /**
     *  This populates the contactList from the database and then sets the contactComboBox to
     *  be populated with that list
     *
     * @throws SQLException if there is a problem populating the contactList from the database in the Database's
     * popContactsList function
     */
    private void initializeContactBox() throws SQLException {
        contactList.clear();
        Database.popContactsList(contactList);
        contactComboBox.setItems(contactList);
    }

    /**
     *  This sets the addDeleteModStatus variable to "true"
     *
     */
    public void appointmentAddClick() {
        addDeleteModStatus = "add";
    }

    /**
     *  This sets the addDeleteModStatus and pulls the information about the appointment selected in the tableview from
     *  the database.It then populates the various text fields with that information and sets the tempApp values to
     *  those of the selected appointment.
     *
     * @throws SQLException if something goes wrong with pulling the appointment information from the database
     */
    public void appointmentUpdateClick() throws SQLException {
        addDeleteModStatus = "update";
        Appointment selectedApp = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedApp != null) {
            tempAppSetter();
            autoPopulate();
        }
    }

    /**
     *  This sets all the fields of the tempApp to being equal to the appointment selected in the tableview
     */
    private void tempAppSetter() {
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            tempApp.setStart(appointmentTableView.getSelectionModel().getSelectedItem().getStart());
            tempApp.setType(appointmentTableView.getSelectionModel().getSelectedItem().getType());
            tempApp.setLocation(appointmentTableView.getSelectionModel().getSelectedItem().getLocation());
            tempApp.setDescription(appointmentTableView.getSelectionModel().getSelectedItem().getDescription());
            tempApp.setTitle(appointmentTableView.getSelectionModel().getSelectedItem().getTitle());
            tempApp.setCustomerId(appointmentTableView.getSelectionModel().getSelectedItem().getCustomerId());
            tempApp.setUserId(appointmentTableView.getSelectionModel().getSelectedItem().getUserId());
            tempApp.setAppointmentId(appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId());
            tempApp.setContactId(appointmentTableView.getSelectionModel().getSelectedItem().getContactId());
            tempApp.setEnd(appointmentTableView.getSelectionModel().getSelectedItem().getEnd());
        }
    }

    /**
     * This sets the addDeleteModStatus variable and pulls the information from the database for the appointment
     * selected from the database. After that, it populates the various textboxes and tempApp with that appointment's
     * information.
     *
     * @throws SQLException if something goes wrong with pulling the appointment information from the database
     */
    public void appointmentDeleteClick() throws SQLException {
        addDeleteModStatus = "delete";
        Appointment selectedApp = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedApp != null) {
            tempAppSetter();
            autoPopulate();
        }
    }

    /**
     * This sets the tempApp contactName and contactId fields to match the contact value selected in the combo box
     */
    public void contactComboSelect() {
        Contact selected = contactComboBox.getValue();
        tempApp.setContactName(selected.getName());
        tempApp.setContactId(selected.getContactId());
    }

    /**
     * Checks that the end and start times selected through the combo boxes and the date pickers are valid. In order to
     * be considered a valid time, it must be within the business hours of 8am and 10pm EST and also not interfere with
     * the time of another appointment. Returns a boolean to represent if the time is valid or not.
     *
     * @return a boolean that is true if the times selected are considered valid and false if the times are outside of
     * business hours or overlap with an existing appointment
     */
    private boolean isValidTime() {
        LocalTime openGen = LocalTime.of(8,0);
        LocalTime closeGen = LocalTime.of(22,0);
        LocalDate oDateGen = startDate.getValue();
        LocalDate dDateGen = endDate.getValue();
        LocalDateTime oGen = LocalDateTime.of(oDateGen, openGen);
        LocalDateTime cGen = LocalDateTime.of(dDateGen, closeGen);
        ZonedDateTime closeET = ZonedDateTime.of(cGen, ZoneId.of("America/New_York"));
        ZonedDateTime openET = ZonedDateTime.of(oGen, ZoneId.of("America/New_York"));
        ZonedDateTime startET = tempApp.getStart().withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endET = tempApp.getEnd().withZoneSameInstant(ZoneId.of("America/New_York"));
        if (startET.toInstant().isBefore(openET.toInstant())) {
            appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("outBusHoursHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("befOpenContent"));
            appointmentScreenAlert.showAndWait();
            return false;
        }
        if (endET.toInstant().isAfter(closeET.toInstant())) {
            appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("outBusHoursHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("aftCloseContent"));
            appointmentScreenAlert.showAndWait();
            return false;
        }
        for (Appointment appointment : allApp) {
            if (tempApp.getAppointmentId() != appointment.getAppointmentId()) {
                if (!tempApp.getStart().isAfter(appointment.getEnd()) &&
                        !tempApp.getStart().isBefore(appointment.getStart())) {
                    appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
                    appointmentScreenAlert.setHeaderText(languageBundle.getString("doubleBookHeader"));
                    appointmentScreenAlert.setContentText(languageBundle.getString("doubleBookContent"));
                    appointmentScreenAlert.showAndWait();
                    return false;
                }
                if (!tempApp.getEnd().isBefore(appointment.getStart()) &&
                        !tempApp.getEnd().isAfter(appointment.getEnd())) {
                    appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
                    appointmentScreenAlert.setHeaderText(languageBundle.getString("doubleBookHeader"));
                    appointmentScreenAlert.setContentText(languageBundle.getString("doubleBookContent"));
                    appointmentScreenAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *  This populates the various text boxes and combo boxes with the information from the tempApp
     *
     * @throws SQLException if collecting the data for the tempApp from the database fails
     */
    private void autoPopulate() throws SQLException {
        descriptionTextBox.setText(tempApp.getDescription());
        appointmentTitleTextBox.setText(tempApp.getTitle());
        appointmentIdTextBox.setText(String.valueOf(tempApp.getAppointmentId()));
        locationTextBox.setText(tempApp.getLocation());
        customerTextBox.setText(String.valueOf(tempApp.getCustomerId()));
        contactComboBox.setValue(Database.getContact(tempApp.getContactId()));
        endDate.setValue(tempApp.getEnd().toLocalDate());
        startDate.setValue(tempApp.getStart().toLocalDate());
        endHourCombo.setValue(tempApp.getEnd().getHour());
        endMinCombo.setValue(tempApp.getEnd().getMinute());
        startMinCombo.setValue(tempApp.getStart().getMinute());
        startHourCombo.setValue(tempApp.getStart().getHour());
        typeTextBox.setText(tempApp.getType());
        userIDTextBox.setText(String.valueOf(tempApp.getUserId()));
    }

    /**
     * This validates that the customerId submitted on the form is one that has an existing customer connected to it,
     * if not the user is given an alert with the valid options on it.
     *
     * @return true if the customerId does match an existing customer and false if the customerId input does not have
     * an existing customer with that Id
     */
    private boolean isCustIDValid() {
        for (Customer customer : customers) {
            if (Integer.parseInt(customerTextBox.getText()) == customer.getCustomerId()) {
                return true;
            }
        }
        appointmentScreenAlert.setTitle(languageBundle.getString("invalidCustTitle"));
        appointmentScreenAlert.setHeaderText(languageBundle.getString("invalidCustHeader"));
        StringBuilder invalidCustContent = new StringBuilder(languageBundle.getString("invalidCustContent") + " ");
        for (Customer customer : customers) {
            invalidCustContent.append(customer.getCustomerId()).append(" ").append(customer.getCustomerName());
        }
        appointmentScreenAlert.setContentText(invalidCustContent.toString());
        appointmentScreenAlert.showAndWait();
        return false;
    }

    /**
     * This sets all the tempApp values to be equal to the various fields and combo boxes. Before this it verifies that
     * every field is filled in, if a field is let empty, the user is given an alert that they did not fill in every
     * box. It also calls the isCustIDValid method to check that the customer Id that was input is valid. Returns a
     * boolean value to indicate if the input was able to pass all tests and tempApp wa populated with the data from
     * those inputs.
     *
     * @return true is the input from all the text nd combo boxes passes all the tests and all fields are successfully
     * set, otherwise returns false
     */
    private boolean isFinalTempApp() {
        if (startDate.getValue() == null || startHourCombo.getValue() == null || startMinCombo.getValue() == null ||
                endDate.getValue() == null || endHourCombo.getValue() == null || endMinCombo.getValue() == null ||
                customerTextBox.getText() == null || locationTextBox.getText() == null ||
                appointmentTitleTextBox.getText() == null || descriptionTextBox.getText() == null ||
                typeTextBox.getText() == null) {
            appointmentScreenAlert.setTitle(languageBundle.getString("invalidAppTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("invalidAppHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("invalidAppContent"));
            appointmentScreenAlert.showAndWait();
            return false;
        }
        else if (!isCustIDValid()) {
            return false;
        }
        else {
            Integer h = startHourCombo.getValue();
            Integer m = startMinCombo.getValue();
            LocalTime time = LocalTime.of(h,m);
            ZonedDateTime sDate = ZonedDateTime.of(startDate.getValue(), time, ZoneId.systemDefault());
            ZonedDateTime startUTC = sDate.withZoneSameInstant(ZoneOffset.UTC);
            tempApp.setStart(startUTC);
            h = endHourCombo.getValue();
            m = endMinCombo.getValue();
            time = LocalTime.of(h, m);
            ZonedDateTime eDate = ZonedDateTime.of(endDate.getValue(), time, ZoneId.systemDefault());
            ZonedDateTime endUTC = eDate.withZoneSameInstant(ZoneOffset.UTC);
            tempApp.setEnd(endUTC);
            Customer tempCust = new Customer();
            tempApp.setCustomerId(Integer.parseInt(customerTextBox.getText()));
            try {
                tempCust = Database.getCustomer(Integer.parseInt(customerTextBox.getText()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tempApp.setCustomerName(tempCust.getCustomerName());
            tempApp.setLocation(locationTextBox.getText());
            tempApp.setTitle(appointmentTitleTextBox.getText());
            tempApp.setDescription(descriptionTextBox.getText());
            tempApp.setType(typeTextBox.getText());
            return true;
        }
    }

    /**
     * This method verifies that the user Id put into the text box is a valid one by checking the database for a user
     * with that Id, if no user exists with that userId, the user is given an alert stating that the user Id does not
     * exist. It then returns a boolean value to indicate if a user with the userId input does exist. If the user Id was
     * deemed to be valid, it sets the tempApp userId field equal to the user Id that was checked.
     *
     * @return true if the user Id from the text box does have an affiliated user and that user Id was able to be set
     * as the value for the tempApp userId, otherwise it returns false
     *
     * @throws SQLException if collecting the user information from the database does not work
     */
    private boolean isValidUser() throws SQLException {
        ObservableList<String> users = FXCollections.observableArrayList();
        Database.userList(users);
        String user = userIDTextBox.getText();
        if (users.contains(user)) {
            tempApp.setUserId(Integer.parseInt(userIDTextBox.getText()));
            return true;
        }
        else {
            appointmentScreenAlert.setTitle(languageBundle.getString("invalidLoginErrorTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("invalidLoginErrorHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("invalidLoginErrorContent"));
            appointmentScreenAlert.showAndWait();
            return false;
        }
    }

    /**
     *  This method first verifies that the data from the text and combo boxes is valid and sets the value of tempApp
     *  using the isFinalTempApp and isValidUser functions. If either of these fail, they will return false and cause
     *  this function to stop. From there, it is determined whether the addDeleteModStatus indicates if the appointment
     *  is being added, deleted, or updated and it goes down a branch respective to that.
     *  <p>
     *  If the appointment is being added, the time is validated using the isValidTime function. If it passes that test,
     *  an appopintment is added to the database with the information from tempApp. The appointments on the
     *  scheduleScreen are refreshed and the appointmentScreen is closed.
     *  <p>
     *  If the appointment is being deleted, the user is presented with a confirmation alert to verify that they want
     *  to delete that apopintment and they are given the appointment type and Id in that alert. Upon them clicking the
     *  OK button on the confirmation alert, the appointment is deleted from the database. The appointments on the
     *  scheduleScreen are refreshed and the appointmentScreen is closed.
     *  <p>
     *  If the appointment is being updated, the time is checked using the isValidTime function. Once it passes that
     *  check, the appointment is updated in the database to match the values of tempApp. The appointments on the
     *  scheduleScreen are refreshed and the appointmentScreen is closed.
     *
     * @throws SQLException if there is a problem adding, deleting, or updating the appointment in the database
     */
    public void appointmentSaveClick() throws SQLException {
        if (!isFinalTempApp() || !isValidUser()) {
            return;
        }
        if (addDeleteModStatus.equalsIgnoreCase("add")) {
            if (isValidTime()) {
                Database.addAppointment(tempApp);
                scheduleController.refreshAllApps();
                Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
                currentStage.close();
            }
        }
        else if(addDeleteModStatus.equalsIgnoreCase("delete")) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle(languageBundle.getString("appDelTitle"));
            confirmationAlert.setHeaderText(languageBundle.getString("appDelHeader") + ' ' + tempApp.getType());
            confirmationAlert.setContentText(languageBundle.getString("appDelContent") + ' ' + tempApp.getAppointmentId());
            Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
            if (confirmation.get() == ButtonType.OK) {
                Database.deleteAppointment(tempApp.getAppointmentId());
                scheduleController.refreshAllApps();
                Stage currentStage = (Stage) appointmentSaveButton.getScene().getWindow();
                currentStage.close();
            }
        }
        else if(addDeleteModStatus.equalsIgnoreCase("update")) {
            if (isValidTime()) {
                Database.updateAppointment(tempApp);
                scheduleController.refreshAllApps();
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
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void appointmentStartSelect() {
        // no operation occurs
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void appointmentEndSelect() {
        // no operation occurs
    }

    /**
     * This sets the scheduleController controlled in this class to be the same one that controls the ScheduleScreen.
     * This is done to allow for the list of appointments in the ScheduleController to be refreshed from this class
     * before the loading of the schedule screen so that the user has a more seamless experience.
     *
     * @param controller the controller used to manipulate ScheduleController is passed in
     */
    public void setScheduleController(ScheduleController controller) {
        this.scheduleController = controller;
    }

    /**
     *  Upon clicking the appointmentCancelButton, the user is given a confirmation alert to verify they want to cancel
     *  what they were doing on the appointment screen. After the user clicks OK on the confirmation alert, the
     *  AppointmentScreen is closed.
     */
    public void appointmentCancelClick() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(languageBundle.getString("cancel"));
        confirmationAlert.setHeaderText(languageBundle.getString("cancelActions"));
        confirmationAlert.setContentText(languageBundle.getString("continue"));
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if (confirmation.get() == ButtonType.OK) {
            Stage currentStage = (Stage) appointmentCancelButton.getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void endMinSelect() {
        // no operation occurs
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void endHourSelect() {
        // no operation occurs
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void startHourSelect() {
        // no operation occurs
    }

    /**
     *  Nothing occurs in this function, it is simply here to prevent errors being thrown
     */
    public void startMinSelect() {
        // no operation occurs
    }
}
