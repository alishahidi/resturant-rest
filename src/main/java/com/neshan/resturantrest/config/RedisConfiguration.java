package com.neshan.resturantrest.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.*;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    private static final String LOCK_NAME = "lock";

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379"); // Replace with your Redis server address

        return Redisson.create(config);
    }

    @Bean(destroyMethod = "destroy")
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, LOCK_NAME);
    }
}
