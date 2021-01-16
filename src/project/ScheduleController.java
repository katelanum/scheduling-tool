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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
    public TableColumn<Appointment, String> appointmentTitleColumn;
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    public TableColumn<Appointment, String> appointmentLocationColumn;
    public TableColumn<Appointment, String> appointmentContactColumn;
    public TableColumn<Appointment, String> appointmentTypeColumn;
    public TableColumn<Appointment, String> appointmentStartColumn;
    public TableColumn<Appointment, String> appointmentEndColumn;
    public TableColumn<Appointment, Integer> appointmentCustomerIdColumn;
    public Button openReportsButton;
    public RadioButton allViewRadio;
    public ToggleGroup scheduleRadioGroup;
    private final ObservableList<Appointment> weekApp = FXCollections.observableArrayList();
    private final ObservableList<Appointment> monthApp = FXCollections.observableArrayList();
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private final ZonedDateTime currentDT = ZonedDateTime.now();
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
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
        for (Appointment appointment : allApp) {
            if ((appointment.getEnd().getYear() == currentDT.getYear()) &&
                    (appointment.getEnd().getMonth() == currentDT.getMonth()) &&
                    (appointment.getEnd().getDayOfMonth() <= currentDT.getDayOfMonth() + 7) &&
                    (appointment.getStart().getDayOfMonth() >= currentDT.getDayOfMonth())) {
                weekApp.add(appointment);
            }
        }
        scheduleTableView.setItems(weekApp);
    }

    public void initialize(){
        Database.initializeAppointmentList(allApp);
        scheduleTableView.setEditable(true);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(appt -> {
            return new ReadOnlyStringWrapper(appt.getValue().getStart().withZoneSameInstant(ZoneOffset.systemDefault()).format(format));
        });
        appointmentEndColumn.setCellValueFactory(appt -> {
            return new ReadOnlyStringWrapper(appt.getValue().getEnd().withZoneSameInstant(ZoneOffset.systemDefault()).format(format));
        });
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
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