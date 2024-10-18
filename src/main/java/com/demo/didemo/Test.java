package com.demo.didemo;

import com.demo.didemo.annotation.Autowire;
import com.demo.didemo.annotation.Component;
import com.demo.didemo.loader.ContextLoader;
import com.demo.didemo.loader.Runner;
import com.demo.didemo.service.OrderService;
import com.demo.didemo.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Component
public class Test implements Runner {

    @Autowire
    private OrderService orderService;

    public static void main (String [] args){
        ContextLoader.getInstance().load("com.demo.didemo");
    }

    @Override
    public void run (){
        log.info("Test ready to start");

        orderService.makeOrder();

        val restaurantService = ContextLoader.getInstance()
                .getBean(RestaurantService.class);

        restaurantService.logToday();
    }
}
