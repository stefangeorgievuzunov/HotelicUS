package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserDbController;
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
        try {
            if (loginValidation(username.getText(), password.getText())) {
                UserDbController loginControl = new UserDbController();
                try {
                    Users loggedUser = loginControl.selectUniqueUser(username.getText());
                    if (loggedUser != null) {
                        App.setLoggedUser(loggedUser);
                    } else {
                        new Error("FATAL ERROR", "User doesn't exist.");
                    }
                } catch (NonUniqueResultException excep) {
                    excep.printStackTrace();
                }
                System.out.println(App.getLoggedUser().getUsername());
                App.dashboardWindow();
            }
        } catch (NonUniqueResultException excp) {
            excp.printStackTrace();
        }

    }

    private boolean loginValidation(String username, String password) throws NonUniqueResultException {
        if (!username.isEmpty() && !password.isEmpty()) {
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
