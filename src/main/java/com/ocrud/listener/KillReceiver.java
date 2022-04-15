package com.ocrud.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocrud.entity.Constant;
import com.ocrud.entity.Orders;
import com.ocrud.entity.Vouchers;
import com.ocrud.service.OrdersService;
import com.ocrud.service.VouchersService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author glow
 */
@Component
@Slf4j
@RabbitListener(queues = Constant.RABBIT_QUEUE_NAME)
public class KillReceiver {

    @Autowired
    private VouchersService vouchersService;
    @Autowired
    private OrdersService ordersService;

    @SneakyThrows
    @RabbitHandler
    public void process(String msg) {
        Vouchers vouchers = vouchersService.getById(msg);
        vouchers.setStockLeft(vouchers.getStock() - ordersService.count(new LambdaQueryWrapper<Orders>().eq(Orders::getFkVoucherId, msg)));
        log.info("Received voucher: {}", vouchers);
        vouchersService.updateById(vouchers);
    }
}
