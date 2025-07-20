package com.ioufev.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用于打印 MQTT 消息内容，模拟业务处理逻辑
 */
@Slf4j
@Service
public class SendService {
    public void send(String message){
        log.info("消息{}", message); // 日志输出
        System.out.println("消息" + message); // 控制台输出
    }
}
