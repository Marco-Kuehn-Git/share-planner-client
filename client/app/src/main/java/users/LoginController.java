//Alex Rechtin//
package users;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import res.DataController;

public class LoginController {
    @FXML
    public JFXTextField userField;
    @FXML
    public JFXPasswordField passField;
    @FXML
    public Label userErrLabel;
    @FXML
    public Label passErrLabel;

    @FXML
    protected void login(ActionEvent event) {
        if (userField.getText().trim().isEmpty()) {
            userErrLabel.setText("Bitte Usernamen eingeben!");
            passErrLabel.setText("");
            return;
        }
        if (passField.getText().trim().isEmpty()) {
            userErrLabel.setText("");
            passErrLabel.setText("Bitte Passwort eingeben!");
            return;
        }

        DataController dataController = new DataController();
        if (!dataController.login(userField.getText(), passField.getText())) {
            userErrLabel.setText("Name und Passwort passen nicht zueinander!");
            passErrLabel.setText("Name und Passwort passen nicht zueinander!");
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void abortBtnClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
