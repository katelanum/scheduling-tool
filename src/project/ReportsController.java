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
    public TableColumn<Appointment,Integer> appointmentIdColumn;

    public void initialize() throws SQLException {
        initializeContact();
        initializeType();
        initializeMonth();
        initializeLocation();
        Database.initializeAppointmentList(allApp);
        scheduleTableView.setEditable(true);
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
            if (!typeList.contains(appointment.getType().toLowerCase())) {
                typeList.add(appointment.getType().toLowerCase());
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TypeSummaryScreen.fxml"), languageBundle);
        Stage stage = loader.load();
        stage.show();
    }
}
