package hotelicus.controllers.extended;

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
    private Session session;

    public UserDbController(){
        this.session= App.getSession();
    }

    public Users selectUniqueUser(String username)throws NonUniqueResultException {
        this.session.beginTransaction();
        Criteria crit=this.session.createCriteria(Users.class);
        crit.add(Restrictions.eq("username", username));

        Users result=(Users)crit.uniqueResult();
        this.session.getTransaction().commit();

        return result;
    }

    public boolean usernamePasswordValidator(String username,String password)throws IOException {
        this.session.beginTransaction();
        Criteria crit=this.session.createCriteria(Users.class);
        crit.add(Restrictions.eq("username", username));
        crit.add(Restrictions.eq("password", password));
        List<Users> users=crit.list();

        this.session.getTransaction().commit();
        if(users.size()<1){
            new Error("Failed to login","Invalid username or password!");
            return false;
        }else{
            return true;
        }
    }
}
