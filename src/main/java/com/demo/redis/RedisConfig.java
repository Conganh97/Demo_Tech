package com.demo.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis configuration class that provides beans for Redis connection.
 */
@Configuration
public class RedisConfig implements DisposableBean {

    @Value("${redis.host:localhost}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private int redisPort;

    @Value("${redis.password:}")
    private String redisPassword;

    @Value("${redis.jedis.pool.max-active:8}")
    private int maxActive;

    @Value("${redis.jedis.pool.max-idle:8}")
    private int maxIdle;

    @Value("${redis.jedis.pool.min-idle:0}")
    private int minIdle;

    @Value("${redis.jedis.pool.time-out:2000}")
    private int timeout;

    @Value("${redis.jedis.pool.max-wait:-1}")
    private long maxWaitMillis;

    @Value("${redis.jedis.pool.test-on-borrow:false}")
    private boolean testOnBorrow;

    @Value("${redis.jedis.pool.test-on-return:false}")
    private boolean testOnReturn;

    @Value("${redis.jedis.pool.test-while-idle:true}")
    private boolean testWhileIdle;

    private JedisPool jedisPool;

    /**
     * Creates a JedisPoolConfig with the configured pool settings.
     *
     * @return The configured JedisPoolConfig
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);
        return poolConfig;
    }

    /**
     * Creates a JedisPool with the configured settings.
     * Pool is managed by Spring as a singleton and will be closed when the application shuts down.
     *
     * @param poolConfig The pool configuration to use
     * @return The configured JedisPool
     */
    @Bean
    @Scope("singleton")
    public JedisPool jedisPool(JedisPoolConfig poolConfig) {
        if (jedisPool == null) {
            if (redisPassword != null && !redisPassword.isEmpty()) {
                jedisPool = new JedisPool(poolConfig, redisHost, redisPort, timeout, redisPassword);
            } else {
                jedisPool = new JedisPool(poolConfig, redisHost, redisPort, timeout);
            }
        }
        return jedisPool;
    }

    /**
     * Bean for MBeanServer to resolve dependency issues when JMX is involved.
     * This prevents errors like "Consider defining a bean of type 'javax.management.MBeanServer'".
     *
     * @return MBeanServerFactoryBean that will create the MBeanServer
     */
    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean mbeanServerFactoryBean = new MBeanServerFactoryBean();
        mbeanServerFactoryBean.setLocateExistingServerIfPossible(true);
        return mbeanServerFactoryBean;
    }

    /**
     * Implements DisposableBean to ensure JedisPool is closed when the Spring context is destroyed.
     * This ensures all Jedis connections are properly closed.
     */
    @Override
    public void destroy() throws Exception {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}