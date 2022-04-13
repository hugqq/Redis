package com.ocrud.service;

import cn.hutool.core.lang.Dict;

import java.util.Date;
import java.util.Map;

public interface SignInService {

     int doSign(Integer userId, String dateStr);

    int getContinuousSignCount(Integer userId, Date date);

    long getSignCount(Integer userId, String dateStr);

    Map<String, Boolean> getSignInfo(Integer userId, String dateStr);

    Dict getFirstTimeSignInfo(Integer userId, String dateStr);

    Dict getTodaySignUser(String dateStr);
}
