package hotelicus.panels.controllers;

import hotelicus.App;
import hotelicus.core.LoggerUtil;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.DeleteNullObjectException;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.window.Confirmation;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.util.List;

public class UserController {
    public UserController() {
    }

    public static void logOut() {
        try {
            Confirmation logConfirmation = new Confirmation("Message", "Are you sure you want to log off ?");
            if (logConfirmation.getConfirmationResult() == true) {
                UserController.setUserLoggedOff(App.getLoggedUser());
                App.setLoggedUser(null);
                SceneController.changePrimaryScene(LoginController.class, "Login Panel");
                SceneController.wipeStages();
            }
        } catch (IOException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    public static boolean isUserLoggedIn(Users isLogged) {
        try {
            DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
            List<LoggedUsers> result = setUserLoggedIn.select(Restrictions.eq("loggedUser", isLogged));
            if (!result.isEmpty()) {
                return true;
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
        return false;
    }

    public static void setUserLoggedIn(Users candidate) {
        try {
            DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
            LoggedUsers newLoggedUser = new LoggedUsers();
            newLoggedUser.setLoggedUser(candidate);
            setUserLoggedIn.insert(newLoggedUser);
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (ConstraintViolationException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    public static void setUserLoggedOff(Users userToLogOff) {
        try {
            DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
            LoggedUsers result = setUserLoggedIn.selectUnique(Restrictions.eq("loggedUser", userToLogOff));
            setUserLoggedIn.delete(result);
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (DeleteNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }
}
