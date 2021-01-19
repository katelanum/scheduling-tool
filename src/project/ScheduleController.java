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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ScheduleController acts an interface between the database and ScheduleScreen.fxml
 *
 * @author katelanum
 */
public class ScheduleController {
    public Stage scheduleStage;
    public Scene scheduleScene;
    public AnchorPane customerPane;
    public RadioButton weekViewRadio;
    public RadioButton monthViewRadio;
    public Button openAppointmentButton;
    public Button openCustomerButton;
    public Text scheduleTitle;
    public Button scheduleClose;
    public TableView<Appointment> scheduleTableView;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public Button openReportsButton;
    public RadioButton allViewRadio;
    public ToggleGroup scheduleRadioGroup;
    private final ObservableList<Appointment> weekApp = FXCollections.observableArrayList();
    private final ObservableList<Appointment> monthApp = FXCollections.observableArrayList();
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private final ZonedDateTime currentDT = ZonedDateTime.now();
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources",
            Locale.getDefault());
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn<Appointment, Integer> customerIdColumn;
    public TableColumn<Appointment, String> customerColumn;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

    /**
     *  Loads AppointmentScreen and the resource bundle
     *
     * @throws IOException if there is a problem loading either the resource bundle of AppointmentScreen.fxml
     */
    public void openAppointmentClick() throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        AppointmentController controller = loader.getController();
        controller.setScheduleController(this);
        stage.show();
    }

    /**
     *  Loads CustomerScreen and the resource bundle
     *
     * @throws IOException if there is a problem loading either the resource bundle of CustomerScreen.fxml
     */
    public void openCustomerClick() throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        CustomerController controller = loader.getController();
        controller.setScheduleController(this);
        stage.show();
    }

    /**
     *  Closes the ScheduleScreen
     */
    public void scheduleCloseClick() {
        Stage currentStage = (Stage) scheduleClose.getScene().getWindow();
        currentStage.close();
    }

    /**
     *  Populates the monthApp list by going through all the appointments in allApp and then adding any appointments in
     *  the current month to monthApp. The table view s then set to display the appointments from monthApp
     */
    public void monthViewClick() {
        monthApp.clear();
        for (Appointment appointment : allApp) {
            if ((appointment.getEnd().getYear() == currentDT.getYear()) && (appointment.getStart().getMonth() ==
                    currentDT.getMonth())) {
                monthApp.add(appointment);
            }
        }
        scheduleTableView.setItems(monthApp);
    }

    /**
     *  Populates the allApp list from the database and sets the table view to show the allApp list
     */
    public void refreshAllApps() {
        allApp.clear();
        Database.initializeAppointmentList(allApp);
        scheduleTableView.setItems(allApp);
    }

    /**
     * Populates the weekApp list by going through all the appointments in the allApp list and then adding any
     * appointments in the current Sunday to Sunday week to weekApp. To define what dates are included in this week,
     * the day of the week is taken from currentDT, then days added or subtracted depending on the day of the week
     * today is. The table view is then set to display the appointments in weekApp
     */
    public void weekViewClick() {
        weekApp.clear();
        ZonedDateTime weekStart;
        ZonedDateTime weekEnd;
        if (currentDT.getDayOfWeek().getValue() == 7) {
            weekStart = currentDT.minusDays(1);
            weekEnd = currentDT.plusDays(7);
        }
        else if (currentDT.getDayOfWeek().getValue() == 1) {
            weekStart = currentDT.minusDays(1);
            weekEnd = currentDT.plusDays(6);
        }
        else if (currentDT.getDayOfWeek().getValue() == 2) {
            weekStart = currentDT.minusDays(2);
            weekEnd = currentDT.plusDays(5);
        }
        else if (currentDT.getDayOfWeek().getValue() == 3) {
            weekStart = currentDT.minusDays(3);
            weekEnd = currentDT.plusDays(4);
        }
        else if (currentDT.getDayOfWeek().getValue() == 4) {
            weekStart = currentDT.minusDays(4);
            weekEnd = currentDT.plusDays(3);
        }
        else if (currentDT.getDayOfWeek().getValue() == 5) {
            weekStart = currentDT.minusDays(5);
            weekEnd = currentDT.plusDays(2);
        }
        else {
            weekStart = currentDT.minusDays(6);
            weekEnd = currentDT.plusDays(1);
        }
        for (Appointment appointment : allApp) {
            if ((appointment.getEnd().getYear() == currentDT.getYear()) &&
                    (appointment.getEnd().getMonth() == currentDT.getMonth()) &&
                    (appointment.getEnd().isBefore(ChronoZonedDateTime.from(weekEnd))) &&
                    (appointment.getStart().isAfter(ChronoZonedDateTime.from(weekStart)))) {
                weekApp.add(appointment);
            }
        }
        scheduleTableView.setItems(weekApp);
    }

    /**
     *  Populates the allApp list from the database and sets the values for the table view and its columns.
     *  <p>
     *  A lambda is used for taking the value of the appointment's start variable, translating that to the user's
     *  timezone, and then also formatting before feeding that into the column for start date and time.
     *  <p>
     *  Another lambda is used for taking the value of the appointment's end variable, translating that to the user's
     *  timezone, and then also formatting before feeding that into the column for end date and time.
     */
    public void initialize(){
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
     *  Loads ReportsScreen and the resource bundle
     *
     * @throws IOException if there is a problem loading either the resource bundle of ReportsScreen.fxml
     */
    public void openReportsClick() throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        Stage loader = FXMLLoader.load(getClass().getResource("ReportsScreen.fxml"), languageBundle);
        loader.show();
    }

    /**
     * Sets the table view to show the appointments from the allApp list
     */
    public void allViewClick() {
        scheduleTableView.setItems(allApp);
    }
}