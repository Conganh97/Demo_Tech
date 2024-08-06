package com.demo.redis;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RedisService {
    private final Jedis jedis;
    private HashMap<String, String> cacheMap = new HashMap<>();

    {
        cacheMap.put("a", "a");
        cacheMap.put("b", "b");
        cacheMap.put("c", "c");
        cacheMap.put("d", "d");
        cacheMap.put("e", "e");
        cacheMap.put("f", "f");

        // tao listener den channel myChannel redis khong can tao channel 1 cach ro rang,
        // redis se tu dong tao channel khi pub 1 channel bat ki
//        JedisPubSub jedisPubSub = new JedisPubSub() {
//            @Override
//            public void onMessage(String channel, String message) {
//                System.out.println("Received message: " + message + " from channel: " + channel);
//            }
//        };
//        new Thread(()->{
//            try (Jedis subcriberJedis = new Jedis("localhost"))
//            {
//                subcriberJedis.subscribe(jedisPubSub,"myChannel");
//            }
//        }).start();
    }

    public RedisService(Jedis jedis) {
        this.jedis = jedis;
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

    // su dung transaction de thuc hien tuan tu cac lenh va rollback khi 1 lenh loi
    public String addListToCache() {
        try {
            Transaction transaction = jedis.multi();
            for (Map.Entry<String, String> item : cacheMap.entrySet()) {
                transaction.set(item.getKey(), item.getValue());
            }
            transaction.exec();
        } catch (Exception e) {
            return "Fail";
        }
        return "True";
    }

    // su dung pipline de thuc hien cac cau lenh ko lien quan den nhau cung luc
    // giam thoi gian thuc hien nhung dam bao toan ven du lieu
    public String addListToCacheByPipeline() {
        Pipeline pipeline = jedis.pipelined();
        for (Map.Entry<String, String> item : cacheMap.entrySet()) {
            pipeline.set(item.getKey(), item.getValue());
        }
        List<Object> result = pipeline.syncAndReturnAll();
        return result.stream().map(Object::toString).collect(Collectors.joining(";"));
    }

    public void pubListToChannel(String channel, String message){
        jedis.publish(channel,message);
    }


}
