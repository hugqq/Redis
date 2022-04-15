package com.ocrud.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.ocrud.entity.Feeds;
import com.ocrud.service.FeedsService;

/**
 * Feed流系统
 * @author ocrud
 * @since 2022-04-12
 */

@RestController
@Slf4j
public class FeedsController {

    private final FeedsService feedsService;

    public FeedsController(FeedsService feedsService) {
        this.feedsService = feedsService;
    }

    @RequestMapping("selectFeedPage")
    public Page<Feeds> selectFeedPage(Feeds feeds) {
        return feedsService.selectForPage(1, 10, feeds.getUserId());
    }

    @RequestMapping("delete")
    public Dict delete(Feeds feeds) {
      return  feedsService.delete(feeds.getId(), feeds.getFkUserId());
    }

}


