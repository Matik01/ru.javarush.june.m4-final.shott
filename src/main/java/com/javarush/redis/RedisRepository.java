package com.javarush.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.entity.City;
import com.javarush.util.DataTransformer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;

import java.util.List;

public class RedisRepository {
    public void write(List<City> cities){
        DataTransformer dataTransformer = new DataTransformer();
        List<CityCountry> cityCountries = dataTransformer.transformData(cities);
        this.pushToRedis(cityCountries);
    }
    private void pushToRedis(List<CityCountry> data) {
        ObjectMapper mapper = new ObjectMapper();
        RedisClient redisClient = prepareRedisClient();

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public RedisClient prepareRedisClient() {
        RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            System.out.println("\nConnected to Redis\n");
        }
        return redisClient;
    }
}
