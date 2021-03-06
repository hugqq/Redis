package com.ocrud.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ocrud.entity.Constant;
import com.ocrud.entity.Vouchers;
import com.ocrud.service.SeckillVouchersService;
import com.ocrud.service.VouchersService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 抢购优惠券
 * @author glow
 */
@RestController
public class SnapUpController {

    private final SeckillVouchersService seckillVouchersService;
    private final VouchersService vouchersService;
    private final RedisTemplate redisTemplate;

    public SnapUpController(SeckillVouchersService seckillVouchersService, VouchersService vouchersService, RedisTemplate redisTemplate) {
        this.seckillVouchersService = seckillVouchersService;
        this.vouchersService = vouchersService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 模拟 添加或更改时候 添加优惠券的库存缓存
     * seckillVouchersId 活动id
     * voucherId 优惠券id
     */
    @RequestMapping("/addVouchersCache")
    public void addVouchersCache(Integer seckillVouchersId, Integer voucherId) {
        Vouchers voucherss = vouchersService.getById(voucherId);
        String redisKey = "seckillVouchersId:" + seckillVouchersId + ":" + Constant.REDIS_VOUCHER_KEY + voucherId;
        Map<String, Object> seckillVoucherMaps = redisTemplate.opsForHash().entries(redisKey);
        if (CollUtil.isEmpty(seckillVoucherMaps)) {
            seckillVoucherMaps = BeanUtil.beanToMap(voucherss);
            redisTemplate.opsForHash().putAll(redisKey, seckillVoucherMaps);
        } else {
            throw new IllegalArgumentException("该券已经拥有了抢购活动");
        }
    }

    /**
     * 抢购优惠券
     * seckillVouchersId 活动id
     * userId 登录用户
     */
    @RequestMapping("/doSeckill")
    public Boolean doSeckill(Integer seckillVouchersId, Integer userId) {
        return seckillVouchersService.doSeckill(seckillVouchersId, userId);
    }


}
