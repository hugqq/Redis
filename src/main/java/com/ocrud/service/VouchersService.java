package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.Vouchers;


public interface VouchersService extends IService<Vouchers> {

    int stockDecrease(Integer fkVoucherId);
}

