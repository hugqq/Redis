package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TVouchers;


public interface TVouchersService extends IService<TVouchers> {

    int stockDecrease(Integer fkVoucherId);
}

