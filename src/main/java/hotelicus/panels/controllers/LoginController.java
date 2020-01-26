package hotelicus.panels.controllers;

import hotelicus.App;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.DeleteNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.panels.main.AdminPanel;
import hotelicus.panels.main.OwnerPanel;
import hotelicus.panels.main.ReceptionistPanel;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loginButton.setDefaultButton(true);
    }

    @FXML
    private void loginRouter() {
        try {
            if (formValidation()) {
                DbController<Users> handleUserRetrieving=new DbController<>(Users.class);
                Users loggedUser = handleUserRetrieving.selectUnique(Restrictions.eq("username", username.getText()), Restrictions.eq("userState", ACTIVE));

                if (loggedUser != null) {
                    DbController<LoggedUsers> handleLoggedUsers=new DbController<>(LoggedUsers.class);
                    LoggedUsers result = handleLoggedUsers.selectUnique(Restrictions.eq("loggedUser", loggedUser));

                    if (result != null) {
                        if (((new Date().getTime() - result.getLastPinged().getTime()) / 1000) > 60) {
                            handleLoggedUsers.delete(result);
                        }
                    }
                    if (!UserController.isUserLoggedIn(loggedUser)) {
                        App.setLoggedUser(loggedUser);
                        UserController.setUserLoggedIn(loggedUser);

                        switch (loggedUser.getPrivileges()) {
                            case ADMIN:
                                SceneController.changePrimaryScene(AdminPanel.class, "Admin Panel");
                                break;
                            case OWNER:
                                SceneController.changePrimaryScene(OwnerPanel.class, "Owner Panel");
                                break;
                            case RECEPTIONIST:
                                SceneController.changePrimaryScene(ReceptionistPanel.class, "Receptionist Panel");
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

    private boolean formValidation() throws NonUniqueResultException {
        try {
            if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
                DbController<Users> retrieveUser=new DbController<>(Users.class);

                List<Users> users = retrieveUser.select(Restrictions.eq("username", username.getText()), Restrictions.eq("password", password.getText()));
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
