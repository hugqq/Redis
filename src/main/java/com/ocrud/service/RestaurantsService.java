package com.ocrud.service;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.Restaurants;


public interface RestaurantsService extends IService<Restaurants> {

    Dict getByIdCache(String id);
}

