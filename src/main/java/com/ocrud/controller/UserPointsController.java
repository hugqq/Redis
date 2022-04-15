package com.ocrud.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ocrud.entity.PointsRankVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.ocrud.entity.UserPoints;
import com.ocrud.service.UserPointsService;

import java.util.List;

/**
 * @author ocrud
 * @since 2022-04-13
 */

@RestController
@RequestMapping("points")
@Slf4j
public class UserPointsController {

    private final UserPointsService userPointsService;

    public UserPointsController(UserPointsService userPointsService) {
        this.userPointsService = userPointsService;
    }

    @RequestMapping("pageList")
    public Dict pageList(UserPoints tUserPoints,
                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         HttpServletRequest request) {
        Dict r = Dict.create();
        LambdaQueryWrapper<UserPoints> lambdaQueryWrapper = new LambdaQueryWrapper<UserPoints>();
        Page<UserPoints> page = userPointsService.page(new Page<>(pageNo, pageSize), lambdaQueryWrapper);
        r.put("data", page);
        return r;
    }


    @RequestMapping("add")
    public boolean add(Integer userId,
                       Integer points,
                       Integer types) {
        // 基本参数校验
        Assert.isFalse(userId == null || userId < 1, "用户不能为空");
        Assert.isFalse(points == null, "积分不能为空");
        Assert.isFalse(types == null, "请选择对应的积分类型");
        return userPointsService.add(userId, points, types);
    }



    @RequestMapping("findTopN")
    public List<PointsRankVO> findTopN(Integer userId, Integer top){
        // 基本参数校验
        Assert.isFalse(userId == null || userId < 1, "用户不能为空");
        Assert.isFalse(top == null || top < 1, "请输入排名数");
        return userPointsService.findTopN(userId,top);
    }

    @RequestMapping("findTopNFromRedis")
    public List<PointsRankVO> findTopNFromRedis(Integer userId, Integer top){
        // 基本参数校验
        Assert.isFalse(userId == null || userId < 1, "用户不能为空");
        Assert.isFalse(top == null || top < 1, "请输入排名数");
        return userPointsService.findTopNFromRedis(userId,top);
    }


}


