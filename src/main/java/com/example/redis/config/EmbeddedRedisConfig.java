package com.example.redis.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;

@Configuration
public class EmbeddedRedisConfig {

    private final RedisServer redisServer;

    public EmbeddedRedisConfig(RedisProperties redisProperties) throws IOException {
        if (isArmMac()) {
            this.redisServer = new RedisServer(getRedisBinaryFile(), redisProperties.getPort());
        } else {
            this.redisServer = new RedisServer(redisProperties.getPort());
        }
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

    private boolean isArmMac() {
        return System.getProperty("os.name").equals("Mac OS X");
    }

    private File getRedisBinaryFile() throws IOException {
        return new ClassPathResource("binary/redis/redis-server-6.2.5").getFile();
    }
}
