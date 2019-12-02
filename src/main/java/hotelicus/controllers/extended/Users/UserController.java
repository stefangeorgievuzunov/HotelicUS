package hotelicus.controllers.extended.Users;

import hotelicus.controllers.main.DbController;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import org.hibernate.criterion.Restrictions;

import java.util.List;
public class UserController {
    public UserController() {
    }

    public static boolean isUserLoggedIn(Users isLogged) {
        DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
        List<LoggedUsers> result = setUserLoggedIn.select(Restrictions.eq("loggedUser", isLogged));
        if (result.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void setUserLoggedIn(Users candidate) {
        DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
        LoggedUsers newLoggedUser = new LoggedUsers();
        newLoggedUser.setLoggedUser(candidate);
        setUserLoggedIn.insert(newLoggedUser);
    }

    public static void setUserLoggedOff(Users userToLogOff) {
        DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
        LoggedUsers result = setUserLoggedIn.selectUnique(Restrictions.eq("loggedUser", userToLogOff));
        setUserLoggedIn.delete(result);
    }
}
