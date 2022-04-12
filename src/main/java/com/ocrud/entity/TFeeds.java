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
public class TFeeds extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 内容
     */
    private String content;

    private Integer fkUserId;

    /**
     * 点赞数量
     */
    private Integer praiseAmount;

    /**
     * 评论数量
     */
    private Integer commentAmount;

    @TableField(exist = false)
    private TUser user;
}
