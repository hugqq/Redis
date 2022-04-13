package com.ocrud.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import com.ocrud.entity.TUserPoints;
import com.ocrud.service.TUserPointsService;

/**
 * @author ocrud
 * @since 2022-04-13
 */

@RestController
@RequestMapping("tUserPoints")
@Slf4j
public class UserPointsController  {

    private final TUserPointsService tUserPointsService;

    @Autowired
    public UserPointsController(TUserPointsService tUserPointsService){
        this.tUserPointsService = tUserPointsService;
    }

    @GetMapping("pageList")
    public Dict pageList(TUserPoints tUserPoints,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request)
    {
        Dict r = Dict.create();
        LambdaQueryWrapper<TUserPoints> lambdaQueryWrapper = new LambdaQueryWrapper<TUserPoints>();
        Page<TUserPoints> page = tUserPointsService.page(new Page<>(pageNo, pageSize), lambdaQueryWrapper);
        r.put("data", page);
        return r;
    }


    @PostMapping("save")
    public Dict save(TUserPoints tUserPoints, HttpServletRequest request)
    {
        Dict r = Dict.create();
        if (tUserPointsService.add(tUserPoints)) {
            TUserPoints one = tUserPointsService.getById(tUserPoints.getId());
            r.put("data", one);
            return r;
        }
        return null;
    }


}


