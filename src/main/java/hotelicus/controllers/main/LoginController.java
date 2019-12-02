package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.UserState.ACTIVE;


public class LoginController implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    private DbController<Users> usersRelated;
    private DbController<LoggedUsers> handleLoggedUser;

    public LoginController() {
        this.usersRelated = new DbController<Users>(Users.class);
        this.handleLoggedUser = new DbController<LoggedUsers>(LoggedUsers.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loginButton.setDefaultButton(true);
    }

    @FXML
    private void loginRouter() {
        if (loginValidation(username.getText(), password.getText())) {
            try {
                Users loggedUser = this.usersRelated.selectUnique(Restrictions.eq("username", username.getText()), Restrictions.eq("userState", ACTIVE));
                if (loggedUser != null) {
                    if (!UserController.isUserLoggedIn(loggedUser)) {
                        App.setLoggedUser(loggedUser);
                        UserController.setUserLoggedIn(loggedUser);
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
                        new Error("Login Failed", "User is already logged in ! Try again later.");
                        LoggedUsers result = this.handleLoggedUser.selectUnique(Restrictions.eq("loggedUser", loggedUser));
                        if (((new Date().getTime()-result.getLastPinged().getTime())/1000)>60) {
                            this.handleLoggedUser.delete(result);
                        }
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
            try {
                List<Users> users = this.usersRelated.select(Restrictions.eq("username", username), Restrictions.eq("password", password));
                if (users.size() < 1) {
                    throw new NullPointerException();
                } else if (users.size() > 1) {
                    throw new NonUniqueResultException(0);
                } else {
                    return true;
                }
            } catch (Exception excep) {
                if (excep instanceof NonUniqueResultException) {
                    new Error("Failed to login", "Invalid username or password!");
                }
                if (excep instanceof NullPointerException) {
                    new Error("Failed to login", "Invalid username or password!");
                }
                return false;
            }
        } else {
            new Error("Failed to login", "There are empty fields!");
            return false;
        }
    }
}
