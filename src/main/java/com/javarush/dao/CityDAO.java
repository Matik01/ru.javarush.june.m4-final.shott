package com.javarush.dao;

import com.javarush.entity.City;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CityDAO extends AbstractDAO<City, Integer> {
    @Override
    public List<City> getAll() {
        Query<City> selectFromCity = sessionFactory.getCurrentSession().createQuery("select c from City c", City.class);
        return selectFromCity.list();
    }

    @Override
    public City getEntityById(Integer id) {
        Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from City c join fetch c.country where c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }

    @Override
    public List<City> getItems(int offset, int limit) {
        Query<City> selectFromCity = sessionFactory.getCurrentSession().createQuery("select c from City c", City.class);
        selectFromCity.setFirstResult(offset);
        selectFromCity.setMaxResults(limit);
        List<City> list = selectFromCity.list();
        return list;
    }

    @Override
    public Integer getTotalCount() {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery("select count(*) from City", Long.class);
        return Math.toIntExact(query.uniqueResult());

    }


}
