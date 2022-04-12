package com.ocrud.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.TFeeds;


public interface TFeedsService extends IService<TFeeds> {

    void addFollowingFeeds(Integer userId, Integer followingUserId, int type);

    void delete(Integer id, Integer userId);

    Page<TFeeds> selectForPage(Integer page, Integer pageSize, Integer userId);
}

