package com.ocrud.service;

import com.ocrud.entity.NearMeUserVO;

import java.util.List;

public interface NearMeService {

    void addUserLocation(Integer userId, Float lon, Float lat);

    List<NearMeUserVO> findNearMe(Integer userId, Integer radius, Float lon, Float lat);

    String distance(Integer userId, Integer toUserId);
}
