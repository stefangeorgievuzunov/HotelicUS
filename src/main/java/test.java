import hotelicus.HibernateUtil;
import hotelicus.entities.Users;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class test {
    public static void test() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Users> employeeList = session.createQuery("FROM " + Users.class.getSimpleName()).list();

        Users user = new Users();
        user.setUsername("Test 1");
        user.setPassword("Password");
        user.setPrivileges(UserPrivileges.OWNER);
        user.setFirstName("Papa");
        user.setLastName("Meme");
        user.setPhoneNumber("+355555555");
        user.setUserState(UserState.ACTIVE);
        user.setCreatedOn(new Date());

        session.save(user);
        session.getTransaction().commit();

        for(Users employee : employeeList) {
            System.out.println(employee.getFirstName());
        }
    }

    public static void main(String[] args){
        test();
    }
}
