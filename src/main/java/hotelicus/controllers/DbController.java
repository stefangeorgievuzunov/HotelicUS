package hotelicus.controllers;

import java.io.IOException;
import java.lang.Class;

import hotelicus.App;
import hotelicus.HibernateUtil;
import hotelicus.window.Error;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import java.util.List;

@SuppressWarnings("all")
public class DbController<T>{
    private  Class<T> type;
    private Session session;
    public DbController(Class<T> type){
        this.session= App.getSession();
        this.type=type;
    }
    private void DESC(String property,Criteria crit){
        crit.addOrder(Order.desc(property));
    }

    public  <T> List<T> findAll(){

        this.session.beginTransaction();
        List<T> all = this.session.createQuery("FROM " + this.type.getSimpleName()).list();

        this.session.getTransaction().commit();
        return all;
    }

    public void insert(T object){
        this.session.save(object);
    }

    public T load(Integer id){
        T object=session.load(this.type, id);
        return object;
    }
    public void update(T object){
        this.session.update(object);
    }

    public <T> List<T>  selectEqualTo(String criteria,Object value,boolean DESC){
        Criteria crit=this.session.createCriteria(this.type);
        crit.add(Restrictions.eq(criteria, value));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return(List<T>)crit.list();
    }

    public <T> boolean usernamePasswordValidator(String username,String password)throws IOException{
        Criteria crit=this.session.createCriteria(this.type);
        crit.add(Restrictions.eq("username", username));
        crit.add(Restrictions.eq("password", password));
        List<T> users=crit.list();

        if(users.size()>1){
            new Error("Failed to login","FATAL ERROR: More than one record existing !");
            return false;
        }
        else if(users.size()<1){
            new Error("Failed to login","Invalid username or password!");
            return false;
        }else{
            return true;
        }
    }
    public <T> List<T>  selectGreaterThan(String criteria,Object value,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.gt(criteria, value));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectGreaterOrEqual(String criteria,Object value,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ge(criteria, value));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }

    public <T> List<T>  selectLowerThan(String criteria,Object value,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.lt(criteria, value));

        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }

    public <T> List<T>  selectLowerOrEqual(String criteria,Object value,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.le(criteria, value));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectNotEqualTo(String criteria,Object value,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ne(criteria, value));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }

    //LIKE FUNCTONS MAY BE WITH JUST ".like" IF WE DONT CARE ABOUT CASE-SENSITIVE
    public <T> List<T>  selectLike_ANYWHERE(String criteria,Object like,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.ANYWHERE));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectLike_START(String criteria,Object like,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.START));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectLike_END(String criteria,Object like,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.END));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectLike_EXACT(String criteria,Object like,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.EXACT));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }


    public <T> List<T>  selectNull(String criteria,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.isNull(criteria));
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
}
