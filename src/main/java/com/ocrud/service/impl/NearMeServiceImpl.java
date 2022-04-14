package com.ocrud.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ocrud.entity.Constant;
import com.ocrud.entity.NearMeUserVO;
import com.ocrud.service.NearMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NearMeServiceImpl implements NearMeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param userId 当前登录用户
     * @param lon    经度
     * @param lat    维度
     */
    @Override
    public void addUserLocation(Integer userId, Float lon, Float lat) {
        // 参数校验
        Assert.isFalse(lon == null, "获取精度失败");
        Assert.isFalse(lat == null, "获取维度失败");
        // 获取 key user:location
        String key = Constant.REDIS_LOCATION_KEY;
        RedisGeoCommands.GeoLocation geoLocation = new RedisGeoCommands.GeoLocation(userId, new Point(lon, lat));
        redisTemplate.opsForGeo().add(key, geoLocation);
    }


    /**
     * 获取附近的人
     *
     * @param userId 登录
     * @param radius 半径(m)，默认1000m
     * @param lon    经度
     * @param lat    纬度
     * @return
     */
    @Override
    public List<NearMeUserVO> findNearMe(Integer userId, Integer radius, Float lon, Float lat) {
        // 查询半径，默认 1000m
        if (radius == null) {
            radius = 1000;
        }
        String key = Constant.REDIS_LOCATION_KEY;
        // 获取用户经纬度
        Point point = null;
        if (lon == null || lat == null) {
            // 如果经纬度没传，那么从 Redis 中获取，但客户端传入会比较精确
            List<Point> points = redisTemplate.opsForGeo().position(key, userId);
            Assert.isFalse(points == null || points.isEmpty(), "获取经纬度失败！");
            point = points.get(0);
        } else {
            point = new Point(lon, lat);
        }

        // 初始化距离对象，单位 m
        Distance distance = new Distance(radius, RedisGeoCommands.DistanceUnit.METERS);
        // 初始化 Geo 命令参数对象
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        // 附近的人限制 20，包含距离，按由近及远排序
        args.limit(20).includeDistance().sortAscending();
        // 以用户经纬度为圆心，范围 1000m
        Circle circle = new Circle(point, distance);
        // 获取附近的人 GeoLocation 信息
        GeoResults<RedisGeoCommands.GeoLocation<Integer>> results = redisTemplate.opsForGeo().radius(key, circle, args);
        List<NearMeUserVO> list = Lists.newArrayList();
        // 循环处理距离信息
        results.forEach(result -> {
            // 获取 locationName 也就是userId ID
            RedisGeoCommands.GeoLocation<Integer> location = result.getContent();
            NearMeUserVO nearMeDinerVO = new NearMeUserVO();
            nearMeDinerVO.setUserId(location.getName());
            // 格式化距离
            Double dist = result.getDistance().getValue();
            // 四舍五入精确到小数点 1 位，为了方便客户端显示
            // 这里后期需要扩展处理，根据距离显示 m km
            String distanceStr = NumberUtil.round(dist, 1).toString() + "m";
            nearMeDinerVO.setDistance(distanceStr);
            list.add(nearMeDinerVO);
        });
        return list;
    }
}
