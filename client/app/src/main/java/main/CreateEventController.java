package main;

import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import res.DataController;
import res.Event;

import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;

public class CreateEventController {

    @FXML
    public GridPane mainGrid;
    @FXML
    public DatePicker datePickerDate;
    @FXML
    public TextField textName;
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

        StringConverter<LocalTime> defaultConverter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.GERMANY);

        JFXTimePicker timePickerStart = new JFXTimePicker();
        timeStart = timePickerStart;
        timePickerStart.set24HourView(true);
        timePickerStart.setConverter(defaultConverter);
        timePickerStart.getStyleClass().add("timePicker");
        mainGrid.add(timePickerStart, 1 , 3);

        JFXTimePicker timePickerEnd = new JFXTimePicker();
        timeEnd = timePickerEnd;
        timePickerEnd.set24HourView(true);
        timePickerEnd.setConverter(defaultConverter);
        timePickerEnd.getStyleClass().add("timePicker");
        mainGrid.add(timePickerEnd, 1 , 4);
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
                    timeStart.getValue().toString(),
                    timeEnd.getValue().toString(),
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
