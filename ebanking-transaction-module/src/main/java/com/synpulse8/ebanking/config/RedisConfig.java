package com.synpulse8.ebanking.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedisConfig {
    public RedissonClient redissonClient() {
        log.info("redissonClient================");
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://ebanking-redis:6379");
        RedissonClient client = Redisson.create(config);
        return client;
    }
}
