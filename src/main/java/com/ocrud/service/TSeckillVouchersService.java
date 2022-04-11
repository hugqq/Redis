package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TSeckillVouchers;


public interface TSeckillVouchersService extends IService<TSeckillVouchers> {

    Boolean doSeckill(Integer seckillVouchersId, Integer userId);
}

