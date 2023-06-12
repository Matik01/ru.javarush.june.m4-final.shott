package com.javarush.dao;

import com.javarush.entity.City;
import com.javarush.entity.Country;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CountryDAO extends AbstractDAO<Country, Integer> {

    @Override
    public List<Country> getAll() {
        Query<Country> selectFromCountry = sessionFactory.getCurrentSession().createQuery("select c from Country c join fetch c.languages", Country.class);
        return selectFromCountry.list();
    }

    @Override
    public Country getEntityById(Integer id) {
        Country country = sessionFactory.getCurrentSession().find(Country.class, id);
        return country;
    }

    @Override
    public List<Country> getItems(int offset, int limit) {
        Query<Country> selectFromCountry = sessionFactory.getCurrentSession().createQuery("select c from Country c", Country.class);
        selectFromCountry.setFirstResult(offset);
        selectFromCountry.setMaxResults(limit);
        return selectFromCountry.list();

    }

    @Override
    public Integer getTotalCount() {
        Query<Integer> query = sessionFactory.getCurrentSession().createQuery("select count(*) from Country", Integer.class);
        return query.uniqueResult();

    }

    public List<City> fetchData() {
        CityDAO cityDAO = new CityDAO();
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            List<City> cities = new ArrayList<>();
            currentSession.beginTransaction();
            List<Country> countries = this.getAll();

            Integer totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                cities.addAll(cityDAO.getItems(i, step));
            }

            currentSession.getTransaction().commit();
            return cities;
        }
    }
}
