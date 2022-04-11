package com.ocrud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class TFollow extends BaseEntity {

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
