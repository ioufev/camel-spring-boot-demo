package com.ioufev.route;

import com.ioufev.job.DeviceJob;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * DemoRoute：核心数据流路由
 * - 使用 cron 定时器生成电压数据并发布至 MQTT
 * - 订阅 MQTT 主题数据，写入文件和调用 sendService
 */
@RequiredArgsConstructor
@Configuration
public class DemoRoute extends RouteBuilder {

    private final CamelContext camelContext;
    private final DeviceJob deviceJob;

    @Override
    public void configure() throws Exception {
        String cron = "0/20 * * * * *"; // 每 20 秒执行一次

        // 路由 1：定时生成模拟数据并发布到 MQTT topic
        from("cron://myCron?schedule=" + cron)
                .routeId("genDeviceDataRoute")
                .bean(deviceJob, "voltage") // 获取电压值
                .to("paho-mqtt5:device1/voltage"); // 发布到 MQTT

        // 路由 2：订阅 MQTT 所有 device1/# 主题
        from("paho-mqtt5:device1/#")
                .routeId("mqtt5Route")
                .to("direct:send") // 调用 SendRoute 进行业务处理
                .process(exchange -> {
                    // 处理消息体，设置文件名和写入格式
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setHeader("CamelFileName", "device1-" + System.currentTimeMillis() + ".txt");
                    exchange.getIn().setBody("[Message] " + body + "\n");
                })
                .to("file:E:\\data\\test?fileExist=Append"); // 追加写入文件
    }
}
