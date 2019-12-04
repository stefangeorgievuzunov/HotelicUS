package hotelicus.controllers.extended.Users;

import hotelicus.App;
import hotelicus.controllers.main.DbController;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.DeleteNullObjectException;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.window.Confirmation;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class UserController {
    public UserController() {
    }

    public static void logOut() {
        Confirmation logConfirmation = new Confirmation("Message", "Are you sure you want to log off ?");
        if (logConfirmation.getConfirmationResult() == true) {
            UserController.setUserLoggedOff(App.getLoggedUser());
            App.setLoggedUser(null);
            App.loginWindow();
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
        }catch(SelectNullObjectException excep){
            excep.printStackTrace();
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
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        } catch (ConstraintViolationException excep) {
            excep.printStackTrace();
        }
    }

    public static void setUserLoggedOff(Users userToLogOff) {
        try {
            DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
            LoggedUsers result = setUserLoggedIn.selectUnique(Restrictions.eq("loggedUser", userToLogOff));
            setUserLoggedIn.delete(result);
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (DeleteNullObjectException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }
}
