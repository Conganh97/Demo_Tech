package com.demo.proxy.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class InterfaceImpl implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("Invoke implement interface by Proxy");
        return null;
    }
}
