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
import java.sql.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class ScheduleController {
    public Stage scheduleStage;
    public Scene scheduleScene;
    public AnchorPane customerPane;
    public RadioButton weekViewRadio;
    public ToggleGroup appointmentRadioGroup;
    public RadioButton monthViewRadio;
    public Text appointmentsTitle;
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
    private ObservableList<Appointment> weekApp = FXCollections.observableArrayList();
    private ObservableList<Appointment> monthApp = FXCollections.observableArrayList();
    private ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    //private Date currentDate = Date.from(Instant.now());
    private ZonedDateTime currentDT = ZonedDateTime.now();
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private AppointmentController appointmentController;
    private CustomerController customerController;
    private LoginController loginController;
    LocalDateTime currentUserDT = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

    public void openAppointmentClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        AppointmentController controller = loader.<AppointmentController>getController();
        controller.setScheduleController(this);
        stage.show();
    }

    public void openCustomerClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        CustomerController controller = loader.<CustomerController>getController();
        controller.setScheduleController(this);
        stage.show();
    }

    public void scheduleCloseClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) scheduleClose.getScene().getWindow();
        currentStage.close();
    }

    public void monthViewClick(ActionEvent actionEvent) {
        monthApp.clear();
        for (int i = 0; i < allApp.size(); i++) {
            if((allApp.get(i).getEnd().getYear() == currentDT.getYear()) && (allApp.get(i).getStart().getMonth() == currentDT.getMonth())) {
                monthApp.add(allApp.get(i));
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
        for (int i = 0; i < allApp.size(); i++) {
            if ((allApp.get(i).getEnd().getYear() == currentDT.getYear()) &&
                    (allApp.get(i).getEnd().getMonth() == currentDT.getMonth()) &&
                    (allApp.get(i).getEnd().getDayOfMonth() <= currentDT.getDayOfMonth() + 7) &&
                    (allApp.get(i).getStart().getDayOfMonth() >=  currentDT.getDayOfMonth())) {
                weekApp.add(allApp.get(i));
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

    public void setAppointmentController(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void allViewClick(ActionEvent actionEvent) {
        scheduleTableView.setItems(allApp);
    }
}