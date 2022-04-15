package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.SeckillVouchers;


public interface SeckillVouchersService extends IService<SeckillVouchers> {

    Boolean doSeckill(Integer seckillVouchersId, Integer userId);
}

