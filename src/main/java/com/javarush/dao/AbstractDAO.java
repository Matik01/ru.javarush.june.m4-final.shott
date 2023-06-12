package com.javarush.dao;

import com.javarush.config.DBSessionFactory;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class AbstractDAO<E, K> {
    protected SessionFactory sessionFactory;
    public AbstractDAO(){
        this.sessionFactory = DBSessionFactory.getSessionFactory();
    }
    public abstract List<E> getAll();
    public abstract E getEntityById(K id);
    public abstract List<E> getItems(int offset, int limit);
    public abstract K getTotalCount();
}
