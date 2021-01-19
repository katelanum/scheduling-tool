package project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * TypeSummaryController acts as an interface between the database and TypeSummaryScreen.fxml
 *
 * @author katelanum
 */
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

    /**
     * Populates the allApp list from the database, then initializes the summaries list using intializeSummaries(), and
     * finally sets the table view to display the contents of the summaries list
     */
    public void initialize() {
        Database.initializeAppointmentList(allApp);
        typeTableView.setEditable(true);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        initializeSummaries();
        typeTableView.setItems(summaries);
    }

    /**
     *  Goes through the allApp list and finds each different, case insensitive appointment type, these types are then
     *  added to the typesList. TypesList is then iterated over for each month and each TypeMonthContainer is created
     *  with a count value of 0 and added to the summaries list. The allApp list is then iterated over and each time an
     *  appointment with a matching type and month is found, the count of the TypeMonthContainer of that type and month
     *  has the count increased by 1. Finally, summaries is filtered to only contain those TypeMonthContainers where
     *  the count is not 0.
     *  <p>
     *  There is a lambda used in the filtering of the TypeMonthContainers within the summaries list. It allows for
     *  the list to have all containers where the count equals 0 to be removed from the summaries list.
     */
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

    /**
     * Closes the TypeSummaryScreen when the closeButton is clicked
     */
    public void closeClick() {
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        currentStage.close();
    }
}