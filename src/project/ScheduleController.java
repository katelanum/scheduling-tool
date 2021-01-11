package project;

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
    public TableColumn<Appointment, Date> appointmentStartColumn;
    public TableColumn<Appointment, Date> appointmentEndColumn;
    public TableColumn<Appointment, Integer> appointmentCustomerIdColumn;
    private ObservableList<Appointment> weekApp;
    private ObservableList<Appointment> monthApp;
    private Date currentDate = Date.from(Instant.now());
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));

    public void openAppointmentClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
        Stage loader = FXMLLoader.load(getClass().getResource("AppointmentScreen.fxml"), languageBundle);
        loader.show();
    }

    public void openCustomerClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", new Locale("fr"));
        Stage loader = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"), languageBundle);
        loader.show();
    }

    public void scheduleCloseClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) scheduleClose.getScene().getWindow();
        currentStage.close();
    }

    public void monthViewClick(ActionEvent actionEvent) {
        int size = Main.getAppointmentList().size();
        for (int i = 0; i < Main.getAppointmentList().size(); i++) {
            if (Main.getAppointmentList().get(i).getStart().getMonth() != currentDate.getMonth()) {
                monthApp.add(Main.getAppointmentList().get(i));
            }
        }
        scheduleTableView.setItems(monthApp);
    }

    public void weekViewClick(ActionEvent actionEvent) {
        int days = 7;
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(currentDate);
        tempCal.add(Calendar.DAY_OF_YEAR, days);
        Date weekOut = tempCal.getTime();
        int size = Main.getAppointmentList().size();
        for (int i =0; i < size; i++) {
            if (Main.getAppointmentList().get(i).getStart().before(weekOut) && (Main.getAppointmentList().get(i).getStart().after(currentDate))) {
                weekApp.add(Main.getAppointmentList().get(i));
            }
        }
        scheduleTableView.setItems(weekApp);
    }

    public void initialize(){
        scheduleTableView.setEditable(true);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        scheduleTableView.setPlaceholder(new Label(languageBundle.getString("noApp")));
        if (!Main.getAppointmentList().isEmpty()) {
            scheduleTableView.setItems(Main.getAppointmentList());
        }
    }
}
