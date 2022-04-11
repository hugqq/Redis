package com.ocrud.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.TVouchers;
import com.ocrud.mapper.TVouchersMapper;
import com.ocrud.service.TVouchersService;

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
public class TVouchersServiceImpl extends ServiceImpl<TVouchersMapper, TVouchers> implements TVouchersService {

    @Override
    public int stockDecrease(Integer fkVoucherId) {
            return baseMapper.stockDecrease(fkVoucherId);
    }
}

