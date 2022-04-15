package com.ocrud.mapper;

import com.ocrud.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}

