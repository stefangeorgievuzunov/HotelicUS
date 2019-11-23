package hotelicus.controllers.extended.Users;

import hotelicus.App;
import hotelicus.entities.Users;
import hotelicus.window.Error;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("all")
public class UserDbController {
    private static Session session;

    public UserDbController(){
        this.session= App.getSession();
    }

    public static  Users selectUniqueUser(String username)throws NonUniqueResultException {
        UserDbController.session.beginTransaction();
        Criteria crit=UserDbController.session.createCriteria(Users.class);
        crit.add(Restrictions.eq("username", username));

        Users result=(Users)crit.uniqueResult();
        UserDbController.session.getTransaction().commit();
        return result;

        }

    public static boolean usernamePasswordValidator(String username,String password) {
        UserDbController.session.beginTransaction();
        Criteria crit=UserDbController.session.createCriteria(Users.class);
        crit.add(Restrictions.eq("username", username));
        crit.add(Restrictions.eq("password", password));
        List<Users> users=crit.list();

        UserDbController.session.getTransaction().commit();
        if(users.size()<1){
            new Error("Failed to login","Invalid username or password!");
            return false;
        }else{
            return true;
        }
    }
}
