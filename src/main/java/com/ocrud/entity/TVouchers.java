package com.ocrud.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ocrud.entity.BaseEntity;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TVouchers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 代金券标题
    */
    private String title;

    /**
    * 缩略图
    */
    private String thumbnail;

    /**
    * 抵扣金额
    */
    private Integer amount;

    /**
    * 售价
    */
    private BigDecimal price;

    /**
    * -1=过期 0=下架 1=上架
    */
    private Integer status;

    /**
    * 开始使用时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startUseTime;

    /**
    * 过期时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expireTime;

    /**
    * 库存
    */
    private Integer stock;

    /**
    * 剩余数量
    */
    private Integer stockLeft;

    /**
    * 描述信息
    */
    private String description;

    /**
    * 使用条款
    */
    private String clause;


}
