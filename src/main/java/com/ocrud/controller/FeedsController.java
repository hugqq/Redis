package com.ocrud.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import com.ocrud.entity.TFeeds;
import com.ocrud.service.TFeedsService;

/**
 * @author ocrud
 * @since 2022-04-12
 */

@RestController
@RequestMapping("tFeeds")
@Slf4j
public class FeedsController {

    private final TFeedsService tFeedsService;

    public FeedsController(TFeedsService tFeedsService) {
        this.tFeedsService = tFeedsService;
    }

    @RequestMapping("selectForPage")
    public Page<TFeeds> selectForPage(TFeeds tFeeds) {
        return tFeedsService.selectForPage(1, 10, tFeeds.getFkUserId());
    }

    @RequestMapping("delete")
    public void delete(TFeeds tFeeds) {
        tFeedsService.delete(tFeeds.getId(), tFeeds.getFkUserId());
    }

}


