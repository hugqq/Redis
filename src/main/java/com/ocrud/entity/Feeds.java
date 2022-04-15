package com.ocrud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_feeds")
public class Feeds extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 内容
     */
    private String content;
    /**
     * userId
     */
    private Integer fkUserId;
    /**
     * 点赞数量
     */
    private Integer praiseAmount;

    /**
     * 评论数量
     */
    private Integer commentAmount;
    /**
     * 参数
     */
    @TableField(exist = false)
    private Integer userId;

    @TableField(exist = false)
    private User user;
}
