# 复用文档

服务端基于 Netty 提供的异步的、事件驱动的架构开发，实现了 C/S 一对多的实时通信，涵盖了实时通信应用的基础功能点。客户端除了基于 Netty 提供网络层面框架以外，还借鉴了 JavaScript React/Redux 的设计思想，实现了状态驱动的界面渲染，数据流具有强方向性，有非常清晰的架构。

该项目各个模块为了实现高度的可测试、可复用、可扩展，遵循了以下设计模式:

- 依赖注入 (Dependency Injection Pattern)

- 监听器 (Observer Pattern)

- 拦截过滤器 (Filter Pattern)

## 术语说明

以下内容为数据流定义了不同术语：

- 请求 (Request)：客户端主动向服务端发送的数据

- 响应 (Response)：服务端收到请求后，向客户端发送回复数据

- 推送 (Push)：服务端主动向客户端推送的数据

- 告知 (Ack)：客户端收到推送数据后，向服务端发送的回复数据

## 可复用的管道模块

### AuthFilterHandler

[octoteam.tahiti.server.pipeline.AuthFilterHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/server/pipeline/AuthFilterHandler.html)

类别：消息过滤

作用：在管道中加入该模块后可以过滤上行和下行的未登录消息

该模块可以在未登录情况下过滤管道中的上行推送 (Push) 消息和下行请求 (Request) 消息。该模块会对每一个收到的下行 REQUEST 消息产生 MessageEvent 事件，事件中标记了该消息收到时是否已经登录。对于被过滤的下行消息，该模块还会发送状态为 NOT_AUTHENTICATED 的上行响应 (Response) 消息。被过滤的消息会被丢弃。

构造函数签名: 

```java
AuthFilterHandler()
```

### AuthRequestHandler

[octoteam.tahiti.server.pipeline.AuthRequestHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/server/pipeline/AuthRequestHandler.html)

类别：请求处理

作用：在管道中加入该模块后可以响应登录请求

该模块处理下行的登录请求 (Request)，通过 `AccountService` 进行验证。无论成功或者失败，该模块都会发送该请求的响应 (Response) 消息。响应消息会处于三种状态中的一种：成功、用户名未找到、用户名或密码错误。对于成功的情况，该模块还会标记当前连接状态为已登录。

构造函数签名：

```java
AuthRequestHandler(AccountService accountService)
```

参数：

- `accountService`: 身份验证服务，需要实现 `AccountService` 接口

### HeartbeatHandler

[octoteam.tahiti.server.pipeline.HeartbeatHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/server/pipeline/HeartbeatHandler.html)

类别：推送生成

作用：在管道中加入该模块后可以产生心跳推送

该模块会在收到 IdleStateEvent 时发送 HEARTBEAT_PUSH 上行消息。需要配合 IdleStateHandler 所提供的 IdleStateEvent 来实现心跳功能。

构造函数签名：

```java
HeartbeatHandler()
```

### PingRequestHandler

[octoteam.tahiti.server.pipeline.PingRequestHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/server/pipeline/PingRequestHandler.html)

类别：请求处理

作用：在管道中加入该模块后可以响应 Ping 请求

该模块处理下行的 Ping 请求 (Request)，并回复 Pong 响应 (Response)。

构造函数签名：

```java
PingRequestHandler()
```

### RequestRateLimitHandler

[octoteam.tahiti.server.pipeline.RequestRateLimitHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/server/pipeline/RequestRateLimitHandler.html)

类别：消息过滤

作用：在管道中加入该模块后可以限制特定类别请求的流量

该模块可以限制管道中的下行消息。若下行消息超过了限制，会产生 RateLimitExceededEvent 事件，并产生状态为 LIMIT_EXCEEDED 的上行消息响应。超出限制的消息会被丢弃。

构造函数签名：

```java
RequestRateLimitHandler(
	ServiceCode serviceCode,
	String name,
	Callable<SimpleRateLimiter> factory
```

参数：

- `serviceCode`: 要限制的消息类别, 只有这个参数指定的消息会被限制

- `name`: 产生的事件中所包含的名称

- `factory`: 一个返回新限流器实例的 Callable 对象，将会使用该限流器进行限流

### UserEventToEventBusHandler

[octoteam.tahiti.shared.netty.pipeline.UserEventToEventBusHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/shared/netty/pipeline/UserEventToEventBusHandler.html)

类别：辅助处理

作用：在管道中加入该模块后可以将管道中的事件提取出来

