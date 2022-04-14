package com.ocrud.entity;

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
public class TUserPoints extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer fkUserId;

    private Integer points;

    /**
    * 积分类型：0=签到，1=关注好友，2=添加评论，3=点赞商户
    */
    private Integer types;

}
