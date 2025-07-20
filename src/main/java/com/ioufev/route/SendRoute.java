package com.ioufev.route;

import com.ioufev.service.SendService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * SendRoute：处理 direct:send 路由的逻辑
 * 将消息交由 SendService 中的方法进行处理
 */
@RequiredArgsConstructor
@Configuration
public class SendRoute extends RouteBuilder {

    private final SendService sendService;

    @Override
    public void configure() throws Exception {
        from("direct:send") // 接收来自其他路由的消息
                .bean(sendService, "send"); // 调用 sendService.send() 方法处理消息
    }
}
