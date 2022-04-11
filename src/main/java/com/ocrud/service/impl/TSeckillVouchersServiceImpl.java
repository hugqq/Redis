package com.ocrud.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocrud.entity.Constant;
import com.ocrud.entity.TOrders;
import com.ocrud.entity.TVouchers;
import com.ocrud.service.TOrdersService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TSeckillVouchers;
import com.ocrud.mapper.TSeckillVouchersMapper;
import com.ocrud.service.TSeckillVouchersService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-11
 */


@Service
@Slf4j
public class TSeckillVouchersServiceImpl extends ServiceImpl<TSeckillVouchersMapper, TSeckillVouchers> implements TSeckillVouchersService {

    @Autowired
    private TOrdersService tOrdersService;
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private DefaultRedisScript redisScript;


    @SneakyThrows
    @Override
    @Transactional(rollbackFor = { SQLException.class })
    public Boolean doSeckill(Integer seckillVouchersId, Integer userId) {
        // 基本参数校验
        Assert.isFalse(seckillVouchersId == null || seckillVouchersId < 0, "请选择需要抢购的代金券");
        // 判断此代金券是否加入抢购
        TSeckillVouchers seckillVouchers = getById(seckillVouchersId);
        Assert.isFalse(seckillVouchers == null, "该代金券并未有抢购活动");
        // 判断是否有效
        Assert.isFalse(seckillVouchers.getIsValid() == 1, "该活动已结束");
        // 判断是否开始、结束
        LocalDateTime now = LocalDateTime.now();
        Assert.isFalse(now.isBefore(seckillVouchers.getStartTime()), "该抢购还未开始");
        Assert.isFalse(now.isAfter(seckillVouchers.getEndTime()), "该抢购已结束");
        Integer voucherId = seckillVouchers.getFkVoucherId();
        String redisKey = "seckillVouchersId:" + seckillVouchersId + ":" + Constant.REDIS_VOUCHER_KEY + voucherId;
        Map<String, Object> seckillVoucherMaps = redisTemplate.opsForHash().entries(redisKey);
        TVouchers tVouchers = BeanUtil.mapToBean(seckillVoucherMaps, TVouchers.class, true, null);
        // 获取登录用户信息 如果userId不为空则模拟单一用户重复下单
        if (userId == null) {
            userId = Integer.valueOf(RandomUtil.randomNumbers(9));
        }
        // 判断登录用户是否已抢到(一个用户针对这次活动只能买一次)
        TOrders order = tOrdersService.findDinerOrder(userId, seckillVouchers.getFkVoucherId());
        Assert.isFalse(order != null, "该用户已抢到该代金券，无需再抢");
        String lockName = "seckillVouchersId:" + seckillVouchersId + Constant.REDIS_VOUCHER_KEY + voucherId + ":" + userId;
        // 加锁并设置失效时间
        long lockTime = Duration.between(tVouchers.getExpireTime(), LocalDateTime.now()).getSeconds();
        RLock lock = redisson.getLock(lockName);
        try {
            boolean lockFlag = lock.tryLock(lockTime, TimeUnit.SECONDS);
            // 拿到锁
            if (lockFlag) {
                // 下单
                TOrders orders = new TOrders();
                orders.setFkUserId(userId);
                orders.setFkSeckillId(seckillVouchers.getId());
                orders.setFkVoucherId(voucherId);
                String orderNo = IdUtil.getSnowflake(1, 1).nextIdStr();
                orders.setOrderNo(orderNo);
                orders.setOrderType(1);
                orders.setStatus(0);
                boolean save = tOrdersService.save(orders);
                // 扣库存
                List<String> keys = new ArrayList<>();
                keys.add(redisKey);
                keys.add("stockLeft");
                Long stockLeft = (Long) redisTemplate.execute(redisScript, keys);
                Assert.isFalse(stockLeft == null || stockLeft < 1, "该代金券已经卖完了");
                // 异步更新
                rabbitTemplate.convertAndSend(Constant.RABBIT_EXCHANGE_NAME, Constant.RABBIT_KEY_NAME, voucherId + "");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            lock.unlock();
            throw e;
        }
        return true;
    }
}

