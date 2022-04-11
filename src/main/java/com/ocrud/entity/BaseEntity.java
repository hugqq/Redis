package com.ocrud.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据Entity类
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;


    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    public Date createDate; // 创建日期



    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    public Date updateDate; // 更新日期

    @TableLogic
    private Integer isValid;
}
