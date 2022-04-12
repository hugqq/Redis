package com.ocrud.service.impl;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocrud.entity.BaseEntity;
import com.ocrud.entity.Constant;
import com.ocrud.entity.TUser;
import com.ocrud.service.TFollowService;
import com.ocrud.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TFeeds;
import com.ocrud.mapper.TFeedsMapper;
import com.ocrud.service.TFeedsService;

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
public class TFeedsServiceImpl extends ServiceImpl<TFeedsMapper, TFeeds> implements TFeedsService {

    @Autowired
    private TFollowService tFollowService;
    @Autowired
    private TUserService tUserService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 变更 Feed
     *
     * @param followingUserId 关注的好友的 ID
     * @param userId          登录用户 token
     * @param type            1 关注 0 取关
     */
    @Override
    public void addFollowingFeeds(Integer userId, Integer followingUserId, int type) {
        // 请选择关注的好友
        Assert.isFalse(followingUserId == null || followingUserId < 1, "请选择关注的好友");
        // 获取关注/取关所有 Feed
        List<TFeeds> followingFeeds = list(new LambdaQueryWrapper<TFeeds>().eq(TFeeds::getFkUserId, followingUserId));
        if (followingFeeds == null || followingFeeds.isEmpty()) {
            return;
        }
        // 我关注的好友的 FeedsKey
        String key = Constant.REDIS_FOLLOWING_FEEDS_KEY + userId;
        // 取关
        if (type == 0) {
            List<Integer> feedIds = followingFeeds.stream().map(BaseEntity::getId).collect(Collectors.toList());
            redisTemplate.opsForZSet().remove(key, feedIds.toArray(new Integer[]{}));
            // 关注
        } else {
            Set<ZSetOperations.TypedTuple> typedTuples = followingFeeds.stream()
                        .map(feed -> new DefaultTypedTuple<>(feed.getId(), (double) feed.getCreateDate().getTime())).collect(Collectors.toSet());
            redisTemplate.opsForZSet().add(key, typedTuples);
        }
    }
    @Override
    public void delete(Integer id, Integer userId) {
        // 请选择要删除的 Feed
        Assert.isFalse(id == null || id < 1, "请选择要删除的Feed");
        // 获取登录用户
        // 获取 Feed 内容
        TFeeds feeds = getById(id);
        // 判断 Feed 是否已被删除且只能删除自己的 Feed
        Assert.isFalse(feeds == null, "该Feed已被删除");
        Assert.isFalse(!feeds.getFkUserId().equals(userId), "只能删除自己的Feed");
        // 删除
        if (!removeById(id)) {
            return;
        }
        // 将内容从粉丝的集合中删除  -- 异步消息队列优化
        Set<Integer> followersIds = tFollowService.findFollowers(userId);
        followersIds.forEach(follower -> {
            String key = Constant.REDIS_FOLLOWING_FEEDS_KEY.concat(follower + "");
            redisTemplate.opsForZSet().remove(key, feeds.getId());
        });
    }

    /**
     * 根据时间由近到远，每次查询 10 条 Feed
     *
     * @param page 页码
     */
    @Override
    public Page<TFeeds> selectForPage(Integer page, Integer pageSize, Integer userId) {
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
        Map<Integer, TUser> userMap = tUserService.list().stream().collect(Collectors.toMap(TUser::getId, Function.identity()));
        // 查询结果
        Page<TFeeds> result = page(new Page<>(page, pageSize), new LambdaQueryWrapper<TFeeds>().in(TFeeds::getId, feedIds));
        // 添加用户 ID 至集合，顺带将 Feeds 转为 VO 对象
        result.getRecords().stream().map(feed -> feed.setUser(userMap.get(feed.getFkUserId())));
        return result;
    }

}

