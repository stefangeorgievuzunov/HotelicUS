package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserDbController;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public LoginController() {
    }

    @FXML
    private void loginRouter() {
        if (loginValidation(username.getText(), password.getText())) {
            App.dashboardWindow();
            return;
        }
    }

    private boolean loginValidation(String username, String password) {
        if (!username.equals("") && !password.equals("")) {
            UserDbController loginControl = new UserDbController();

            if (loginControl.usernamePasswordValidator(username, password))
                return true;
            else
                return false;
        } else {
            new Error("Failed to login", "There are empty fields!");
            return false;
        }
    }
}
