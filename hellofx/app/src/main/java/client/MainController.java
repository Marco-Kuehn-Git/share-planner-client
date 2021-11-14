package client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import res.Event;
import res.RestController;

public class MainController {
    @FXML
    private GridPane calendarGrid;

    @FXML
    private VBox vBoxMon;
    @FXML
    private VBox vBoxTue;
    @FXML
    private VBox vBoxWen;
    @FXML
    private VBox vBoxThu;
    @FXML
    private VBox vBoxFri;
    @FXML
    private VBox vBoxSat;
    @FXML
    private VBox vBoxSun;


    @FXML
    protected void onAddBtnClick(){
        RestController restController = new RestController();
        Event[] eventList = restController.getAllEvents();

        for(Event event : eventList){
            Label label = new Label();
            label.setText(event.toString());
            label.setTextFill(Color.WHITE);
            vBoxWen.getChildren().add(label);
        }
    }
}