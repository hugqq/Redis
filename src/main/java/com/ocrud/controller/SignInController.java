package com.ocrud.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.ocrud.service.SignInService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 签到
 * @author glow
 */
@RestController
public class SignInController {

    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    /**
     * 用户签到
     *
     * @param userId
     * @param dateStr 某个日期 yyyy-MM-dd
     * @return
     */
    @RequestMapping("doSign")
    public int doSign(Integer userId, String dateStr) {
        return signInService.doSign(userId, dateStr);
    }

    /**
     * 统计用户某月不算今天连续签到次数
     *
     * @param userId
     * @param dateStr 某个日期 yyyy-MM-dd
     * @return
     */
    @RequestMapping("getContinuousSignCount")
    public int getContinuousSignCount(Integer userId, String dateStr) {
        DateTime dateTime = null;
        try {
            dateTime = DateUtil.parse(dateStr, "yyyy-MM-dd");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("日期格式错误");
        }
        return signInService.getContinuousSignCount(userId, dateTime);
    }

    /**
     * 获取签到次数 默认当月
     *
     * @param userId
     * @param dateStr 某个日期 yyyy-MM-dd
     * @return
     */
    @RequestMapping("getSignCount")
    public Long getSignCount(Integer userId, String dateStr) {
        return signInService.getSignCount(userId, dateStr);
    }


    /**
     * 获取用户签到情况 默认当月
     *
     * @param userId
     * @param dateStr 某个日期 yyyy-MM-dd
     * @return
     */
    @RequestMapping("getSignInfo")
    public Map<String, Boolean> getSignInfo(Integer userId, String dateStr) {
        return signInService.getSignInfo(userId, dateStr);
    }



    /**
     * 获取用户首次签到情况 默认当月
     *
     * @param userId
     * @param dateStr 某个日期 yyyy-MM-dd
     * @return
     */
    @RequestMapping("getFirstTimeSignInfo")
    public Dict getFirstTimeSignInfo(Integer userId, String dateStr) {
        return signInService.getFirstTimeSignInfo(userId, dateStr);
    }

    /**
     * 获取指定日期签到用户
     */
    @RequestMapping("getTodaySignUser")
    public Dict getTodaySignUser(String dateStr) {
        return signInService.getTodaySignUser(dateStr);
    }

}
