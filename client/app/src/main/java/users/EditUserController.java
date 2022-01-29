package users;

import container.DataController;
import container.User;
import helper.HttpRequestException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Objects;

public class EditUserController extends CreateUserController{
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;

        textForename.setText(currentUser.getForename());
        textName.setText(currentUser.getName());
        textLogin.setText(currentUser.getLogin());
        checkButtonIsAdmin.setSelected(currentUser.isAdmin());
    }

    @Override
    protected void createUser(ActionEvent event){
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

        User user = new User();

        if(!textPassword.getText().trim().isEmpty() || !textPasswordSecond.getText().trim().isEmpty()){
            if (textPassword.getText().trim().isEmpty()) {
                labelError.setText("Bitte Passwort eingeben!");
                return;
            }
            if (textPassword.getText().trim().length() < 8) {
                labelError.setText("Das Passwort muss mindestens 8 Zeichen lang sein!");
                return;
            }
            if (!Objects.equals(textPassword.getText(), textPasswordSecond.getText())){
                labelError.setText("Passwörter stimmen nicht überein!");
                return;
            }
            user.setPassword(textPassword.getText().trim());
        }

        user.setUserId(currentUser.getUserId());
        user.setLogin(textLogin.getText().trim());
        user.setForename(textForename.getText().trim());
        user.setName(textName.getText().trim());
        user.setAdmin(checkButtonIsAdmin.isSelected());

        try {
            sendHttpRequest(user);
        } catch (HttpRequestException e) {
            labelError.setText(e.getMessage());
            return;
        }

        if(currentUser.getUserId() == DataController.USER_ID){
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Bitte starte das Programm neu um die Änderungen anzuwenden."
            );
            alert.showAndWait();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    protected void sendHttpRequest(User user) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.editUser(user);
    }
}
