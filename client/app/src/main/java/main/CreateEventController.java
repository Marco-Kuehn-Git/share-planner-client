package main;

import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import res.DataController;
import res.Event;

public class CreateEventController {

    @FXML
    public GridPane grid;
    @FXML
    public DatePicker datePickerDate;
    @FXML
    public TextField textName;
    @FXML
    public ComboBox<String> ComboBoxTyp;
    @FXML
    public ComboBox<String> ComboBoxPriotity;
    @FXML
    public CheckBox checkBoxIsFullDay;
    @FXML
    public CheckBox checkBoxIsPrivate;
    @FXML
    public Label labelError;
    @FXML
    public JFXTimePicker timeStart;
    @FXML
    public JFXTimePicker timeEnd;


    public CreateEventController() {
    }

    @FXML
    public void initialize() {
        JFXTimePicker timePickerStart = new JFXTimePicker();
        timeStart = timePickerStart;
        timePickerStart.set24HourView(true);
        grid.add(timePickerStart, 1 , 3);

        JFXTimePicker timePickerEnd = new JFXTimePicker();
        timeEnd = timePickerEnd;
        timePickerEnd.set24HourView(true);
        grid.add(timePickerEnd, 1 , 4);
    }


    @FXML
    protected void createBtnClick(ActionEvent actionEvent) {
        try {
            if (datePickerDate.getValue() == null) {
                throw new IllegalArgumentException("Bitte w\u00e4hle ein Datum aus");
            }

            Event event = new Event(
                    textName.getText(),
                    ComboBoxPriotity.getSelectionModel().getSelectedIndex(),
                    checkBoxIsFullDay.isSelected(),
                    checkBoxIsPrivate.isSelected(),
                    timeStart.toString(),
                    timeEnd.toString(),
                    datePickerDate.getValue().atStartOfDay(),
                    (int) DataController.USER_ID
            );

            System.out.println(event.getAsUrlParam());

            DataController dataController = new DataController();
            dataController.createEvent(event);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } catch (RuntimeException e) {
            labelError.setText(e.getMessage());
        }
    }

    @FXML
    protected void abortBtnClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
