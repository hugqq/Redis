package com.ocrud.service;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.Follow;

import java.util.Set;


public interface FollowService extends IService<Follow> {
    Dict follow(Follow tFollow);

    Dict findCommonsFriends(Follow tFollow);

    Set<Integer> findFollowing(Integer userId);

    Set<Integer> findFollowers(Integer userId);

    Dict findFollowing(Follow tFollow);

    Dict findFollowers(Follow tFollow);

    Dict isFollowed(Follow tFollow);
}

