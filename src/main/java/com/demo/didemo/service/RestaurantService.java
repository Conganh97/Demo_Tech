package com.demo.didemo.service;

import com.demo.didemo.annotation.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@Component
public class RestaurantService {

    public void doSomething() {
        log.info("Restaurant service does something!");
    }

    public void logToday() {
        log.info("Today: {}", Instant.now());
    }
}
