//Alex Rechtin//
package users;

import helper.HttpRequestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import container.DataController;
import container.User;

import java.util.Objects;

public class CreateUserController {

    @FXML
    protected TextField textName;
    @FXML
    protected PasswordField textPassword;
    @FXML
    protected PasswordField textPasswordSecond;
    @FXML
    protected ToggleButton checkButtonIsAdmin;
    @FXML
    protected TextField textLogin;
    @FXML
    protected TextField textForename;
    @FXML
    protected Label labelError;

    @FXML
    protected void createUser(ActionEvent event) {
        if (validateNameAndLogin() || validatePassword()) return;

        User user = new User();
        user.setLogin(textLogin.getText().trim());
        user.setForename(textForename.getText().trim());
        user.setName(textName.getText().trim());
        user.setPassword(textPassword.getText().trim());
        user.setAdmin(checkButtonIsAdmin.isSelected());

        try {
            sendHttpRequest(user);
        } catch (HttpRequestException e) {
            labelError.setText(e.getMessage());
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    protected boolean validatePassword() {
        if (textPassword.getText().trim().isEmpty()) {
            labelError.setText("Bitte Passwort eingeben!");
            return true;
        }
        if (textPassword.getText().trim().length() < 8) {
            labelError.setText("Das Passwort muss mindestens 8 Zeichen lang sein!");
            return true;
        }
        if (!Objects.equals(textPassword.getText(), textPasswordSecond.getText())){
            labelError.setText("Passwörter stimmen nicht überein!");
            return true;
        }
        return false;
    }

    protected boolean validateNameAndLogin() {
        if (textLogin.getText().trim().isEmpty()){
            labelError.setText("Bitte Login Namen angeben");
            return true;
        }
        if (textForename.getText().trim().isEmpty()) {
            labelError.setText("Bitte Vornamen eingeben!");
            return true;
        }
        if (textName.getText().trim().isEmpty()) {
            labelError.setText("Bitte Nachnamen eingeben!");
            return true;
        }
        return false;
    }

    protected void sendHttpRequest(User user) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.createUser(user);
    }

    @FXML
    protected void abortBtnClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
