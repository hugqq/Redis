package com.ocrud.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.Orders;
import com.ocrud.mapper.OrdersMapper;
import com.ocrud.service.OrdersService;

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
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Override
    public Orders findDinerOrder(Integer userId, Integer id) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Orders>().eq(Orders::getFkUserId, userId).eq(Orders::getFkVoucherId, id).last("limit 1"));
    }
}

