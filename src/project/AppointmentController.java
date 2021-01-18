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
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private final Appointment tempApp = new Appointment();
    private final ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    private ScheduleController scheduleController;
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private final ObservableList<Integer> minutes = FXCollections.observableArrayList();
    private final ObservableList<Integer> hours = FXCollections.observableArrayList();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

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
        startColumn.setCellValueFactory(appt -> {
            return new ReadOnlyStringWrapper(appt.getValue().getStart().withZoneSameInstant(ZoneOffset.systemDefault()).format(format));
        });
        endColumn.setCellValueFactory(appt -> {
            return new ReadOnlyStringWrapper(appt.getValue().getEnd().withZoneSameInstant(ZoneOffset.systemDefault()).format(format));
        });
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
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
        Appointment selectedApp = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedApp != null) {
            tempAppSetter();
            autoPopulate();
        }
    }

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

    public void appointmentDeleteClick(ActionEvent actionEvent) throws SQLException {
        addDeleteModStatus = "delete";
        Appointment selectedApp = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedApp != null) {
            tempAppSetter();
            autoPopulate();
        }
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
        for (Appointment appointment : allApp) {
            if (!tempApp.getStart().isAfter(appointment.getEnd()) && !tempApp.getStart().isBefore(appointment.getStart())) {
                appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
                appointmentScreenAlert.setHeaderText(languageBundle.getString("doubleBookHeader"));
                appointmentScreenAlert.setContentText(languageBundle.getString("doubleBookContent"));
                appointmentScreenAlert.showAndWait();
                return false;
            }
            if (!tempApp.getEnd().isBefore(appointment.getStart()) && !tempApp.getEnd().isAfter(appointment.getEnd())) {
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
