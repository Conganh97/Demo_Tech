//package com.demo.redis;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPubSub;
//
//public class RedisChannelListener {
//    private Jedis jedis;
//    private JedisPubSub jedisPubSub;
//    private String channel;
//
//    public RedisChannelListener(String host, int port, String channel) {
//        this.jedis = new Jedis(host, port);
//        this.channel = channel;
//        this.jedisPubSub = new JedisPubSub() {
//            @Override
//            public void onMessage(String channel, String message) {
//                System.out.println("Received message: " + message + " from channel: " + channel);
//                handleMessage(channel, message);
//            }
//        };
//    }
//
//    public void startListening() {
//        System.out.println("Subscribing to channel: " + channel);
//        jedis.subscribe(jedisPubSub, channel);
//        System.out.println("Subscription ended.");
//    }
//
//    public void stopListening() {
//        jedisPubSub.unsubscribe();
//    }
//
//    protected void handleMessage(String channel, String message) {
//        // Override this method to handle the message
//    }
//
//    public static void main(String[] args) {
//        RedisChannelListener listener = new RedisChannelListener("localhost", 6379, "myChannel");
//
//        // Start listening in the main thread (this will block)
//        new Thread(listener::startListening).start();
//
//        // Simulate some other work in the main thread
////        try (Jedis jedisPublisher = new Jedis("localhost")) {
////            jedisPublisher.publish("myChannel", "Hello, Redis!");
////        }
//
//        // Stop listening after some time (for demonstration purposes)
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        listener.stopListening();
//    }
//}