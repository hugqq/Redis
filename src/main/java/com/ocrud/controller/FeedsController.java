package com.ocrud.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.ocrud.entity.TFeeds;
import com.ocrud.service.TFeedsService;

/**
 * @author ocrud
 * @since 2022-04-12
 */

@RestController
@Slf4j
public class FeedsController {

    private final TFeedsService tFeedsService;

    public FeedsController(TFeedsService tFeedsService) {
        this.tFeedsService = tFeedsService;
    }

    @RequestMapping("selectFeedPage")
    public Page<TFeeds> selectFeedPage(TFeeds tFeeds) {
        return tFeedsService.selectForPage(1, 10, tFeeds.getUserId());
    }

    @RequestMapping("delete")
    public Dict delete(TFeeds tFeeds) {
      return  tFeedsService.delete(tFeeds.getId(), tFeeds.getFkUserId());
    }

}


