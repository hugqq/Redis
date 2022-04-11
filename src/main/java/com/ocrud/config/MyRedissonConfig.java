package com.ocrud.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Configuration
public class MyRedissonConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "shutdown") // 服务停止后调用 shutdown 方法。
    public RedissonClient redisson() throws IOException {
        // 1.创建配置
        Config config = new Config();
        // 集群模式
        // config.useClusterServers().addNodeAddress("127.0.0.1:7004", "127.0.0.1:7001");
        // 2.根据 Config 创建出 RedissonClient 示例。
        String host = env.getProperty("spring.redis.host");
        String port = StrUtil.isEmpty(env.getProperty("spring.redis.port")) ? "6379" : env.getProperty("spring.redis.port");
        String password = env.getProperty("spring.redis.password");
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }
}
