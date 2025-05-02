# 脚手架程序
总结日常开发中常用到的脚手架，以提高开发效率。

# Springboot 脚手架

1. 常用工具类，如日期，Json等等；
2. 国际化处理；
3. 统一的异常处理；
4. 请求日志处理；
5. Token 缓存管理类，结合缓存保证 token 的有效性；
6. 定义一套编码风格；
7. 加入代码生成器。

# Socket 脚手架

1. 常用工具类，如 Json 等；
2. 加入业务线程池；
3. 支持 websocket 和 tcp 两种协议。

# Socket RPC 脚手架
## 功能介绍

1. Socket & MQ 使用同一套协议 RPC 进行通信；
2. Socket 支持 websocket 和 tcp 两种协议；
3. MQ 支持 nats；
4. Socket 支持心跳、离线消息缓存及断线重连功能；
5. 支持服务及用户在线功能；
6. 支持消息推送功能。

## 工程结构

```txt
socket-rpc-scaffolding
|-- socket-rpc-app
|   |-- socket-rpc-app-base
|   |-- socket-rpc-app-nats
|   |-- socket-rpc-app-online
|   |-- socket-rpc-app-user
|   `-- socket-rpc-app-wsgw
|-- socket-rpc-common
|-- socket-rpc-mq
|-- socket-rpc-tcp
|-- socket-rpc-ws
`-- sql
```

**目录说明：**

- socket-rpc-common: RPC 公共模块；
- socket-rpc-mq: Nats mq rpc 模块；
- socket-rpc-tcp: TCP rpc 模块；
- socket-rpc-ws: Websocket rpc 模块；
- socket-rpc-app：应用模块；
- socket-rpc-app-base: 应用公共模块；
- socket-rpc-app-nats：nats 模块公共模块；
- socket-rpc-app-online：服务及用户在线模块；
- socket-rpc-app-user：用户模块；
- socket-rpc-app-wsgw：Websocket 网关；
- sql：数据库脚本

详情参考文章：[Socket RPC 脚手架](https://zhangxt.top/2025/05/02/socket-rpc-scaffolding/)

