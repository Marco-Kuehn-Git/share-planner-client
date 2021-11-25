package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import res.Event;
import res.DataController;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

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
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("create-event.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 850, 500);
            scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("create-event.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Termin erstellen");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }


        DataController dataController = new DataController();
        Event[] eventList = dataController.getAllEvents();

        for(Event event : eventList){
            Label label = new Label();
            label.setText(event.toString());
            label.setTextFill(Color.WHITE);
            vBoxWen.getChildren().add(label);
        }
    }

    @FXML
    protected void createBtnClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void abortBtnClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}