package com.ocrud.service;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TFollow;

import java.util.Set;


public interface TFollowService extends IService<TFollow> {
    Dict follow(TFollow tFollow);

    Dict findCommonsFriends(TFollow tFollow);

    Set<Integer> findFollowing(Integer userId);

    Set<Integer> findFollowers(Integer userId);

    Dict findFollowing(TFollow tFollow);

    Dict findFollowers(TFollow tFollow);

    Dict isFollowed(TFollow tFollow);
}

