package com.ocrud.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocrud.entity.Constant;
import com.ocrud.entity.TUser;
import com.ocrud.service.TFeedsService;
import com.ocrud.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TFollow;
import com.ocrud.mapper.TFollowMapper;
import com.ocrud.service.TFollowService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-11
 */

@Transactional
@Service
public class TFollowServiceImpl extends ServiceImpl<TFollowMapper, TFollow> implements TFollowService {

    @Autowired
    private TUserService tUserService;
    @Autowired
    private TFeedsService tFeedsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Dict follow(TFollow tFollow) {
        Integer followUserId = tFollow.getFollowUserId();
        Integer userId = tFollow.getUserId();
        Integer isFollowed = tFollow.getIsFollowed();
        Assert.isFalse(followUserId == null || followUserId < 1, "请选择要关注的人");
        // 获取登录用户信息
        TUser tUser = tUserService.getById(userId);
        // 获取当前登录用户与需要关注用户的关注信息
        TFollow follow = getOne(new LambdaQueryWrapper<TFollow>().eq(TFollow::getUserId, userId).eq(TFollow::getFollowUserId, followUserId));
        // 如果没有关注信息，且要进行关注操作
        if (follow == null && isFollowed == 1) {
            // 添加关注信息
            follow = new TFollow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            boolean flag = save(follow);
            // 添加关注列表到 Redis
            if (flag) {
                addToRedisSet(userId, followUserId);
                tFeedsService.addFollowingFeeds(userId,followUserId,1);
                return Dict.create().set("code", "200").set("msg", "关注成功");
            } else {
                return Dict.create().set("code", "500").set("msg", "关注失败");
            }
        }
        Assert.isFalse(follow != null && follow.getIsFollowed() == 0 && isFollowed == 1, "重复关注");
        Assert.isFalse(follow != null && follow.getIsFollowed() == 1 && isFollowed == 0, "重复取消关注");
        // 如果有关注信息，且目前处于取关状态，且要进行关注操作
        if (follow != null && follow.getIsFollowed() == 1 && isFollowed == 1) {
            // 重新关注
            follow.setIsFollowed(0);
            boolean flag = updateById(follow);
            // 添加关注列表
            if (flag) {
                addToRedisSet(userId, followUserId);
                tFeedsService.addFollowingFeeds(userId,followUserId,1);
                return Dict.create().set("code", "200").set("msg", "关注成功");
            } else {
                return Dict.create().set("code", "500").set("msg", "关注失败");
            }
        }
        // 如果有关注信息，且目前处于关注中状态，且要进行取关操作
        if (follow != null && follow.getIsFollowed() == 0 && isFollowed == 0) {
            follow.setIsFollowed(1);
            boolean flag = updateById(follow);
            // 移除 Redis 关注列表
            if (flag) {
                removeFromRedisSet(userId, followUserId);
                tFeedsService.addFollowingFeeds(userId,followUserId,0);
                return Dict.create().set("code", "200").set("msg", "取关成功");
            } else {
                return Dict.create().set("code", "500").set("msg", "取关失败");
            }
        }
        return Dict.create().set("code", "500").set("msg", "请先关注");
    }

    @Override
    public Dict findCommonsFriends(TFollow tFollow) {
        Integer followUserId = tFollow.getFollowUserId();
        Integer userId = tFollow.getUserId();
        // 是否选择了关注对象
        Assert.isFalse(followUserId == null || followUserId < 1, "请选择要查看的人");

        // 登录用户的关注信息
        String userKey = Constant.REDIS_FOLLOWING_KEY + userId;
        // 登录用户关注的对象的关注信息
        String followersKey = Constant.REDIS_FOLLOWING_KEY + followUserId;
        // 计算交集
        Set<Integer> ids = redisTemplate.opsForSet().intersect(userKey, followersKey);
        // 没有
        if (ids == null || ids.isEmpty()) {
            return Dict.create().set("code", "200").set("msg", "查询成功").set("data", new ArrayList<>());
        } else {
            List<TUser> list = tUserService.list(new LambdaQueryWrapper<TUser>().in(TUser::getId, ids));
            return Dict.create().set("code", "200").set("msg", "查询成功").set("data", list);
        }
    }


    @Override
    public Set<Integer> findFollowing(Integer userId ) {
        return redisTemplate.opsForSet().members(Constant.REDIS_FOLLOWING_KEY + userId);
    }


    @Override
    public Set<Integer> findFollowers(Integer userId ) {
        return redisTemplate.opsForSet().members(Constant.REDIS_FOLLOWERS_KEY + userId);
    }

    @Override
    public Dict findFollowing(TFollow tFollow) {
        Integer userId = tFollow.getUserId();
        Set<Integer> ids = redisTemplate.opsForSet().members(Constant.REDIS_FOLLOWING_KEY + userId);
        List<TUser> list = tUserService.list(new LambdaQueryWrapper<TUser>().in(TUser::getId, ids));
        return Dict.create().set("code", "200").set("msg", "查询成功").set("data", list);
    }

    @Override
    public Dict findFollowers(TFollow tFollow) {
        Integer userId = tFollow.getUserId();
        Set<Integer> ids = redisTemplate.opsForSet().members(Constant.REDIS_FOLLOWERS_KEY + userId);
        List<TUser> list = tUserService.list(new LambdaQueryWrapper<TUser>().in(TUser::getId, ids));
        return Dict.create().set("code", "200").set("msg", "查询成功").set("data", list);
    }

    @Override
    public Dict isFollowed(TFollow tFollow) {
        Integer userId = tFollow.getUserId();
        Integer followUserId = tFollow.getFollowUserId();
        Set<Integer> ids = redisTemplate.opsForSet().members(Constant.REDIS_FOLLOWERS_KEY + followUserId);
        return Dict.create().set("code", "200").set("msg", "查询成功").set("data", ids.contains(userId));
    }


    private void addToRedisSet(Integer userId, Integer followUserId) {
        redisTemplate.opsForSet().add(Constant.REDIS_FOLLOWING_KEY + userId, followUserId);
        redisTemplate.opsForSet().add(Constant.REDIS_FOLLOWERS_KEY + followUserId, userId);
    }

    private void removeFromRedisSet(Integer userId, Integer followUserId) {
        redisTemplate.opsForSet().remove(Constant.REDIS_FOLLOWING_KEY + userId, followUserId);
        redisTemplate.opsForSet().remove(Constant.REDIS_FOLLOWERS_KEY + followUserId, userId);
    }

}

