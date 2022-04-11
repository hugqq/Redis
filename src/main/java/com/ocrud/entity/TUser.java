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
public class TUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 用户姓名
    */
    private String name;

    /**
    * 伪token
    */
    private String token;


}
