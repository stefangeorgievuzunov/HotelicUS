package hotelicus.controllers.main;


import java.lang.Class;

import hotelicus.App;
import javafx.util.Pair;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Properties;

@SuppressWarnings("all")
public class DbController<T> {
    private Class<T> type;
    private Session session;

    public DbController(Class<T> type) {
        this.session = App.getSession();
        this.type = type;
    }

    public <T> List<T> findAll() {
        this.session.beginTransaction();
        List<T> all = this.session.createQuery("FROM " + this.type.getSimpleName()).list();
        this.session.getTransaction().commit();
        return all;
    }

    public void insert(T object) throws ConstraintViolationException {
        this.session.beginTransaction();
        this.session.save(object);
        this.session.getTransaction().commit();
    }

    public void delete(T ...objects) {
        this.session.beginTransaction();
        for(T object : objects){

            this.session.delete(object);
        }
        this.session.getTransaction().commit();
    }

    public void update(T ...objects) {
        this.session.beginTransaction();
        for(T object : objects){
            this.session.update(object);
        }
        this.session.getTransaction().commit();
    }

    public void insertOrUpdate(T ...objects) throws ConstraintViolationException {
        this.session.beginTransaction();
        for(T object : objects){
            this.session.saveOrUpdate(object);
        }
        this.session.getTransaction().commit();
    }
    public <T> List<T> select(Criterion ...args){
        this.session.beginTransaction();
        Criteria crit = this.session.createCriteria(this.type);
        for(Criterion criteria : args){
            crit.add(criteria);
        }
        this.session.getTransaction().commit();
        return (List<T>) crit.list();
    }
    public T selectUnique(Criterion ...args)throws NonUniqueResultException,NullPointerException,IllegalArgumentException {
        this.session.beginTransaction();
        Criteria crit = this.session.createCriteria(this.type);
        for(Criterion criteria : args){
            crit.add(criteria);
        }
        this.session.getTransaction().commit();
        return (T)crit.uniqueResult();
    }

    public <T> List<T> selectNull(String criteria) {
        this.session.beginTransaction();
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.isNull(criteria));
        this.session.getTransaction().commit();
        return (List<T>) crit.list();
    }

}
