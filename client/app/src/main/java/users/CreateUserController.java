package users;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class CreateUserController {

    public TextField textName;
    public TextField textPassword;
    public TextField textPasswordSecond;
    public CheckBox checkBoxIsAdmin;
    public Label labelErrorName;
    public Label labelErrorPw;

    protected void createUser(ActionEvent event) {
        if (textName.getText().trim().isEmpty()) {
            labelErrorName.setText("Bitte Usernamen eingeben!");
            labelErrorPw.setText("");
            return;
        }
        if (textPassword.getText().trim().isEmpty()) {
            labelErrorName.setText("");
            labelErrorPw.setText("Bitte Passwort eingeben!");
            return;
        }
        if (Objects.equals(textPassword.getText(), textPasswordSecond.getText())){
            labelErrorName.setText("");
            labelErrorPw.setText("Passwörter stimmen nicht überein!");
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
