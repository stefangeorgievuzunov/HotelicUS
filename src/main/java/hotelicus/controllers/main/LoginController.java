package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.DeleteNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
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
        try {
            this.usersRelated = new DbController<Users>(Users.class);
            this.handleLoggedUser = new DbController<LoggedUsers>(LoggedUsers.class);
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loginButton.setDefaultButton(true);
    }

    @FXML
    private void loginRouter() {
        try {
            if (loginValidation(username.getText(), password.getText())) {

                Users loggedUser = this.usersRelated.selectUnique(Restrictions.eq("username", username.getText()), Restrictions.eq("userState", ACTIVE));
                if (loggedUser != null) {
                    LoggedUsers result = this.handleLoggedUser.selectUnique(Restrictions.eq("loggedUser", loggedUser));
                    if (result != null) {
                        if (((new Date().getTime() - result.getLastPinged().getTime()) / 1000) > 60) {
                            this.handleLoggedUser.delete(result);
                        }
                    }
                    if (!UserController.isUserLoggedIn(loggedUser)) {
                        App.setLoggedUser(loggedUser);
                        UserController.setUserLoggedIn(loggedUser);
                        switch (loggedUser.getPrivileges()) {
                            case ADMIN:
                                Monitor.changePrimaryScene(AdminPanel.class, "Admin Panel");
                                break;
                            case OWNER:
                                Monitor.changePrimaryScene(OwnerPanel.class, "Owner Panel");
                                break;
                        }
                    } else {
                        new Error("Login Failed", "User is already logged in ! Try again later.");
                    }
                } else {
                    throw new SelectNullObjectException();
                }
            }
        } catch (IOException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        } catch (NonUniqueResultException excep) {
            excep.printStackTrace();
        } catch (DeleteNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private boolean loginValidation(String username, String password) throws NonUniqueResultException {
        try {
            if (!username.isEmpty() && !password.isEmpty()) {

                List<Users> users = this.usersRelated.select(Restrictions.eq("username", username), Restrictions.eq("password", password));
                if (users.size() < 1) {
                    throw new SelectNullObjectException();
                } else if (users.size() > 1) {
                    throw new NonUniqueResultException(0);
                } else {
                    return true;
                }
            } else {
                new Error("Failed to login", "There are empty fields!");
                return false;
            }
        } catch (NonUniqueResultException excep) {
            excep.printStackTrace();
            new Error("Failed to login", "Invalid username or password!");
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            new Error("Failed to login", "Invalid username or password!");
        }
        return false;
    }
}
