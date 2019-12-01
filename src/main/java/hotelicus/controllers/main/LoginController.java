package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.entities.Users;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.NonUniqueResultException;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    public LoginController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loginButton.setDefaultButton(true);
    }

    @FXML
    private void loginRouter() {
        if (loginValidation(username.getText(), password.getText())) {
            try {
                Users loggedUser = UserController.selectUniqueUser(username.getText());
                if (loggedUser != null) {
                    if (!UserController.isUserLoggedIn(loggedUser)) {
                        UserController.setUserLoggedIn(loggedUser);
                        App.setLoggedUser(loggedUser);
                        switch (loggedUser.getPrivileges()) {
                            case ADMIN:
                                App.adminWindow();
                                break;
                            case OWNER:
                                App.ownerWindow();
                                break;
                            default:
                                throw new Exception();
                        }
                    } else {
                        new Error("Login Failed", "User is already logged in !");
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception excep) {
                excep.printStackTrace();
                new Error("FATAL ERROR", "Something went wrong.");
            }
        }
    }

    private boolean loginValidation(String username, String password) throws NonUniqueResultException {
        if (!username.isEmpty() && !password.isEmpty()) {
            if (UserController.usernamePasswordValidator(username, password))
                return true;
            else
                return false;
        } else {
            new Error("Failed to login", "There are empty fields!");
            return false;
        }
    }
}
