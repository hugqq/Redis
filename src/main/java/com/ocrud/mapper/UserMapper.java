package com.ocrud.mapper;

import com.ocrud.entity.User;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}

