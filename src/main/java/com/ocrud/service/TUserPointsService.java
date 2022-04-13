package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TUserPoints;


public interface TUserPointsService extends IService<TUserPoints> {

    boolean add(TUserPoints tUserPoints);
}

