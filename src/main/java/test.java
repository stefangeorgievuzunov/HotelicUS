import hotelicus.entities.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class test {
    public static void test() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Users> employeeList = session.createQuery("FROM " + Users.class.getSimpleName()).list();


        for(Users employee : employeeList) {
            System.out.println(employee.getFirstName());
        }
    }

    public static void main(String[] args){
        test();
    }
}
