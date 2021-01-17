package project;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public ComboBox<Contacts> contactComboBox;
    public Text startDateText;
    public DatePicker startDate;
    public DatePicker endDate;
    public Text endDateText;
    public Button appointmentSaveButton;
    private final Alert appointmentScreenAlert = new Alert(Alert.AlertType.WARNING);
    public TableView<Appointment> appointmentTableView;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public TableColumn<Appointment, String> appointmentTitleColumn;
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    public TableColumn<Appointment, String> appointmentLocationColumn;
    public TableColumn<Appointment, String> appointmentContactColumn;
    public TableColumn<Appointment, String> appointmentTypeColumn;
    public TableColumn<Appointment, String> appointmentStartColumn;
    public TableColumn<Appointment, String> appointmentEndColumn;
    public TableColumn<Appointment, Integer> appointmentCustomerIdColumn;
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
    String addDeleteModStatus = "add";
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private boolean isValidTime = false;
    private ZonedDateTime appointmentStart;
    private ZonedDateTime appointmentEnd;
    private Appointment tempApp = new Appointment();
    private int appID;
    private int appointmentIdCount;
    private ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    private LoginController loginController;
    private ScheduleController scheduleController;
    private CustomerController customerController;
    private ObservableList<Appointment> allApp = FXCollections.observableArrayList();
//    private LocalTime openGen = LocalTime.of(13,00);
//    private LocalTime closeGen = LocalTime.of(3,00);
//    private LocalDate oDateGen = LocalDate.of(2020,1,1);
//    private LocalDate dDateGen = LocalDate.of(2020,1,2);
//    private LocalDateTime oGen = LocalDateTime.of(oDateGen, openGen);
//    private LocalDateTime cGen = LocalDateTime.of(dDateGen, closeGen);
//    private ZonedDateTime closeUTC = ZonedDateTime.of(cGen, ZoneOffset.UTC);
//    private ZonedDateTime openUTC = ZonedDateTime.of(oGen, ZoneOffset.UTC);
//    private ZonedDateTime closeLocal = closeUTC.withZoneSameInstant(ZoneOffset.systemDefault());
//    private ZonedDateTime openLocal = openUTC.withZoneSameInstant(ZoneOffset.systemDefault());
    private ObservableList<Integer> minutes = FXCollections.observableArrayList();
    private ObservableList<Integer> hours = FXCollections.observableArrayList();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        initializeContactBox();
        Database.initializeAppointmentList(allApp);
        Database.initializeCustomerList(customers);
        appointmentTableView.setEditable(true);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        // below takes in the start and then that is able to be changed to the correct time zone
        // need to update the appointment to deal with ZonedDateTime
        // have all storage be in UTC and only deal with local in UI
        appointmentStartColumn.setCellValueFactory(appt -> {
            return new ReadOnlyStringWrapper(appt.getValue().getStart().withZoneSameInstant(ZoneOffset.systemDefault()).format(format));
        });
        appointmentEndColumn.setCellValueFactory(appt -> {
            return new ReadOnlyStringWrapper(appt.getValue().getEnd().withZoneSameInstant(ZoneOffset.systemDefault()).format(format));
        });
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTableView.setPlaceholder(new Label(languageBundle.getString("noApp")));
        appointmentTableView.setItems(allApp);
        initializeMinBoxes();
        initializeHoursBoxes();
    }

    private void initializeHoursBoxes() {
        for (int i = 0; i <= 23; i++) {
            hours.add(i);
        }
        endHourCombo.setItems(hours);
        startHourCombo.setItems(hours);
    }

    private void initializeMinBoxes() {
        for (int i = 0; i < 60; i += 15) {
            minutes.add(i);
        }
        endMinCombo.setItems(minutes);
        startMinCombo.setItems(minutes);
    }

    private void initializeContactBox() throws SQLException {
        contactList.clear();
        Database.popContactsList(contactList);
        contactComboBox.setItems(contactList);
    }

    public void appointmentAddClick(ActionEvent actionEvent) {
        addDeleteModStatus = "add";
    }

    public void appointmentUpdateClick(ActionEvent actionEvent) throws SQLException {
        addDeleteModStatus = "update";
        tempAppSetter();
        autoPopulate();
    }

    private void tempAppSetter() throws SQLException {
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

    public void appointmentDeleteClick(ActionEvent actionEvent) throws SQLException {
        addDeleteModStatus = "delete";
        tempAppSetter();
        autoPopulate();
    }

    public void contactComboSelect(ActionEvent actionEvent) {
        Contacts selected = contactComboBox.getValue();
        tempApp.setContactName(selected.getName());
        tempApp.setContactId(selected.getContactId());
    }

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
        for (int i = 0; i < allApp.size(); i++) {
            if (tempApp.getEnd().isBefore(allApp.get(i).getEnd()) && tempApp.getStart().isAfter(allApp.get(i).getStart())) {
                appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
                appointmentScreenAlert.setHeaderText(languageBundle.getString("doubleBookHeader"));
                appointmentScreenAlert.setContentText(languageBundle.getString("doubleBookContent"));
                appointmentScreenAlert.showAndWait();
                return false;
            }
        }
        return true;
    }

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

    public void appointmentSaveClick(ActionEvent actionEvent) throws SQLException {
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
            //Appointment currentApp = Database.getAppointment(appID);
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

    public void appointmentStartSelect(ActionEvent actionEvent) {
    }

    public void appointmentEndSelect(ActionEvent actionEvent) {
    }

    public void setScheduleController(ScheduleController controller) {
        this.scheduleController = controller;
    }


    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void appointmentCancelClick(ActionEvent actionEvent) {
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

    public void endMinSelect(MouseEvent mouseEvent) {
    }

    public void endHourSelect(MouseEvent mouseEvent) {
    }

    public void startHourSelect(MouseEvent mouseEvent) {
    }

    public void startMinSelect(MouseEvent mouseEvent) {
    }
}
