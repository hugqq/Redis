package com.ocrud.mapper;

import com.ocrud.entity.TUserPoints;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


@Mapper
public interface TUserPointsMapper extends BaseMapper<TUserPoints> {

    // 添加积分
    @Insert("insert into t_user_points (fk_user_id, points, types, is_valid, create_date, update_date) " +
            " values (#{fkUserId}, #{points}, #{types}, 0, now(), now())")
    boolean save(TUserPoints tUserPoints);
}

