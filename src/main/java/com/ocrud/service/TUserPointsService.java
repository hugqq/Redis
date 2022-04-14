package com.ocrud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.ocrud.entity.PointsRankVO;
import com.ocrud.entity.TUserPoints;

import java.util.List;


public interface TUserPointsService extends IService<TUserPoints> {

    Boolean add(Integer userId, Integer points, Integer types);

    Boolean addPoints(int count, Integer userId);

    List<PointsRankVO> findTopN(Integer userId, Integer top);

    List<PointsRankVO> findTopNFromRedis(Integer userId, Integer top);
}

