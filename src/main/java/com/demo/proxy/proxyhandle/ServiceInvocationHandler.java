package com.demo.proxy.proxyhandle;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ServiceInvocationHandler implements InvocationHandler {
    private final Object customer;

    public ServiceInvocationHandler(Object customer) {
        this.customer = customer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Before method");
        Object result = method.invoke(customer, args);
        log.info("After method");
        return result;
    }
}
