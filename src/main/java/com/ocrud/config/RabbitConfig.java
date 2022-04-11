package com.ocrud.config;

import com.ocrud.entity.Constant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean(value = "exchange")
    public Exchange Exchange() {
        return ExchangeBuilder.fanoutExchange(Constant.RABBIT_EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Queue Queue() {
        return QueueBuilder.durable(Constant.RABBIT_QUEUE_NAME).withArgument("x-message-ttl", 2000).build();
    }

    @Bean
    public Binding Binding(@Qualifier("exchange") Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.RABBIT_KEY_NAME).noargs();
    }
}
