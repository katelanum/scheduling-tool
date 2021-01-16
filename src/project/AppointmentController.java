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
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

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
    private LocalTime openGen = LocalTime.of(22,00);
    private LocalTime closeGen = LocalTime.of(3,00);
    private LocalDate dateGen = LocalDate.of(2020,1,1);
    private LocalDateTime oGen = LocalDateTime.of(dateGen, openGen);
    private LocalDateTime cGen = LocalDateTime.of(dateGen, closeGen);
    private ZonedDateTime closeUTC = ZonedDateTime.of(cGen, ZoneOffset.UTC);
    private ZonedDateTime openUTC = ZonedDateTime.of(oGen, ZoneOffset.UTC);
    private ZonedDateTime closeLocal = closeUTC.withZoneSameInstant(ZoneOffset.systemDefault());
    private ZonedDateTime openLocal = openUTC.withZoneSameInstant(ZoneOffset.systemDefault());
    private ObservableList<Integer> minutes = FXCollections.observableArrayList();
    private ObservableList<Integer> hours = FXCollections.observableArrayList();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

    public void initialize() throws SQLException {
        initializeContactBox();
        Database.initializeAppointmentList(allApp);
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
        for (int i = 00; i < 60; i += 15) {
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
        if (tempApp.getStart().getHour() < openUTC.getHour()) {
            appointmentScreenAlert.setTitle(languageBundle.getString("invalidTimeTitle"));
            appointmentScreenAlert.setHeaderText(languageBundle.getString("outBusHoursHeader"));
            appointmentScreenAlert.setContentText(languageBundle.getString("befOpenContent"));
            appointmentScreenAlert.showAndWait();
            return false;
        }
        if (tempApp.getEnd().getHour() > closeUTC.getHour()) {
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
        addressTextBox.setText(tempApp.getLocation());
        appointmentTitleTextBox.setText(tempApp.getTitle());
        appointmentIdTextBox.setText(String.valueOf(tempApp.getAppointmentId()));
        locationTextBox.setText(tempApp.getLocation());
        customerTextBox.setText(tempApp.getCustomerName());
        contactComboBox.setValue(Database.getContact(tempApp.getContactId()));
    }

    public void appointmentSaveClick(ActionEvent actionEvent) throws SQLException {
        if (!startDate.getValue().equals(null) && !startHourCombo.getValue().equals(null) && !startMinCombo.getValue().equals(null)) {
            Integer h = startHourCombo.getValue();
            Integer m = startMinCombo.getValue();
            LocalTime time = LocalTime.of(h,m);
            ZonedDateTime sDate = ZonedDateTime.of(startDate.getValue(), time, ZoneId.systemDefault());
            ZonedDateTime startUTC = sDate.withZoneSameInstant(ZoneOffset.UTC);
            tempApp.setStart(startUTC);
        }
        if (!endDate.getValue().equals(null) && !endHourCombo.getValue().equals(null) && !endMinCombo.getValue().equals(null)) {
            Integer h = endHourCombo.getValue();
            Integer m = endMinCombo.getValue();
            LocalTime time = LocalTime.of(h, m);
            ZonedDateTime eDate = ZonedDateTime.of(endDate.getValue(), time, ZoneId.systemDefault());
            ZonedDateTime endUTC = eDate.withZoneSameInstant(ZoneOffset.UTC);
            tempApp.setEnd(endUTC);
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
            Appointment currentApp = Database.getAppointment(appID);
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle(languageBundle.getString("appDelTitle"));
            confirmationAlert.setHeaderText(languageBundle.getString("appDelHeader") + ' ' + currentApp.getType());
            confirmationAlert.setContentText(languageBundle.getString("appDelContent") + ' ' + currentApp.getAppointmentId());
            Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
            if (confirmation.get() == ButtonType.OK) {
                Database.deleteAppointment(appID);
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
        tempApp = null;
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
