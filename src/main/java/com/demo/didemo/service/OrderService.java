package com.demo.didemo.service;

import com.demo.didemo.annotation.Autowire;
import com.demo.didemo.annotation.Component;
import com.demo.didemo.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderService {

    @Autowire
    private PaymentService paymentService;

    @Autowire
    private RestaurantService restaurantService;

    @PostConstruct
    void postInitiate(){
        log.info("Do something after creating order service instance");
    }

    public void makeOrder(){
        paymentService.doSomething();
        restaurantService.doSomething();
    }
}
