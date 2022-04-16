package com.ocrud.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import com.ocrud.entity.Restaurants;
import com.ocrud.service.RestaurantsService;

/**
 * @author ocrud
 * @since 2022-04-15
 */

@RestController
@RequestMapping("restaurants")
@Slf4j
public class RestaurantsController {

    private final RestaurantsService restaurantsService;

    public RestaurantsController(RestaurantsService restaurantsService) {
        this.restaurantsService = restaurantsService;
    }

    @GetMapping("pageList")
    public Dict pageList(Restaurants restaurants,
                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         HttpServletRequest request) {

        Dict r = Dict.create();
        LambdaQueryWrapper<Restaurants> lambdaQueryWrapper = new LambdaQueryWrapper<Restaurants>();
        Page<Restaurants> page = restaurantsService.page(new Page<>(pageNo, pageSize), lambdaQueryWrapper);
        r.put("data", page);
        return r;
    }

    @GetMapping("getById")
    public Dict getById(String id, HttpServletRequest request) {
        Assert.isFalse(id == null, "请选择餐厅查看");
        return restaurantsService.getByIdCache(id);
    }




}


