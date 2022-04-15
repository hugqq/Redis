package com.ocrud.controller;

import cn.hutool.core.lang.Dict;
import com.ocrud.entity.Follow;
import com.ocrud.entity.User;
import com.ocrud.service.FollowService;
import com.ocrud.service.UserService;
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

    private final FollowService followService;
    private final UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    /**
     * 查询所有
     */
    @RequestMapping("/selectFollow")
    public List<Follow> selectFollow() {
        Map<Integer, String> userMap = userService.list().stream().collect(Collectors.toMap(User::getId, User::getName));
        List<Follow> list = followService.list();
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
    public Dict follow(Follow follow) {
        return followService.follow(follow);
    }

    /**
     * 共同好友
     */
    @RequestMapping("/findCommonsFriends")
    public Dict findCommonsFriends(Follow follow) {
        return followService.findCommonsFriends(follow);
    }

    /**
     * 关注列表
     */
    @RequestMapping("/findFollowing")
    public Dict findFollowing(Follow follow) {
        return followService.findFollowing(follow);
    }

    /**
     * 粉丝列表
     */
    @RequestMapping("/findFollowers")
    public Dict findFollowers(Follow follow) {
        return followService.findFollowers(follow);
    }

    /**
     * 判断用户是否关注
     */
    @RequestMapping("/isFollowed")
    public Dict isFollowed(Follow follow) {
        return followService.isFollowed(follow);
    }
}
