package com.ocrud.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TUserPoints;
import com.ocrud.mapper.TUserPointsMapper;
import com.ocrud.service.TUserPointsService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ocrud
 * @since 2022-04-13
 */

@Transactional
@Service
public class TUserPointsServiceImpl extends ServiceImpl<TUserPointsMapper, TUserPoints> implements TUserPointsService {

    @Override
    public boolean add(TUserPoints tUserPoints) {
        return baseMapper.add(tUserPoints);
    }
}

