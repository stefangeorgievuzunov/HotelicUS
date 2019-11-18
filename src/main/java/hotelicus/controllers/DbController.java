package hotelicus.controllers;

import java.lang.Class;

import hotelicus.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

@SuppressWarnings("all")
public class DbController<T> {
    private  Class<T> type;
    private Session session;

    public DbController(Class<T> type){
       this.session = HibernateUtil.getSessionFactory().openSession();
       this.type=type;
    }

    private void ASC(String property,Criteria crit){
        crit.addOrder(Order.asc(property));
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

    public <T> List<T>  selectEqualTo(String criteria,Object value,boolean ASC,boolean DESC){
        Criteria crit=this.session.createCriteria(this.type);
        crit.add(Restrictions.eq(criteria, value));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return(List<T>)crit.list();
    }

    public <T> List<T>  selectGreaterThan(String criteria,Object value,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.gt(criteria, value));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectGreaterOrEqual(String criteria,Object value,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ge(criteria, value));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }

    public <T> List<T>  selectLowerThan(String criteria,Object value,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.lt(criteria, value));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }

    public <T> List<T>  selectLowerOrEqual(String criteria,Object value,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.le(criteria, value));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectNotEqualTo(String criteria,Object value,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ne(criteria, value));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }

    //LIKE FUNCTONS MAY BE WITH JUST ".like" IF WE DONT CARE ABOUT CASE-SENSITIVE
    public <T> List<T>  selectLike_ANYWHERE(String criteria,Object like,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.ANYWHERE));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectLike_START(String criteria,Object like,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.START));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectLike_END(String criteria,Object like,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.END));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
    public <T> List<T>  selectLike_EXACT(String criteria,Object like,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.ilike(criteria, like+"%", MatchMode.EXACT));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }


    public <T> List<T>  selectNull(String criteria,boolean ASC,boolean DESC) {
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.isNull(criteria));
        if(ASC==true && DESC==true){
            ASC=false;
            DESC=false;
        }
        if(ASC==true){
            this.ASC(criteria,crit);
        }
        if(DESC==true){
            this.DESC(criteria,crit);
        }
        return (List<T>) crit.list();
    }
}
