package com.ocrud.service;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TFollow;


public interface TFollowService extends IService<TFollow> {
    Dict follow(Integer userId,Integer followUserId, Integer isFollowed);

    Dict findCommonsFriends(Integer userId, Integer followUserId);
}

