package hotelicus.controllers.extended.Users;

import hotelicus.App;
import hotelicus.controllers.main.DbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.enums.HotelState;
import hotelicus.window.Error;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

@SuppressWarnings("all")
public class UserController {
    private static Session session=App.getSession();

    public UserController(){

    }

    public static Users selectUniqueUser(String username){
        UserController.session.beginTransaction();
        Criteria crit = UserController.session.createCriteria(Users.class);
        crit.add(Restrictions.eq("username", username));

        Users result = (Users) crit.uniqueResult();
        UserController.session.getTransaction().commit();
        return result;

    }

    public static boolean usernamePasswordValidator(String username, String password) {
        System.out.println(username+" "+ password);
       try{
           UserController.session.beginTransaction();
           Criteria crit = UserController.session.createCriteria(Users.class);
           crit.add(Restrictions.eq("username",username));
           crit.add(Restrictions.eq("password",password));
           List<Users> users = crit.list();

           UserController.session.getTransaction().commit();
           if (users.size() < 1) {
               throw new NullPointerException();
           }else if(users.size()>1){
               throw new NonUniqueResultException(0);
           }else{
               return true;
           }
       }
        catch(Exception excep){
           if(excep instanceof NonUniqueResultException){
               new Error("Failed to login", "Invalid username or password!");
           }
           if(excep instanceof NullPointerException) {
               new Error("Failed to login", "Invalid username or password!");
           }
            return false;
        }
    }

    public static List<Hotels> selectOwnerHotels(Users hotelOwner, HotelState hotelState){
        UserController.session.beginTransaction();
        Criteria crit = UserController.session.createCriteria(Hotels.class);
        crit.add(Restrictions.eq("owner", hotelOwner));
        crit.add(Restrictions.eq("hotelState", hotelState));
        List<Hotels> hotels = crit.list();
        UserController.session.getTransaction().commit();
        return hotels;
    }

    public static boolean isUserLoggedIn(Users isLogged){
        DbController<LoggedUsers> setUserLoggedIn=new DbController<LoggedUsers>(LoggedUsers.class);
        List<LoggedUsers> result=setUserLoggedIn.selectEqualTo("loggedUser",isLogged,false);
        if(result.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    public static void setUserLoggedIn(Users candidate){
        DbController<LoggedUsers> setUserLoggedIn=new DbController<LoggedUsers>(LoggedUsers.class);
        LoggedUsers newLoggedUser=new LoggedUsers();
        newLoggedUser.setLoggedUser(candidate);
        setUserLoggedIn.insert(newLoggedUser);
    }

    public static void setUserLoggedOff(Users userToLogOff){

        UserController.session.beginTransaction();
        Criteria crit = UserController.session.createCriteria(LoggedUsers.class);
        crit.add(Restrictions.eq("loggedUser", userToLogOff));
        LoggedUsers result = (LoggedUsers) crit.uniqueResult();
        UserController.session.delete(result);
        UserController.session.getTransaction().commit();
    }
}
