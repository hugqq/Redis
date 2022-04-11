package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TOrders;


public interface TOrdersService extends IService<TOrders> {

    TOrders findDinerOrder(Integer userId, Integer id);
}

