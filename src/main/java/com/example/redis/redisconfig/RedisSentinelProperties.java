package com.example.redis.redisconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "redis.sentinel")
public class RedisSentinelProperties {

    private List<String> host;
    private Integer port;
    private String master;
}
