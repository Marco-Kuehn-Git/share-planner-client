package main;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OptionController {

    @FXML
    public JFXButton updateUserBtn;
    @FXML
    public JFXButton deleteUserBtn;
    @FXML
    public JFXButton createUserBtn;
    @FXML
    public JFXToggleButton saveLoginTBtn;
    @FXML
    public Label labelError;
    @FXML
    public JFXComboBox<String> userCmb;

    @FXML
    public void initialize(){

    }

    public void onBackBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onCreateBtnClick(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource("../users/create-user.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 650);
            scene.getStylesheets().add(Objects.requireNonNull(
                    MainApplication.class.getResource("../users/create-user.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("User erstellen");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            Stage stageOld = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateBtnClick(ActionEvent actionEvent) {
    }

    public void onDeleteBtnClick(ActionEvent actionEvent) {
    }
}
