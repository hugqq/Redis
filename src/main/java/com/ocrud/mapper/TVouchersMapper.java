package com.ocrud.mapper;

import com.ocrud.entity.TVouchers;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface TVouchersMapper extends BaseMapper<TVouchers> {

    /**
     * 减库存
     */
    @Update("update t_vouchers set stock_left = stock_left - 1 where id = #{id}")
    int stockDecrease(@Param("id") int id);
}

