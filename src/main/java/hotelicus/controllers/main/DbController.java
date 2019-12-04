package hotelicus.controllers.main;


import java.lang.Class;

import hotelicus.App;
import hotelicus.exceptions.*;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

@SuppressWarnings("all")
public class DbController<T> {
    private Class<T> type;
    private Session session;

    public DbController(Class<T> type) {
        this.session = App.getSession();
        if (type != null) {
            this.type = type;
        } else {
            throw new DbControllerNullConstructorException();
        }
    }

    public <T> List<T> findAll() {
        this.session.beginTransaction();
        List<T> all = this.session.createQuery("FROM " + this.type.getSimpleName()).list();
        this.session.getTransaction().commit();
        return all;
    }

    public void insert(T... objects) throws ConstraintViolationException {
        if (objects != null) {
            this.session.beginTransaction();
            for (T object : objects) {
                if (object != null) {
                    this.session.save(object);
                } else {
                    throw new InsertNullObjectException();
                }
            }
            this.session.getTransaction().commit();
        }
    }

    public void delete(T... objects) {
        if (objects != null) {
            this.session.beginTransaction();
            for (T object : objects) {
                if (object != null) {
                    this.session.delete(object);
                } else {
                    throw new DeleteNullObjectException();
                }
            }
            this.session.getTransaction().commit();
        }
    }

    public void update(T... objects) {
        if (objects != null) {
            this.session.beginTransaction();
            for (T object : objects) {
                if (object != null) {
                    this.session.update(object);
                } else {
                    throw new UpdateNullObjectException();
                }
            }
            this.session.getTransaction().commit();
        }
    }

    public void insertOrUpdate(T... objects) throws ConstraintViolationException {
        this.session.beginTransaction();
        for (T object : objects) {
            this.session.saveOrUpdate(object);
        }
        this.session.getTransaction().commit();
    }

    public <T> List<T> select(Criterion... args) {
        this.session.beginTransaction();
        Criteria crit = this.session.createCriteria(this.type);
        for (Criterion criteria : args) {
            try {
                crit.add(criteria);
            } catch (NullPointerException excp) {
                throw new SelectNullObjectException();
            }
        }
        this.session.getTransaction().commit();
        return (List<T>) crit.list();
    }

    public T selectUnique(Criterion... args) throws NonUniqueResultException {
        this.session.beginTransaction();
        Criteria crit = this.session.createCriteria(this.type);
        for (Criterion criteria : args) {
            try{
                crit.add(criteria);
            }catch(NullPointerException excep){
                throw new SelectNullObjectException();
            }
        }
        this.session.getTransaction().commit();
        return (T) crit.uniqueResult();
    }

    public <T> List<T> selectNull(String criteria) {
        this.session.beginTransaction();
        Criteria crit = this.session.createCriteria(this.type);
        crit.add(Restrictions.isNull(criteria));
        this.session.getTransaction().commit();
        return (List<T>) crit.list();
    }

}
