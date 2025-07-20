package com.ioufev.job;

import org.springframework.context.annotation.Configuration;

/**
 * 模拟设备任务类
 * 提供一个生成电压数据的方法，供 Camel 定时调度时调用
 */
@Configuration
public class DeviceJob {
    public String voltage(){
        return "380";
    }
}
