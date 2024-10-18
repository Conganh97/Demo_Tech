package com.demo.didemo.service;

import com.demo.didemo.annotation.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentService {

    public void doSomething(){
        log.info("Payment service does something!");
    }
}
