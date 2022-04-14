package com.ocrud.entity;


import lombok.Data;

@Data
public class PointsRankVO {

    private Integer id;
    // 总积分
    private Integer total;

    // 排名
    private Integer ranks;

    // 是否是自己
    private Boolean isMe;
}
