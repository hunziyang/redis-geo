package com.yang.geo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class GeoApplication implements ApplicationRunner {

    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(GeoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Point> pointMap = new HashMap<>();
        pointMap.put("tianjing",new Point(117.12,39.08));
        pointMap.put("shijiazhuang",new Point(114.29,38.02));
        Distance distance = redisTemplate.opsForGeo().distance("yang", "tianjing", "shijiazhuang", RedisGeoCommands.DistanceUnit.KILOMETERS);
        Metric metric = distance.getMetric();
        System.out.println(distance.getValue());
        System.out.println(metric.getAbbreviation());
        GeoResults radius = redisTemplate.opsForGeo().radius("yang", "tianjing", new Distance(300, RedisGeoCommands.DistanceUnit.KILOMETERS), RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs());
//        GeoResult<RedisGeoCommands.GeoLocation<String>> a = (GeoResult<RedisGeoCommands.GeoLocation<String>>)radius.getContent().get(0);
//        String name = a.getContent().getName();
//        System.out.println(name);
        List content = radius.getContent();
        content.forEach(c->{
            GeoResult<RedisGeoCommands.GeoLocation<String>> a = (GeoResult<RedisGeoCommands.GeoLocation<String>>)c;
            String name = a.getContent().getName();
            log.warn(name);
        });
        redisTemplate.expire("yang",160, TimeUnit.MINUTES);
    }
}
