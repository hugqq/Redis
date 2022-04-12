package com.ocrud.controller;

import cn.hutool.core.lang.Dict;
import com.ocrud.entity.TFollow;
import com.ocrud.entity.TUser;
import com.ocrud.service.TFollowService;
import com.ocrud.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 好友功能
 * @author glow
 */
@RestController
public class FollowController {

    private final TFollowService tFollowService;
    private final TUserService tUserService;

    public FollowController(TFollowService tFollowService, TUserService tUserService) {
        this.tFollowService = tFollowService;
        this.tUserService = tUserService;
    }

    /**
     * 查询所有
     */
    @RequestMapping("/selectFollow")
    public List<TFollow> selectFollow() {
        Map<Integer, String> userMap = tUserService.list().stream().collect(Collectors.toMap(TUser::getId, TUser::getName));
        List<TFollow> list = tFollowService.list();
        list.forEach(e -> {
                    e.setUserName(userMap.get(e.getUserId()));
                    e.setFollowUserName("关注了："+userMap.get(e.getFollowUserId()));
        }
        );
        return list;
    }

    /**
     * 关注/取关
     * isFollowed 1关注 0取关
     */
    @RequestMapping("/follow")
    public Dict follow(TFollow tFollow) {
        return tFollowService.follow(tFollow);
    }

    /**
     * 共同好友
     */
    @RequestMapping("/findCommonsFriends")
    public Dict findCommonsFriends(TFollow tFollow) {
        return tFollowService.findCommonsFriends(tFollow);
    }

    /**
     * 关注列表
     */
    @RequestMapping("/findFollowing")
    public Dict findFollowing(TFollow tFollow) {
        return tFollowService.findFollowing(tFollow);
    }

    /**
     * 粉丝列表
     */
    @RequestMapping("/findFollowers")
    public Dict findFollowers(TFollow tFollow) {
        return tFollowService.findFollowers(tFollow);
    }

    /**
     * 判断用户是否关注
     */
    @RequestMapping("/isFollowed")
    public Dict isFollowed(TFollow tFollow) {
        return tFollowService.isFollowed(tFollow);
    }
}
