package com.demo.redis;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RedisService {
    private final Jedis jedis;
    private final HashMap<String, String> cacheMap = new HashMap<>();

    public RedisService(Jedis jedis) {
        this.jedis = jedis;
        initializeCacheMap();
    }

    private void initializeCacheMap() {
        cacheMap.put("a", "a");
        cacheMap.put("b", "b");
        cacheMap.put("c", "c");
        cacheMap.put("d", "d");
        cacheMap.put("e", "e");
        cacheMap.put("f", "f");
    }

    public String addToCache(String key, String value) {
        return jedis.set(key, value);
    }

    public String getFromCache(String key) {
        return jedis.get(key);
    }

    public long deleteFromCache(String key) {
        return jedis.del(key);
    }

    public boolean existsInCache(String key) {
        return jedis.exists(key);
    }

    public String addListToCache() {
        try {
            Transaction transaction = jedis.multi();
            for (Map.Entry<String, String> item : cacheMap.entrySet()) {
                transaction.set(item.getKey(), item.getValue());
            }
            transaction.exec();
            return "Success";
        } catch (Exception e) {
            return "Transaction failed: " + e.getMessage();
        }
    }

    public String addListToCacheByPipeline() {
        try {
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<String, String> item : cacheMap.entrySet()) {
                pipeline.set(item.getKey(), item.getValue());
            }
            List<Object> result = pipeline.syncAndReturnAll();
            return result.stream().map(Object::toString).collect(Collectors.joining(";"));
        } catch (Exception e) {
            return "Pipeline failed: " + e.getMessage();
        }
    }

    public void pubListToChannel(String channel, String message) {
        jedis.publish(channel, message);
    }
}