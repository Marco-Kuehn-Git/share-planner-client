/* Marco Kühn */
package main;

import com.jfoenix.controls.*;
import config.Config;
import config.ConfigLoader;
import container.HttpRequest;
import helper.HttpRequestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import container.DataController;
import container.User;
import users.EditUserController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionController {

    @FXML
    private JFXButton updateUserBtn;
    @FXML
    private JFXButton deleteUserBtn;
    @FXML
    private JFXButton createUserBtn;
    @FXML
    private JFXToggleButton saveLoginTBtn;
    @FXML
    private Label labelError;
    @FXML
    private GridPane mainGrid;

    private JFXComboBox<String> comboBox;
    private DataController dataController;
    private List<User> users;
    private Config config;

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

        config = ConfigLoader.load();
        if(config == null){
            config = new Config(false, -1, "");
        }
        saveLoginTBtn.setSelected(config.isSaveLogin());
    }

    public void onBackBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onCreateBtnClick(ActionEvent actionEvent) {
        MainApplication.loadScene(
                "User erstellen",
                "../users/create-user.fxml",
                "../users/create-user.css",
                800,
                650
        );
    }

    public void onUpdateBtnClick(ActionEvent actionEvent) {
        int editIndex = comboBox.getSelectionModel().getSelectedIndex();

        if(editIndex < 0 || editIndex >= users.size()) return;

        MainApplication.loadScene(
                "User bearbeiten",
                "../users/edit-user.fxml",
                "../users/create-user.css",
                800,
                650,
                fxmlLoader -> {
                    EditUserController editUserController = fxmlLoader.getController();
                    editUserController.setCurrentUser(users.get(editIndex));
                }
        );
    }

    public void onDeleteBtnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wirklich löschen?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            int removeIndex = comboBox.getSelectionModel().getSelectedIndex();
            try {
                dataController.deleteUser(users.get(removeIndex));
            } catch (HttpRequestException e) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert1.showAndWait();
                return;
            }
            comboBox.getItems().remove(removeIndex);
            users.remove(removeIndex);
        }
    }

    private void setUserAtController(FXMLLoader fxmlLoader){
        int editIndex = comboBox.getSelectionModel().getSelectedIndex();
        EditUserController editUserController = fxmlLoader.getController();
        editUserController.setCurrentUser(users.get(editIndex));
    }

    public void toggledBtn(ActionEvent actionEvent) {
        config.setSaveLogin(saveLoginTBtn.isSelected());
        if(config.isSaveLogin()){
            config.setId(DataController.USER_ID);
            config.setToken(HttpRequest.TOKEN);
        } else {
            config.setId(-1);
            config.setToken("");
        }
        ConfigLoader.save(config);
    }
}
