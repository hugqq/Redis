package com.ocrud.controller;

import com.ocrud.entity.NearMeUserVO;
import com.ocrud.service.NearMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 附近的人
 * @author glow
 */
@RestController
public class NearMeController {

    private final NearMeService nearMeService;

    public NearMeController(NearMeService nearMeService) {
        this.nearMeService = nearMeService;
    }

    /**
     * 增加经纬度
     */
    @RequestMapping("addUserLocation")
    public void addUserLocation(Integer userId, Float lon, Float lat) {
        nearMeService.addUserLocation(userId, lon, lat);
    }
    /**
     * 查找附近的人
     */
    @RequestMapping("findNearMe")
    public List<NearMeUserVO> findNearMe(Integer userId, Integer radius, Float lon, Float lat) {
        return nearMeService.findNearMe(userId, radius, lon, lat);
    }
    /**
     * 计算俩人之间的距离
     */
    @RequestMapping("distance")
    public String distance(Integer userId,Integer toUserId) {
        return nearMeService.distance(userId, toUserId);
    }
}
