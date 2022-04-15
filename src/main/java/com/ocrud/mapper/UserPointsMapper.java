package com.ocrud.mapper;

import com.ocrud.entity.PointsRankVO;
import com.ocrud.entity.UserPoints;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserPointsMapper extends BaseMapper<UserPoints> {

    // 添加积分
    @Insert("insert into t_user_points (fk_user_id, points, types, is_valid, create_date, update_date) " +
            " values (#{fkUserId}, #{points}, #{types}, 0, now(), now())")
    boolean add(UserPoints tUserPoints);


    /**
     * mysql 8.0 以下
     * 查询积分排行榜 TOPN
     */
    @Select("select t1.*,@rank:= @rank + 1 as ranks from (" +
            "    select fk_user_id as id,sum(points) as total" +
            "    from t_user_points,(SELECT @rank:=0 ) r " +
            "    group by fk_user_id" +
            "    order by total DESC limit #{top} ) as t1")
    List<PointsRankVO> findTopN57(@Param("top") Integer top);


    /**
     * mysql 8.0
     * 查询积分排行榜 TOPN
     */
    @Select("SELECT t1.fk_user_id AS id, " +
            " sum( t1.points ) AS total, " +
            " rank () over ( ORDER BY sum( t1.points ) DESC ) AS ranks" +
            " FROM t_user_points t1" +
            " WHERE t1.is_valid = 0" +
            " GROUP BY t1.fk_user_id " +
            " ORDER BY total DESC LIMIT #{top}")
    List<PointsRankVO> findTopN(@Param("top") Integer top);

    // 根据食客 ID 查询当前用户的积分排名
    @Select("SELECT id, total, ranks FROM (" +
            " SELECT t1.fk_user_id AS id, " +
            " sum( t1.points ) AS total, " +
            " rank () over ( ORDER BY sum( t1.points ) DESC ) AS ranks" +
            " FROM t_user_points t1" +
            " WHERE t1.is_valid = 0" +
            " GROUP BY t1.fk_user_id " +
            " ORDER BY total DESC ) r " +
            " WHERE id = #{userId}")
    PointsRankVO findUserRank(@Param("userId") Integer userId);





}

