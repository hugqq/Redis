package com.ocrud.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocrud.entity.Constant;
import com.ocrud.entity.TOrders;
import com.ocrud.entity.TVouchers;
import com.ocrud.service.TOrdersService;
import com.ocrud.service.TVouchersService;
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
    private TVouchersService tVouchersService;
    @Autowired
    private TOrdersService tOrdersService;

    @SneakyThrows
    @RabbitHandler
    public void process(String msg) {
        TVouchers tVouchers = tVouchersService.getById(msg);
        tVouchers.setStockLeft(tVouchers.getStock() - tOrdersService.count(new LambdaQueryWrapper<TOrders>().eq(TOrders::getFkVoucherId, msg)));
        log.info("Received voucher: {}", tVouchers);
        tVouchersService.updateById(tVouchers);
    }
}
