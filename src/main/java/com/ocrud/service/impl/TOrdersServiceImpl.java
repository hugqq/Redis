package com.ocrud.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TOrders;
import com.ocrud.mapper.TOrdersMapper;
import com.ocrud.service.TOrdersService;

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
public class TOrdersServiceImpl extends ServiceImpl<TOrdersMapper, TOrders> implements TOrdersService {

    @Override
    public TOrders findDinerOrder(Integer userId, Integer id) {
        return baseMapper.selectOne(new LambdaQueryWrapper<TOrders>().eq(TOrders::getFkUserId, userId).eq(TOrders::getFkVoucherId, id).last("limit 1"));
    }
}

