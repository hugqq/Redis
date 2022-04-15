package com.ocrud.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ocrud.entity.Constant;
import com.ocrud.entity.PointTypesConstant;
import com.ocrud.entity.PointsRankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.UserPoints;
import com.ocrud.mapper.UserPointsMapper;
import com.ocrud.service.UserPointsService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-13
 */

@Transactional
@Service
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints> implements UserPointsService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加积分
     */
    @Override
    public Boolean add(Integer userId, Integer points, Integer types) {
        // 插入数据库
        UserPoints userPoints = new UserPoints();
        userPoints.setFkUserId(userId);
        userPoints.setPoints(points);
        userPoints.setTypes(types);
        boolean result = baseMapper.add(userPoints);
        // 将积分保存到 Redis 的 Sorted Sets 中
        redisTemplate.opsForZSet().incrementScore(
                Constant.REDIS_POINTS_KEY, userId, points);
        return result;
    }

    /**
     * 添加用户积分 给签到用
     */
    @Override
    public Boolean addPoints(int count, Integer userId) {
        // 签到1天送10积分，连续签到2天送20积分，3天送30积分，4天以上均送50积分
        int points = 10;
        if (count == 2) {
            points = 20;
        } else if (count == 3) {
            points = 30;
        } else if (count >= 4) {
            points = 50;
        }
        return add(userId, points, PointTypesConstant.sign.getType());
    }

    /**
     * 查询排行榜，并显示个人排名 -- Mysql
     */
    @Override
    public List<PointsRankVO> findTopN(Integer userId, Integer top) {
        List<PointsRankVO> topN = baseMapper.findTopN(top);
        if (topN == null || topN.isEmpty()) {
            return Lists.newArrayList();
        }
        topN.forEach(e -> e.setIsMe(e.getId().equals(userId)));
        if (!isUserExist(topN, userId)) {
            // 如果不在 ranks 中，获取个人排名追加在最后
            PointsRankVO userRank = baseMapper.findUserRank(userId);
            userRank.setIsMe(true);
            topN.add(userRank);
        }
        return topN;
    }

    /**
     * 查询排行榜，并显示个人排名 -- Redis
     */
    @Override
    public List<PointsRankVO> findTopNFromRedis(Integer userId, Integer top) {

        // 统计积分排行榜
        Set<ZSetOperations.TypedTuple<Integer>> rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(
                Constant.REDIS_POINTS_KEY, 0, top - 1);
        if (rangeWithScores == null || rangeWithScores.isEmpty()) {
            return Lists.newArrayList();
        }
        // 初始化排名
        int rank = 1;
        // 初始化食客 ID 集合
        List<PointsRankVO> topN = Lists.newArrayList();
        // 根据 key： 用户ID  value：积分信息 构建一个 Map
        Map<Integer, PointsRankVO> ranksMap = new LinkedHashMap<>(top);
        // 循环处理排行榜，添加排名信息
        for (ZSetOperations.TypedTuple<Integer> rangeWithScore : rangeWithScores) {
            // 食客ID
            Integer currentUserId = rangeWithScore.getValue();
            // 积分
            int points = rangeWithScore.getScore().intValue();
            // 将食客 ID 添加至食客 ID 集合
            PointsRankVO dinerPointsRankVO = new PointsRankVO();
            dinerPointsRankVO.setId(currentUserId);
            dinerPointsRankVO.setRanks(rank);
            dinerPointsRankVO.setTotal(points);
            dinerPointsRankVO.setIsMe(false);
            topN.add(dinerPointsRankVO);
            ranksMap.put(currentUserId, dinerPointsRankVO);
            // 排名 +1
            rank++;
        }
        // 包含
        if (isUserExist(topN, userId)) {
            PointsRankVO pointsRankVO = ranksMap.get(userId);
            pointsRankVO.setIsMe(true);
            return Lists.newArrayList(ranksMap.values());
        }
        // 如果不在 ranks 中，获取个人排名追加在最后
        else {
            Long myRank = redisTemplate.opsForZSet().reverseRank(
                    Constant.REDIS_POINTS_KEY, userId);
            if (myRank != null) {
                PointsRankVO userRank = new PointsRankVO();
                userRank.setId(userId);
                userRank.setRanks(myRank.intValue() + 1);
                // 获取积分
                Double points = redisTemplate.opsForZSet().score(Constant.REDIS_POINTS_KEY,
                        userId);
                userRank.setTotal(points.intValue());
                userRank.setIsMe(true);
                topN.add(userRank);
            }
        }
        return topN;
    }


    /**
     * 判断用户是否在排行榜中
     */
    public static boolean isUserExist(List<PointsRankVO> pointsRankList, Integer userId) {
        List<Integer> collect = pointsRankList.stream().map(PointsRankVO::getId).collect(Collectors.toList());
        return collect.contains(userId);
    }
}

