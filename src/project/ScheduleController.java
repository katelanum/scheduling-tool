package project;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn<Appointment, Integer> customerIdColumn;
    public TableColumn<Appointment, String> customerColumn;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

    public void openAppointmentClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        AppointmentController controller = loader.getController();
        controller.setScheduleController(this);
        stage.show();
    }

    public void openCustomerClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        CustomerController controller = loader.getController();
        controller.setScheduleController(this);
        stage.show();
    }

    public void scheduleCloseClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) scheduleClose.getScene().getWindow();
        currentStage.close();
    }

    public void monthViewClick(ActionEvent actionEvent) {
        monthApp.clear();
        for (Appointment appointment : allApp) {
            if ((appointment.getEnd().getYear() == currentDT.getYear()) && (appointment.getStart().getMonth() == currentDT.getMonth())) {
                monthApp.add(appointment);
            }
        }
        scheduleTableView.setItems(monthApp);
    }

    public void refreshAllApps() {
        allApp.clear();
        Database.initializeAppointmentList(allApp);
        scheduleTableView.setItems(allApp);
    }

    public void weekViewClick(ActionEvent actionEvent) {
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

    public void initialize(){
        Database.initializeAppointmentList(allApp);
        scheduleTableView.setEditable(true);
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
        scheduleTableView.setPlaceholder(new Label(languageBundle.getString("noApp")));
        scheduleTableView.setItems(allApp);
    }

    public void openReportsClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        Stage loader = FXMLLoader.load(getClass().getResource("ReportsScreen.fxml"), languageBundle);
        loader.show();
    }

    public void allViewClick(ActionEvent actionEvent) {
        scheduleTableView.setItems(allApp);
    }
}