package project;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ReportsController acts as the interface between the database and ReportsScreen.fxml
 *
 * @author katelanum
 */
public class ReportsController {
    public RadioButton typeViewRadio;
    public Stage scheduleStage;
    public Scene scheduleScene;
    public AnchorPane customerPane;
    public RadioButton contactViewRadio;
    public Text reportsTitle;
    public Button reportsClose;
    public ComboBox<String> typeCombo;
    public ComboBox<Contact> contactCombo;
    public ComboBox<String> monthCombo;
    public RadioButton locationRadio;
    public TableView<Appointment> scheduleTableView;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn<Appointment, Integer> customerIdColumn;
    public TableColumn<Appointment, String> customerColumn;
    public ComboBox<String> locationCombo;
    public ToggleGroup reportRadioGroup;
    public Button typeSummaryButton;
    private final ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private final ObservableList<Appointment> listByCon = FXCollections.observableArrayList();
    private final ObservableList<Appointment> listByType = FXCollections.observableArrayList();
    private final ObservableList<Appointment> listByLoc = FXCollections.observableArrayList();
    private final ObservableList<String> monthList = FXCollections.observableArrayList();
    private final ObservableList<String> typeList = FXCollections.observableArrayList();
    private final ObservableList<String> locationList = FXCollections.observableArrayList();
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    public TableColumn<Appointment,Integer> appointmentIdColumn;

    /**
     *  Populates the allApp list from the database and the contact, type, month, and location combo boxes from their
     *  initialize functions. Sets the table view to showing allApps and sets each column.
     *  <p>
     *  A lambda is used for taking the value of the appointment's start variable, translating that to the user's
     *  timezone, and then also formatting before feeding that into the column for start date and time.
     *  <p>
     *  Another lambda is used for taking the value of the appointment's end variable, translating that to the user's
     *  timezone, and then also formatting before feeding that into the column for end date and time.
     *
     * @throws SQLException if there is a problem populating the allApp list from the database
     */
    public void initialize() throws SQLException {
        initializeContact();
        initializeType();
        initializeMonth();
        initializeLocation();
        Database.initializeAppointmentList(allApp);
        scheduleTableView.setEditable(true);
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
        scheduleTableView.setPlaceholder(new Label(languageBundle.getString("noApp")));
        scheduleTableView.setItems(allApp);
    }

    /**
     * Populates monthCombo with the resource bundle values for the months of the year
     */
    private void initializeMonth() {
        monthList.add(languageBundle.getString("jan"));
        monthList.add(languageBundle.getString("feb"));
        monthList.add(languageBundle.getString("mar"));
        monthList.add(languageBundle.getString("apr"));
        monthList.add(languageBundle.getString("may"));
        monthList.add(languageBundle.getString("jun"));
        monthList.add(languageBundle.getString("jul"));
        monthList.add(languageBundle.getString("aug"));
        monthList.add(languageBundle.getString("sep"));
        monthList.add(languageBundle.getString("oct"));
        monthList.add(languageBundle.getString("nov"));
        monthList.add(languageBundle.getString("dec"));
        monthCombo.setItems(monthList);
    }

    /**
     *  Populates contactList with contact information from the database, from there it sets the items of contactCombo
     *  to be the items from contactList
     *
     * @throws SQLException if there is a problem pulling the contact information from the database
     */
    private void initializeContact() throws SQLException {
        contactList.clear();
        Database.popContactsList(contactList);
        contactCombo.setItems(contactList);
    }

    /**
     * Populates appointments list from the database, then uses that list to find all the appointment types and puts
     * those types into typeList. The typeList is then used to populate the items in typeCombo
     */
    private void initializeType() {
        ObservableList<Appointment> appointments =  FXCollections.observableArrayList();
        Database.initializeAppointmentList(appointments);
        for (Appointment appointment : appointments) {
            if (!typeList.contains(appointment.getType().toLowerCase())) {
                typeList.add(appointment.getType().toLowerCase());
            }
        }
        typeCombo.setItems(typeList);
    }

    /**
     *  Populates appointments list from the database, then uses that list to find all the appointment locations and
     *  puts those types into locationList. The locationList is then used to populate the items in locationCombo
     */
    private void initializeLocation() {
        ObservableList<Appointment> appointments =  FXCollections.observableArrayList();
        Database.initializeAppointmentList(appointments);
        for (Appointment appointment : appointments) {
            if (!locationList.contains(appointment.getLocation())) {
                locationList.add(appointment.getLocation());
            }
        }
        locationCombo.setItems(locationList);
    }

    /**
     *  Upon clicking the typeView radio button, verifies that both typeCombo and monthCombo have selected values, it
     *  then puts all appointments with both the selected type and month into listByType. The table view is then set to
     *  show the values from listByType.
     */
    public void typeViewClick() {
        listByType.clear();
        if (typeCombo.getValue() != null && monthCombo.getValue() != null) {
            String type = typeCombo.getValue();
            String month = monthCombo.getValue();
            for (Appointment appointment : allApp) {
                if (appointment.getType().equalsIgnoreCase(type) &&
                        appointment.getStart().getMonth().name().equalsIgnoreCase(month)) {
                    listByType.add(appointment);
                }
            }
            scheduleTableView.setItems(listByType);
        }
    }

