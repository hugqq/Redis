package com.ocrud.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocrud.entity.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.Restaurants;
import com.ocrud.mapper.RestaurantsMapper;
import com.ocrud.service.RestaurantsService;

import java.util.LinkedHashMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-15
 */

@Transactional
@Service
@Slf4j
public class RestaurantsServiceImpl extends ServiceImpl<RestaurantsMapper, Restaurants> implements RestaurantsService {

    private final RedisTemplate redisTemplate;

    public RestaurantsServiceImpl( RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Dict getByIdCache(String id) {
        Dict r = Dict.create();
        String key = Constant.REDIS_POINTS_KEY + id;
        LinkedHashMap restaurantMap = (LinkedHashMap) redisTemplate.opsForHash().entries(key);
        // 如果缓存不存在，查询数据库
        Restaurants restaurant = null;
        if (restaurantMap == null || restaurantMap.isEmpty()) {
            log.info("缓存失效了，查询数据库：{}", id);
            // 查询数据库
            restaurant = getById(id);
            if (restaurant != null) {
                // 更新缓存
                redisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(restaurant));
            }
        } else {
            restaurant = BeanUtil.fillBeanWithMap(restaurantMap,
                    new Restaurants(), false);
        }
        r.put("data", restaurant);
        return r;
    }
}

