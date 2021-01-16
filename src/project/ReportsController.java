package project;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportsController {
    public RadioButton typeViewRadio;
    public Stage scheduleStage;
    public Scene scheduleScene;
    public AnchorPane customerPane;
    public ToggleGroup appointmentRadioGroup;
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
    private ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    private ObservableList<Appointment> listByCon = FXCollections.observableArrayList();
    private ObservableList<Appointment> listByType = FXCollections.observableArrayList();
    private ObservableList<Appointment> listByMonth = FXCollections.observableArrayList();
    private ObservableList<Appointment> listByLoc = FXCollections.observableArrayList();
    private ObservableList<String> monthList = FXCollections.observableArrayList();
    private ObservableList<String> typeList = FXCollections.observableArrayList();
    private ObservableList<String> locationList = FXCollections.observableArrayList();
    private ResourceBundle languageBundle = ResourceBundle.getBundle("project/resources", Locale.getDefault());
    private ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    LocalDateTime currentUserDT = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

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
        for (int i = 0; i < appointments.size(); i++) {
            if (!typeList.contains(appointments.get(i).getType())) {
                typeList.add(appointments.get(i).getType());
            }
        }
        typeCombo.setItems(typeList);
    }

    private void initializeLocation() {
        ObservableList<Appointment> appointments =  FXCollections.observableArrayList();
        Database.initializeAppointmentList(appointments);
        for (int i = 0; i < appointments.size(); i++) {
            if (!locationList.contains(appointments.get(i).getLocation())) {
                locationList.add(appointments.get(i).getLocation());
            }
        }
        locationCombo.setItems(locationList);
    }

    public void typeViewClick(ActionEvent actionEvent) {
        listByType.clear();
        if (!typeCombo.getValue().equals(null) && !monthCombo.getValue().equals(null)) {
            String type = typeCombo.getValue();
            String month = monthCombo.getValue();
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getType().equalsIgnoreCase(type)) {
                    listByType.add(allApp.get(i));
                }
            }
            scheduleTableView.setItems(listByType);
        }

    }

    public void contactViewClick(ActionEvent actionEvent) {
        listByCon.clear();
        if (!contactCombo.getValue().equals(null)) {
            Integer contactID = contactCombo.getValue().getContactId();
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getContactId() == contactID) {
                    listByCon.add(allApp.get(i));
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
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getLocation().equalsIgnoreCase(location)) {
                    listByLoc.add(allApp.get(i));
                }
            }
            scheduleTableView.setItems(listByLoc);
        }
    }

    public void typeSelection(ActionEvent actionEvent) {
        listByType.clear();
        if (typeViewRadio.isSelected() && !monthCombo.getValue().equals(null)) {
            String type = typeCombo.getValue();
            String month = monthCombo.getValue();
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getType().equalsIgnoreCase(type)) {
                    listByType.add(allApp.get(i));
                }
            }
            scheduleTableView.setItems(listByType);
        }
    }

    public void contactSelection(ActionEvent actionEvent) {
        listByCon.clear();
        if (contactViewRadio.isSelected()) {
            Integer contactID = contactCombo.getValue().getContactId();
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getContactId() == contactID) {
                    listByCon.add(allApp.get(i));
                }
            }
            scheduleTableView.setItems(listByCon);
        }
    }

    public void monthSelection(ActionEvent actionEvent) {
        listByType.clear();
        if (typeViewRadio.isSelected() && !typeCombo.getValue().equals(null)) {
            String type = typeCombo.getValue();
            String month = monthCombo.getValue();
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getType().equalsIgnoreCase(type)) {
                    listByType.add(allApp.get(i));
                }
            }
            scheduleTableView.setItems(listByType);
        }
    }

    public void locationSelection(ActionEvent actionEvent) {
        listByLoc.clear();
        if (locationRadio.isSelected()) {
            String location = locationCombo.getValue();
            for (int i = 0; i < allApp.size(); i++) {
                if (allApp.get(i).getLocation().equalsIgnoreCase(location)) {
                    listByLoc.add(allApp.get(i));
                }
            }
            scheduleTableView.setItems(listByLoc);
        }
    }
}
