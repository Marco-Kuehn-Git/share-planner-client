package main;

import com.jfoenix.controls.*;
import helper.HttpRequestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import container.DataController;
import container.Event;

import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;

public class CreateEventController {

    @FXML
    public GridPane mainGrid;
    @FXML
    public JFXDatePicker datePickerDate;
    @FXML
    public JFXTextField textName;
    @FXML
    public JFXComboBox<String> ComboBoxPriotity;
    @FXML
    public JFXToggleButton toggleBtnIsFullDay;
    @FXML
    public JFXToggleButton toggleBtnIsPrivate;
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
        timeStart.set24HourView(true);
        timeStart.setConverter(defaultConverter);

        timeEnd.set24HourView(true);
        timeEnd.setConverter(defaultConverter);
    }


    @FXML
    protected void createBtnClick(ActionEvent actionEvent) {
        try {
            if (datePickerDate.getValue() == null) {
                throw new IllegalArgumentException("Bitte w\u00e4hle ein Datum aus");
            }

            System.out.println(datePickerDate.getValue());

            Event event = new Event(
                    textName.getText(),
                    ComboBoxPriotity.getSelectionModel().getSelectedIndex(),
                    toggleBtnIsFullDay.isSelected(),
                    toggleBtnIsPrivate.isSelected(),
                    timeStart.getValue(),
                    timeEnd.getValue(),
                    datePickerDate.getValue().atStartOfDay(),
                    (int) DataController.USER_ID
            );

            System.out.println(event.getAsUrlParam());

            sendHttpRequest(event);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            labelError.setText(e.getMessage());
        }
    }

    protected void sendHttpRequest(Event event) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.createEvent(event);
    }

    @FXML
    protected void abortBtnClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
