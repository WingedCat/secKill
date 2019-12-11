package edu.xpu.hcp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hcp
 */
@Component
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.timeout}")
    private int timeout;
    @Value("${redis.max-idle}")
    private int poolMaxIdle;
    @Value("${redis.max-wait}")
    private int poolMaxWait;
    @Value("${redis.max-total}")
    private int poolMaxTotal;
}
