package com.javarush.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.config.DBSessionFactory;
import com.javarush.dao.CityDAO;
import com.javarush.entity.City;
import com.javarush.entity.CountryLanguage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RedisRepositoryTest {
    @Test
    public void timeComparisonTest(){
        List<Integer> ids = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        DBSessionFactory.getSessionFactory().close();

        assertTrue(stopRedis < stopMysql);

    }
    private void testRedisData(List<Integer> ids) {
        ObjectMapper mapper = new ObjectMapper();
        try (StatefulRedisConnection<String, String> connection = new RedisRepository().prepareRedisClient().connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void testMysqlData(List<Integer> ids) {
        CityDAO cityDAO = new CityDAO();
        try (Session session = DBSessionFactory.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {

                City city = cityDAO.getEntityById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

}