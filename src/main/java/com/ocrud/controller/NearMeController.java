package com.ocrud.controller;

import com.ocrud.entity.NearMeUserVO;
import com.ocrud.service.NearMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NearMeController {
    @Autowired
    private NearMeService nearMeService;

    @RequestMapping("addUserLocation")
    public void addUserLocation(Integer userId, Float lon, Float lat) {
        nearMeService.addUserLocation(userId, lon, lat);
    }

    @RequestMapping("findNearMe")
    public List<NearMeUserVO> findNearMe(Integer userId, Integer radius, Float lon, Float lat) {
        return nearMeService.findNearMe(userId, radius, lon, lat);
    }

}