    /**
     *  Upon clicking the contact view radio button, it verifies there is a value selected from contactCombo. Once that
     *  is verified, a list of appointments with that contact is constructed by going through the list of all
     *  appointments and adding appointments with the selected contact to listByCon. The scheduleTableView is then set
     *  to show the appointments from listByCon.
     */
    public void contactViewClick() {
        listByCon.clear();
        if (contactCombo.getValue() != null) {
            int contactID = contactCombo.getValue().getContactId();
            for (Appointment appointment : allApp) {
                if (appointment.getContactId() == contactID) {
                    listByCon.add(appointment);
                }
            }
            scheduleTableView.setItems(listByCon);
        }
    }

    /**
     * Closes ReportsScreen
     */
    public void reportsCloseClick() {
        Stage currentStage = (Stage) reportsClose.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Upon clicking the location view radio button, it verifies that there is a location selected from the location
     * combo box. Once that is verified, it constructs listByLoc by going through the list of all appointments and
     * adding any appointments with a location matching the selected one to listByLoc. The scheduleTableView is then
     * set to show the appointments from listByLoc.
     */
    public void locationViewClick() {
        listByLoc.clear();
        if (locationCombo.getValue() != null) {
            String location = locationCombo.getValue();
            for (Appointment appointment : allApp) {
                if (appointment.getLocation().equalsIgnoreCase(location)) {
                    listByLoc.add(appointment);
                }
            }
            scheduleTableView.setItems(listByLoc);
        }
    }

    /**
     * Upon selection in the type combo box, verifies that both typeCombo and monthCombo have selected values, it then
     * puts all appointments with both the selected type and month into listByType. The table view is then set to show
     * the values from listByType.
     */
    public void typeSelection() {
        listByType.clear();
        if (typeViewRadio.isSelected() && monthCombo.getValue() != null) {
            String type = typeCombo.getValue();
            String month = monthCombo.getValue();
            for (Appointment appointment : allApp) {
                if (appointment.getType().equalsIgnoreCase(type) &&
                        appointment.getStart().getMonth().name().equalsIgnoreCase(month)) {
                    listByType.add(appointment);
                }
            }
            scheduleTableView.setItems(listByType);
        }
    }

    /**
     *  Upon selecting a contact from the contact combo box, it verifies that the contact view radio button is also
     *  selected. Once that is verified, a list of appointments with that contact is constructed by going through the
     *  list of all appointments and adding appointments with the selected contact to listByCon. The scheduleTableView
     *  is then set to show the appointments from listByCon.
     */
    public void contactSelection() {
        listByCon.clear();
        if (contactViewRadio.isSelected()) {
            int contactID = contactCombo.getValue().getContactId();
            for (Appointment appointment : allApp) {
                if (appointment.getContactId() == contactID) {
                    listByCon.add(appointment);
                }
            }
            scheduleTableView.setItems(listByCon);
        }
    }

    /**
     *  Upon selection in the month combo box, verifies that both typeCombo and monthCombo have selected values, it then
     *  puts all appointments with both the selected type and month into listByType. The table view is then set to show
     *  the values from listByType.
     */
    public void monthSelection() {
        listByType.clear();
        if (typeViewRadio.isSelected() && typeCombo.getValue() != null) {
            String type = typeCombo.getValue();
            String month = monthCombo.getValue();
            for (Appointment appointment : allApp) {
                if (appointment.getType().equalsIgnoreCase(type) &&
                        appointment.getStart().getMonth().name().equalsIgnoreCase(month)) {
                    listByType.add(appointment);
                }
            }
            scheduleTableView.setItems(listByType);
        }
    }

    /**
     *  Upon selecting a location from locationCombo, it verifies that the location view radio button is also selected.
     *  Once that is verified, it constructs listByLoc by going through the list of all appointments and adding any
     *  appointments with a location matching the selected one to listByLoc. The scheduleTableView is then set to show
     *  the appointments from listByLoc.
     */
    public void locationSelection() {
        listByLoc.clear();
        if (locationRadio.isSelected()) {
            String location = locationCombo.getValue();
            for (Appointment appointment : allApp) {
                if (appointment.getLocation().equalsIgnoreCase(location)) {
                    listByLoc.add(appointment);
                }
            }
            scheduleTableView.setItems(listByLoc);
        }
    }

    /**
     *  Loads TypeSummaryScreen
     *
     * @throws IOException if there is a problem loading either the resource bundle of TypeSummaryScreen.fxml
     */
    public void typeSummaryClick() throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TypeSummaryScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        stage.show();
    }
}
