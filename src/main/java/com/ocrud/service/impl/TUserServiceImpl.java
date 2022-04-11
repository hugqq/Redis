package com.ocrud.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TUser;
import com.ocrud.mapper.TUserMapper;
import com.ocrud.service.TUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-11
 */

@Transactional
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

}

