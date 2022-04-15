package com.ocrud.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocrud.entity.BaseEntity;
import com.ocrud.entity.Constant;
import com.ocrud.entity.User;
import com.ocrud.service.FollowService;
import com.ocrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.Feeds;
import com.ocrud.mapper.FeedsMapper;
import com.ocrud.service.FeedsService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-12
 */

@Transactional
@Service
public class FeedsServiceImpl extends ServiceImpl<FeedsMapper, Feeds> implements FeedsService {

    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 变更 Feed
     *
     * @param followingUserId 关注的好友的 ID
     * @param userId          登录用户
     * @param type            1 关注 0 取关
     */
    @Override
    public void addFollowingFeeds(Integer userId, Integer followingUserId, int type) {
        // 请选择关注的好友
        Assert.isFalse(followingUserId == null || followingUserId < 1, "请选择关注的好友");
        // 获取关注/取关所有 Feed
        List<Feeds> followingFeeds = list(new LambdaQueryWrapper<Feeds>().eq(Feeds::getFkUserId, followingUserId));
        if (followingFeeds == null || followingFeeds.isEmpty()) {
            return;
        }
        // 我关注的 FeedsKey
        String key = Constant.REDIS_FOLLOWING_FEEDS_KEY + userId;
        // 取关
        if (type == 0) {
            List<Integer> feedIds = followingFeeds.stream().map(BaseEntity::getId).collect(Collectors.toList());
            redisTemplate.opsForZSet().remove(key, feedIds.toArray(new Integer[]{}));
            // 关注
        } else {
            Set<ZSetOperations.TypedTuple> typedTuples = followingFeeds.stream().map(feed -> {
                if (feed.getCreateDate() == null) {
                    feed.setCreateDate(new Date());
                }
                return new DefaultTypedTuple<>(feed.getId(), (double) feed.getCreateDate().getTime());
            }).collect(Collectors.toSet());
            redisTemplate.opsForZSet().add(key, typedTuples);
        }
    }

    @Override
    public Dict delete(Integer id, Integer userId) {
        // 请选择要删除的 Feed
        Assert.isFalse(id == null || id < 1, "请选择要删除的Feed");
        // 获取登录用户
        // 获取 Feed 内容
        Feeds feeds = getById(id);
        // 判断 Feed 是否已被删除且只能删除自己的 Feed
        Assert.isFalse(feeds == null, "该Feed已被删除");
        Assert.isFalse(feeds.getFkUserId().equals(userId), "只能删除自己的Feed");
        // 删除
        if (removeById(id)) {
            // 将内容从粉丝的集合中删除  -- 异步消息队列优化
            Set<Integer> followersIds = followService.findFollowers(userId);
            followersIds.forEach(follower -> {
                String key = Constant.REDIS_FOLLOWING_FEEDS_KEY.concat(follower + "");
                redisTemplate.opsForZSet().remove(key, feeds.getId());
            });
            return Dict.create().set("code", "200").set("msg", "删除成功");
        }else {
            return Dict.create().set("code", "500").set("msg", "删除失败");
        }
    }

    /**
     * 根据时间由近到远，每次查询 10 条 Feed
     *
     * @param page 页码
     */
    @Override
    public Page<Feeds> selectForPage(Integer page, Integer pageSize, Integer userId) {
        if (page == null) {
            page = 1;

        }
        if (pageSize == null) {
            pageSize = 10;
        }
        // 我关注的好友的 FeedsKey
        String redisFeedKey = Constant.REDIS_FOLLOWING_FEEDS_KEY + userId;
        // SortedSet 的 ZREVRANGE 命令是闭区间(左闭右闭)
        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;
        // 获取 10 条 Feed ID
        Set<Integer> feedIds = redisTemplate.opsForZSet().reverseRange(redisFeedKey, start, end);
        if (feedIds == null || feedIds.isEmpty()) {
            return new Page<>();
        }
        Map<Integer, User> userMap = userService.list().stream().collect(Collectors.toMap(User::getId, Function.identity()));
        // 查询结果
        Page<Feeds> result = page(new Page<>(page, pageSize), new LambdaQueryWrapper<Feeds>().in(Feeds::getId, feedIds));
        // 翻译字典
        result.getRecords().forEach(feed -> feed.setUser(userMap.get(feed.getFkUserId())));
        return result;
    }

}

