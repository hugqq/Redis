package com.ocrud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_follow")
public class Follow extends BaseEntity {

    /**
    * 用户
    */
    private Integer userId;

    /**
    * 粉丝
    */
    private Integer followUserId;

    /**
     * 是否关注
     */
    private Integer isFollowed;
    /**
     * 用户
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 粉丝
     */
    @TableField(exist = false)
    private String followUserName;
}
