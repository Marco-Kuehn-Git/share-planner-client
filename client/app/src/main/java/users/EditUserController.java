package users;

import res.User;

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
    }
}
