package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.Orders;


public interface OrdersService extends IService<Orders> {

    Orders findDinerOrder(Integer userId, Integer id);
}

