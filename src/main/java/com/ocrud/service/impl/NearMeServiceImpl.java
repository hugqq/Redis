package com.ocrud.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.ocrud.entity.Constant;
import com.ocrud.entity.NearMeUserVO;
import com.ocrud.service.NearMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 获取登录用户id
     * 获取查询半径，以米为单位，默认1000m
     * 获取用户的经纬度，如果客户端没上传经纬度，那么从Redis中读取经纬度
     * 格式化查询的半径，使用RedisTemplate的Distance对象
     * 查询限制条件：限制20，返回包含距离，按由近及远排序
     * 格式化结果，将其封装到Map中，Key为dinerId，Value构建返回的VO，同时格式化distance属性，方便客户端展示
     * 查询附近的人的信息，并添加到对应的VO中
     * 返回结果
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
    /**
     * 计算两点之间的距离
     * GEODIST key l1 l2 [m|km|ft|mi]
     */
    @Override
    public String distance(Integer userId, Integer toUserId) {
        Assert.isFalse(userId == null, "获取用户失败！");
        Assert.isFalse(toUserId == null, "获取对象失败！");
        String key = Constant.REDIS_LOCATION_KEY;
        Distance distance = redisTemplate.opsForGeo().distance(key, userId, toUserId, Metrics.MILES);
        return  NumberUtil.round(distance.getValue(), 1).toString() + "m";
    }

}
