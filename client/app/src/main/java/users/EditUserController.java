package users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.util.Objects;

public class EditUserController {

    public TextField textName;
    public TextField textPassword;
    public TextField textPasswordSecond;
    public ToggleButton checkButtonIsAdmin;
    public TextField textLogin;
    public TextField textForename;
    public Label labelError;

    @FXML
    public void saveUser(ActionEvent event) {
        labelError.setTextFill(Paint.valueOf("Red"));
        if (textLogin.getText().trim().isEmpty()){
            labelError.setText("Bitte Login Namen angeben");
            return;
        }
        if (textForename.getText().trim().isEmpty()) {
            labelError.setText("Bitte Vornamen eingeben!");
            return;
        }
        if (textName.getText().trim().isEmpty()) {
            labelError.setText("Bitte Nachnamen eingeben!");
            return;
        }
        if (textPassword.getText().trim().isEmpty()) {
            labelError.setText("Bitte Passwort eingeben!");
            return;
        }
        if (!Objects.equals(textPassword.getText(), textPasswordSecond.getText())){
            labelError.setText("Passwörter stimmen nicht überein!");
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void abortBtnClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
