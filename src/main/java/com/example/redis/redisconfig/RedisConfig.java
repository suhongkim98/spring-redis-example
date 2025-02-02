package com.example.redis.redisconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties(RedisSentinelProperties.class)
public class RedisConfig {

    public final int port;
    public final List<String> host;
    private final String master;

    public RedisConfig(RedisSentinelProperties redisSentinelProperties) {
        this.host = redisSentinelProperties.getHost();
        this.port = redisSentinelProperties.getPort();
        this.master = redisSentinelProperties.getMaster();
    }

    @Bean
    public LettuceConnectionFactory sentinelRedisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(master);

        for (var host : host) {
            sentinelConfig.addSentinel(new RedisNode(host, port));
        }

        return new LettuceConnectionFactory(sentinelConfig);
    }

//    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}