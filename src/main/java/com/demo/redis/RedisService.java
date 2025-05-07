package com.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for Redis operations using JedisPool.
 */
@Service
public class RedisService {
    private final JedisPool jedisPool;
    private final Map<String, String> defaultCacheItems;

    /**
     * Constructs a new RedisService with the given JedisPool.
     *
     * @param jedisPool The JedisPool to use for Redis operations
     */
    @Autowired
    public RedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.defaultCacheItems = initializeDefaultCacheItems();
    }

    /**
     * Initialize the default cache items.
     *
     * @return Map containing default cache items
     */
    private Map<String, String> initializeDefaultCacheItems() {
        return Map.of("a", "a", "b", "b", "c", "c", "d", "d", "e", "e", "f", "f");
    }

    /**
     * Adds a key-value pair to the Redis cache.
     *
     * @param key   The key to add
     * @param value The value to add
     * @return Status code reply
     */
    public String set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        }
    }

    /**
     * Retrieves a value from the Redis cache by key.
     *
     * @param key The key to retrieve
     * @return The value associated with the key, or null if the key doesn't exist
     */
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * Deletes a key-value pair from the Redis cache.
     *
     * @param key The key to delete
     * @return The number of keys that were removed
     */
    public long delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        }
    }

    /**
     * Checks if a key exists in the Redis cache.
     *
     * @param key The key to check
     * @return true if the key exists, false otherwise
     */
    public boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    /**
     * Adds the default cache items to Redis using a transaction.
     *
     * @return Status message indicating success or failure
     */
    public String addListToCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                Transaction transaction = jedis.multi();
                for (Map.Entry<String, String> item : defaultCacheItems.entrySet()) {
                    transaction.set(item.getKey(), item.getValue());
                }
                transaction.exec();
                return "Transaction completed successfully";
            } catch (Exception e) {
                return "Transaction failed: " + e.getMessage();
            }
        }
    }

    /**
     * Adds the default cache items to Redis using a pipeline.
     *
     * @return Response messages from Redis
     */
    public String addListToCacheByPipeline() {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                Pipeline pipeline = jedis.pipelined();
                for (Map.Entry<String, String> item : defaultCacheItems.entrySet()) {
                    pipeline.set(item.getKey(), item.getValue());
                }
                List<Object> results = pipeline.syncAndReturnAll();
                return results.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("; "));
            } catch (Exception e) {
                return "Pipeline failed: " + e.getMessage();
            }
        }
    }

    /**
     * Publishes a message to a Redis channel.
     *
     * @param channel The channel to publish to
     * @param message The message to publish
     * @return The number of clients that received the message
     */
    public long pubListToChannel(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.publish(channel, message);
        }
    }
}