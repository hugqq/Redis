package com.ocrud.service;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TFeeds;


public interface TFeedsService extends IService<TFeeds> {

    void addFollowingFeeds(Integer userId, Integer followingUserId, int type);

    Dict delete(Integer id, Integer userId);

    Page<TFeeds> selectForPage(Integer page, Integer pageSize, Integer userId);
}

