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
public class TOrders extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 订单号
    */
    private String orderNo;

    /**
    * 优惠券id
    */
    private Integer fkVoucherId;

    /**
    * 用户id
    */
    private Integer fkUserId;

    /**
    * 如果是抢购订单时，抢购订单的id
    */
    private Integer fkSeckillId;

    /**
    * 订单状态：-1=已取消 0=未支付 1=已支付 2=已消费 3=已过期
    */
    private Integer status;

    /**
    * 订单类型：0=正常订单 1=抢购订单
    */
    private Integer orderType;

}