该模块会将管道中产生的事件全部转发到给定的 EventBus 中。

构造函数签名：

```java
UserEventToEventBusHandler(EventBus eventBus)
```

参数：

- `eventBus`: 接受消息的 EventBus 实例

### HeartbeatPushHandler

[octoteam.tahiti.client.pipeline.HeartbeatPushHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/client/pipeline/HeartbeatPushHandler.html)

类别：推送处理

作用：在管道中加入该模块后会自动响应下行数据中的心跳推送

该模块接收到下行的 HEARTBEAT_PUSH 后，会发送上行的 ACK。

构造函数签名：

```java
HeartbeatPushHandler()
```

### ResponseCallbackHandler

[octoteam.tahiti.client.pipeline.ResponseCallbackHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/client/pipeline/ResponseCallbackHandler.html)

类别：辅助处理

作用：在管道中加入该模块后会将响应和请求匹配起来形成回调

该模块处理服务端对请求的响应。若发送对应请求时指定了回调函数，则会调用该回调函数，实现回调式 Request - Response。

构造函数签名：

```java
ResponseCallbackHandler(CallbackRepository callbackRepository)
```

参数：

- `callbackRepository`: 存储回调函数的 CallbackRepository 实例

## 其他可复用模块

### Store

[octoteam.tahiti.client.ui.Store](https://summerwish.github.io/Tahiti/octoteam/tahiti/client/ui/Store.html)

该模块实现了响应式状态存储。其他模块可以从 Store 中订阅状态项，从而在状态项发生改变的时候获得通知。

- `void update(Runnable r)`: 启动更新过程, 在执行完毕后自动结束更新过程

- `void beginUpdate()`: 启动更新过程

- `void endUpdate()`: 结束更新过程, 并触发更新通知

- `void init(String key, Object value)`: 存储初始状态, 不会触发更新通知

- `void put(String key, Object value)`: 更新状态, 会触发更新通知

- `Object get(String key)`: 获得状态的值

- `void observe(String key, Function<Object, Void> r)`: 为状态添加监听器

### CallbackRepository

[octoteam.tahiti.client.CallbackRepository](https://summerwish.github.io/Tahiti/octoteam/tahiti/client/CallbackRepository.html)

该模块实现了基于有序序列的回调仓库。

- `long getNextSequence()`: 获得下一个序列值

- `long getNextSequence(Function<Message, Void> r)`: 获得下一个序列值, 并存储回调

- `void resolveCallback(long seqId, Message msg)`: 根据序列值调用回调

### CounterBasedRateLimiter

[octoteam.tahiti.server.ratelimiter.CounterBasedRateLimiter](https://summerwish.github.io/Tahiti/octoteam/tahiti/server/ratelimiter/CounterBasedRateLimiter.html)

实现了 SimpleRateLimiter 接口。提供基于计数的限流器接口。获得令牌超过允许次数限制后，就无法继续获得令牌。

- `CounterBasedRateLimiter(int maxLimit)`: 指定限制次数

- `boolean tryAcquire()`: 尝试获取令牌，若失败则返回 `false`

### MessageHandler

[octoteam.tahiti.shared.netty.MessageHandler](https://summerwish.github.io/Tahiti/octoteam/tahiti/shared/netty/MessageHandler.html)

该模块实现了双向消息过滤，派生类可以通过覆盖函数的方式决定是否将一个下行消息向下传递或将一个上行消息向上传递。

- `void messageReceived(ChannelHandlerContext ctx, Message msg)`: 当从外部收到消息时被调用，默认是向消息处理队列中的下一 Handler 传递消息，子类可以通过覆写这个方法实现自定义的消息处理行为

- `void messageSent(ChannelHandlerContext ctx, Message msg, ChannelPromise promise)`: 当向外部发送消息时被调用，默认是向消息处理队列中的下一 Handler 传递消息，子类可以通过覆写这个方法实现自定义的消息处理行为

### StatisticsLogger

[octoteam.tahiti.shared.logging.StatisticsLogger](https://summerwish.github.io/Tahiti/octoteam/tahiti/shared/logging/StatisticsLogger.html)

该模块实现了可统计的日志，该模块定期将统计值写入日志，派生类可调用该模块接口修改统计值。

- `StatisticsLogger(String filePath, int periodSeconds)`: 按给定时间间隔定时写入给定路径的日志

- `void clear(String key)`: 通过输入新键值对,初始化需要记录的事件信息,时间对应初始次数值为0

- `int increase(String key)`: 对相应记录事件的次数值进行加 1