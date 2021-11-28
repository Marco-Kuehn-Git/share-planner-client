package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import res.DataController;
import res.Event;

public class CreateEventController {

    @FXML
    public DatePicker datePickerDate;
    @FXML
    public TextField textName;
    @FXML
    public TextField textStart;
    @FXML
    public TextField textEnd;
    @FXML
    public ComboBox<String> ComboBoxTyp;
    @FXML
    public ComboBox<String> ComboBoxPriotity;
    @FXML
    public CheckBox checkBoxIsFullDay;
    @FXML
    public CheckBox checkBoxIsPrivate;


    public CreateEventController(){}

    @FXML
    public void initialize(){}


    @FXML
    protected void createBtnClick(ActionEvent actionEvent){

        Event event = new Event(
                textName.getText(),
                ComboBoxPriotity.getSelectionModel().getSelectedIndex(),
                checkBoxIsFullDay.isSelected(),
                checkBoxIsPrivate.isSelected(),
                textStart.getText(),
                textEnd.getText(),
                datePickerDate.getValue().atStartOfDay(),
                1
        );

        System.out.println(event.getAsUrlParam());

        DataController dataController = new DataController();
        dataController.createEvent(event);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void abortBtnClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
