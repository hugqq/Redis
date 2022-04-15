package com.ocrud.service;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.Feeds;


public interface FeedsService extends IService<Feeds> {

    void addFollowingFeeds(Integer userId, Integer followingUserId, int type);

    Dict delete(Integer id, Integer userId);

    Page<Feeds> selectForPage(Integer page, Integer pageSize, Integer userId);
}

