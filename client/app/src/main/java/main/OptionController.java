package main;

import com.jfoenix.controls.*;
import helper.HttpRequestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import res.DataController;
import res.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public GridPane mainGrid;

    private JFXComboBox<String> comboBox;
    private DataController dataController;
    private List<User> users;

    @FXML
    public void initialize(){
        dataController = new DataController();
        try{
           users = dataController.getAllUser();
        } catch (HttpRequestException e){
           users = new ArrayList<>();
        }

        ObservableList<String> observableUserList = FXCollections.observableArrayList();
        for (User user: users) {
            observableUserList.add(user.getLogin());
        }
        comboBox = new JFXComboBox<>(observableUserList);
        comboBox.getStyleClass().add("comboBox");
        mainGrid.add(comboBox, 2,2);

    }

    public void onBackBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onCreateBtnClick(ActionEvent actionEvent) {
        loadUserScene(actionEvent, "User erstellen", "../users/create-user.fxml");
    }

    public void onUpdateBtnClick(ActionEvent actionEvent) {
        loadUserScene(actionEvent, "User bearbeiten", "../users/edit-user.fxml");
    }

    public void onDeleteBtnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wirklich löschen?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            try {
                dataController.deleteUser(users.get(comboBox.getSelectionModel().getSelectedIndex()));
            } catch (HttpRequestException e) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert1.showAndWait();
            }
        }
    }

    private Scene loadUserScene(ActionEvent actionEvent, String title, String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource(fxml));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 650);
            scene.getStylesheets().add(Objects.requireNonNull(
                    MainApplication.class.getResource("../users/create-user.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            Stage stageOld = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }
}
