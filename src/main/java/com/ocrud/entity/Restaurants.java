package com.ocrud.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ocrud.entity.BaseEntity;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_restaurants")
public class Restaurants extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 餐厅英文名称
    */
    private String name;

    /**
    * 餐厅中文名称
    */
    private String cnname;

    /**
    * 经度
    */
    private Double x;

    /**
    * 纬度
    */
    private Double y;

    /**
    * 英文位置信息
    */
    private String location;

    /**
    * 中文位置信息
    */
    private String cnlocation;

    /**
    * 商圈，比如徐家汇
    */
    private String area;

    /**
    * 餐厅电话
    */
    private String telephone;

    /**
    * 餐厅邮箱
    */
    private String email;

    /**
    * 餐厅官网
    */
    private String website;

    /**
    * 菜系
    */
    private String cuisine;

    /**
    * 均价
    */
    private String averagePrice;

    /**
    * 介绍
    */
    private String introduction;

    /**
    * 缩略图
    */
    private String thumbnail;

    /**
    * 喜欢数量
    */
    private Integer likeVotes;

    /**
    * 不喜欢数量
    */
    private Integer dislikeVotes;

    /**
    * 城市id
    */
    private Integer cityId;

}
