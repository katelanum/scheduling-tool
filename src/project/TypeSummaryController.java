package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TypeSummaryController {
    public Text typeSummTitle;
    public Stage typeSummStage;
    public Scene typeSummScene;
    public AnchorPane typeSummPane;
    public Button closeButton;
    public TableView<TypeMonthContainer> typeTableView;
    public TableColumn<TypeMonthContainer, String> monthColumn;
    public TableColumn<TypeMonthContainer, String> typeColumn;
    public TableColumn<TypeMonthContainer, Integer> countColumn;
    private final ObservableList<Appointment> allApp = FXCollections.observableArrayList();
    private ObservableList<TypeMonthContainer> summaries = FXCollections.observableArrayList();
    private final ObservableList<String> typesList = FXCollections.observableArrayList();

    public void initialize() {
        Database.initializeAppointmentList(allApp);
        typeTableView.setEditable(true);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        initializeSummaries();
        typeTableView.setItems(summaries);
    }

    private void initializeSummaries() {
        for (Appointment appointment : allApp) {
            if (!typesList.contains(appointment.getType().toLowerCase())) {
                typesList.add(appointment.getType().toLowerCase());
            }
        }
        for (String s : typesList) {
            for (int j = 1; j < 13; j++) {
                TypeMonthContainer tempContainer = new TypeMonthContainer();
                tempContainer.setCount(0);
                tempContainer.setMonthNum(j);
                tempContainer.setType(s);
                summaries.add(tempContainer);
            }
        }
        for (Appointment appointment : allApp) {
            for (TypeMonthContainer summary : summaries) {
                if (appointment.getStart().getMonth().getValue() == summary.getMonthNum() &&
                        appointment.getType().equalsIgnoreCase(summary.getType())) {
                    summary.setCount(summary.getCount() + 1);
                }
            }
        }
        summaries = summaries.filtered(container -> container.getCount() != 0);
    }

    public void closeClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        currentStage.close();
    }
}