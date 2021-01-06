package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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
    public TableView scheduleTableView;

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
    }

    public void weekViewClick(ActionEvent actionEvent) {
    }
}
