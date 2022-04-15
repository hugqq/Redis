package com.ocrud.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocrud.entity.Vouchers;
import com.ocrud.mapper.VouchersMapper;
import com.ocrud.service.VouchersService;

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
public class VouchersServiceImpl extends ServiceImpl<VouchersMapper, Vouchers> implements VouchersService {

    @Override
    public int stockDecrease(Integer fkVoucherId) {
            return baseMapper.stockDecrease(fkVoucherId);
    }
}

