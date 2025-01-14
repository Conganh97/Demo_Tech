package com.demo.proxy.service.serviceimpl;

import com.demo.proxy.service.MyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerServiceImpl implements MyService {
    @Override
    public void performTask(String name) {
        log.info("CustomerServiceImpl is performing task: {}", name);
    }
}
