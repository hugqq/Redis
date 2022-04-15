package com.ocrud;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ocrud.entity.Constant;
import com.ocrud.entity.Restaurants;
import com.ocrud.mapper.RestaurantsMapper;
import com.ocrud.service.RestaurantsService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RestaurantsService restaurantsService;

    @Test
    public void testAdd(){
        List<Restaurants> lists = Lists.newArrayList();
        for (int i = 0; i < 1000; i++) {
            Restaurants restaurants = new Restaurants();
            Faker faker = new Faker(Locale.CHINA);
            String name = faker.name().fullName();
            restaurants.setName(name);
            restaurants.setCnname(name);
            restaurants.setX(Double.valueOf(faker.address().longitude()));
            restaurants.setY(Double.valueOf(faker.address().latitude()));
            String address = faker.address().streetAddress();
            restaurants.setLocation(address);
            restaurants.setCnlocation(address);
            restaurants.setArea(faker.address().cityName());
            restaurants.setTelephone(faker.phoneNumber().cellPhone());
            restaurants.setEmail("11111@qq.com");
            restaurants.setWebsite("");
            restaurants.setCuisine("");
            restaurants.setAveragePrice("");
            restaurants.setIntroduction("");
            restaurants.setThumbnail("");
            restaurants.setLikeVotes(0);
            restaurants.setDislikeVotes(0);
            restaurants.setCityId(0);
            restaurants.setCreateDate(new Date());
            restaurants.setUpdateDate(new Date());
            lists.add(restaurants);
        }
        restaurantsService.saveBatch(lists);

    }



    // 逐行插入
    @Test
    public void testSyncForHash() {
        List<Restaurants> restaurants = restaurantsService.list();
        long start = System.currentTimeMillis();
        restaurants.forEach(restaurant -> {
            Map<String, Object> restaurantMap = BeanUtil.beanToMap(restaurant);
            String key = Constant.REDIS_POINTS_KEY + restaurant.getId();
            redisTemplate.opsForHash().putAll(key, restaurantMap);
        });
        long end = System.currentTimeMillis();
        log.info("执行时间：{}", end - start);
    }


    // Pipeline 管道插入
    @Test
    public void testSyncForHashPipeline() {
        List<Restaurants> restaurants = restaurantsService.list();
        long start = System.currentTimeMillis();
        List<Long> list = redisTemplate.executePipelined((RedisCallback<Long>) connection -> {
            for (Restaurants restaurant : restaurants) {
                try {
                    String key = Constant.REDIS_POINTS_KEY + restaurant.getId();
                    Map<String, Object> restaurantMap = BeanUtil.beanToMap(restaurant);
                    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
                    Map<byte[], byte[]> restaurantStringMap = Maps.newHashMap();
                    restaurantMap.forEach((k, v) -> {
                        restaurantStringMap.put(stringRedisSerializer.serialize(k), jackson2JsonRedisSerializer.serialize(v));
                    });
                    connection.hMSet(stringRedisSerializer.serialize(key), restaurantStringMap);
                } catch (Exception e) {
                    log.info(restaurant.toString());
                    continue;
                }
            }
            return null;
        });
        long end = System.currentTimeMillis();
        log.info("执行时间：{}", end - start);
    }

}
