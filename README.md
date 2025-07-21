## 📘 Apache Camel Spring Boot MQTT 示例项目

这是一个基于 **Apache Camel** 与 **Spring Boot** 的简单演示项目，主要实现以下功能：

* 使用 Camel 的 `cron` 定时器模拟设备数据生成
* 使用 `paho-mqtt5` 组件发布设备数据到 MQTT Broker
* 订阅 MQTT 主题消息，调用本地业务服务处理并追加写入文件

## 项目结构说明

```
camel-spring-boot-demo/
├── src/
│   ├── main/
│   │   ├── java/com/ioufev/
│   │   │   ├── job/DeviceJob.java           # 模拟设备数据（如电压）
│   │   │   ├── service/SendService.java     # 接收 MQTT 消息并打印
│   │   │   ├── route/SendRoute.java         # 路由：direct:send → 业务服务
│   │   │   └── route/DemoRoute.java         # 核心路由：MQTT 发布/订阅
│   │   └── resources/
│   │       ├── application.yml              # MQTT 与 Camel 配置
│   │       └── ...
├── pom.xml                                  # Maven 项目定义文件
└── README.md                                # 项目说明文档（本文件）
```

## 快速开始

### 环境要求

* JDK 8+
* Maven 3.6+
* 已安装或运行中的 MQTT Broker（如 [Mosquitto](https://mosquitto.org/)）

### 克隆项目

```bash
git clone https://github.com/ioufev/camel-spring--boot-demo.git
cd camel-spring--boot-demo
```

### 修改配置

编辑 `application.yml`：

```yaml
camel:
  component:
    paho-mqtt5:
      brokerUrl: tcp://127.0.0.1:1883 # 修改为你的 MQTT Broker 地址
```

### 启动项目

```bash
mvn spring-boot:run
```

## 路由说明

### 1. 定时任务 → 发布 MQTT 消息

```java
from("cron://myCron?schedule=0/20 * * * * *")
    .bean(deviceJob, "voltage")
    .to("paho-mqtt5:device1/voltage");
```

> 每隔 20 秒，发送一次 "380" 电压值到 MQTT 主题 `device1/voltage`

### 2. 订阅 MQTT → 调用服务 → 写入文件

```java
from("paho-mqtt5:device1/#")
    .to("direct:send")
    .process(...) // 格式化消息
    .to("file:E:\\data\\test?fileExist=Append");
```

> 订阅 `device1/#` 下所有消息，将内容通过 `SendService` 打印，并保存到本地文件


## 示例的思路

* ✅ 使用 Apache Camel 的 EIP 路由机制简洁实现数据流转
* ✅ MQTT 消息传输，模拟物联网终端通信
* ✅ 路由可扩展，适合接入更多服务，如数据库、REST 模块
* ✅ 演示了 `cron`、`direct`、`bean`、`file` 等常见组件用法

