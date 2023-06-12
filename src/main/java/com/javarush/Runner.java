package com.javarush;

import com.javarush.config.DBSessionFactory;
import com.javarush.dao.CountryDAO;
import com.javarush.entity.City;
import com.javarush.liquibase.Validator;
import com.javarush.redis.RedisRepository;

import java.util.List;

public class Runner {
    public static void main(String[] args) {
        Validator validator = new Validator();
        try {
            validator.liquibaseConnector();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CountryDAO countryDAO = new CountryDAO();
        List<City> cities = countryDAO.fetchData();
        RedisRepository redisRepository = new RedisRepository();
        redisRepository.write(cities);

        DBSessionFactory.getSessionFactory().getCurrentSession().close();
    }
}