package com.ocrud.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.ocrud.entity.Constant;
import com.ocrud.entity.TFollow;
import com.ocrud.entity.TUser;
import com.ocrud.entity.TVouchers;
import com.ocrud.service.TFollowService;
import com.ocrud.service.TSeckillVouchersService;
import com.ocrud.service.TUserService;
import com.ocrud.service.TVouchersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author glow
 */
@RestController
public class SnapUpController {

    @Autowired
    private TSeckillVouchersService seckillVouchersService;
    @Autowired
    private TVouchersService tVouchersService;
    @Autowired
    private TFollowService tFollowService;
    @Autowired
    private TUserService tUserService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 模拟 添加或更改时候 添加优惠券的库存缓存
     */
    @RequestMapping("/addVouchersCache")
    public void addVouchersCache(Integer seckillVouchersId, Integer voucherId) {
        TVouchers tVouchers = tVouchersService.getById(voucherId);
        String redisKey = "seckillVouchersId:" + seckillVouchersId + ":" + Constant.REDIS_VOUCHER_KEY + voucherId;
        Map<String, Object> seckillVoucherMaps = redisTemplate.opsForHash().entries(redisKey);
        if (CollUtil.isEmpty(seckillVoucherMaps)) {
            seckillVoucherMaps = BeanUtil.beanToMap(tVouchers);
            redisTemplate.opsForHash().putAll(redisKey, seckillVoucherMaps);
        } else {
            throw new IllegalArgumentException("该券已经拥有了抢购活动");
        }
    }

    /**
     * 抢购优惠券
     *
     * @param seckillVouchersId
     * @param userId
     */
    @RequestMapping("/doSeckill")
    public Boolean doSeckill(Integer seckillVouchersId, Integer userId) {
        return seckillVouchersService.doSeckill(seckillVouchersId, userId);
    }

    /**
     * 查询所有
     */
    @RequestMapping("/selectFollow")
    public List<TFollow> selectFollow() {
        Map<Integer, String> userMap = tUserService.list().stream().collect(Collectors.toMap(TUser::getId, TUser::getName));
        List<TFollow> list = tFollowService.list();
        list.forEach(
                e -> {
                    e.setUserName(userMap.get(e.getUserId()));
                    e.setFollowUserName(userMap.get(e.getFollowUserId()));
                }
        );
        return list;
    }

    /**
     * 关注/取关
     * @param userId
     * @param followUserId
     * @param isFollowed 1关注 0取关
     */
    @RequestMapping("/follow")
    public Dict follow(Integer userId,Integer followUserId, Integer isFollowed) {
        return tFollowService.follow(userId,followUserId,isFollowed);
    }

    /**
     * 共同好友
     * @param userId
     * @param followUserId
     */
    @RequestMapping("/findCommonsFriends")
    public Dict findCommonsFriends(Integer userId,Integer followUserId) {
        return tFollowService.findCommonsFriends(userId,followUserId);
    }


}
