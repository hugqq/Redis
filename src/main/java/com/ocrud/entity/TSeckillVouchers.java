package com.ocrud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ocrud.entity.BaseEntity;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSeckillVouchers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer fkVoucherId;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private LocalDateTime endTime;

}
