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
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportsController {
    public RadioButton typeViewRadio;
    public Stage scheduleStage;
    public Scene scheduleScene;
    public AnchorPane customerPane;
    public RadioButton contactViewRadio;
    public Text reportsTitle;
    public Button reportsClose;
    public ComboBox<String> typeCombo;
    public ComboBox<Contacts> contactCombo;
    public ComboBox<String> monthCombo;
    public RadioButton locationRadio;
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
    public ComboBox<String> locationCombo;
    public ToggleGroup reportRadioGroup;
    public Button typeSummaryButton;
    private final ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    private final ObservableList<Appointment> listByCon = FXCollections.observableArrayList();
    private final ObservableList<Appointment> listByType = FXCollections.observableArrayList();
    private final ObservableList<Appointment> listByLoc = FXCollections.observableArrayList();
    private final ObservableList<String> monthList = FXCollections.observableArrayList();
    private final ObservableList<String> typeList = FXCollections.observableArrayList();
    private final ObservableList<String> locationList = FXCollections.observableArrayList();
    private final ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

    public void initialize() throws SQLException {
        initializeContact();
        initializeType();
        initializeMonth();
        initializeLocation();
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

    private void initializeContact() throws SQLException {
        contactList.clear();
        Database.popContactsList(contactList);
        contactCombo.setItems(contactList);
    }

    private void initializeType() {
        ObservableList<Appointment> appointments =  FXCollections.observableArrayList();
        Database.initializeAppointmentList(appointments);
        for (Appointment appointment : appointments) {
            if (!typeList.contains(appointment.getType())) {
                typeList.add(appointment.getType());
            }
        }
        typeCombo.setItems(typeList);
    }

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

    public void typeViewClick(ActionEvent actionEvent) {
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

    public void contactViewClick(ActionEvent actionEvent) {
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

    public void reportsCloseClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) reportsClose.getScene().getWindow();
        currentStage.close();
    }

    public void locationViewClick(ActionEvent actionEvent) {
        listByLoc.clear();
        if (!locationCombo.getValue().isEmpty()) {
            String location = locationCombo.getValue();
            for (Appointment appointment : allApp) {
                if (appointment.getLocation().equalsIgnoreCase(location)) {
                    listByLoc.add(appointment);
                }
            }
            scheduleTableView.setItems(listByLoc);
        }
    }

    public void typeSelection(ActionEvent actionEvent) {
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

    public void contactSelection(ActionEvent actionEvent) {
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

    public void monthSelection(ActionEvent actionEvent) {
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

    public void locationSelection(ActionEvent actionEvent) {
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

    public void typeSummaryClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("typeSummaryScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        stage.show();
    }
}
