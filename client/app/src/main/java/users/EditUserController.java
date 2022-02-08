/* Marc Beyer */
package users;

import container.DataController;
import container.User;
import helper.HttpRequestException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
        if (validateNameAndLogin()) return;

        User user = new User();

        if(!textPassword.getText().trim().isEmpty() || !textPasswordSecond.getText().trim().isEmpty()){
            if (validatePassword()) return;
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
