##Tahiti软件复用文档
###构件介绍
这一构件基于netty架构提供的异步的、事件驱动的网络应用程序和工具开发，实现了多个客户端的实时通信，完整地涵盖了通信工具的基础功能点。


###使用说明
- JDK版本：JDK8.0
- IDE：JetBrains Intellij IDEA
- Protobuf
  * 介绍：protocol buffer 是在客户端和服务器进行数据交互的格式，它独立于语言，独立于平台，可以定义自己的数据结构，然后使用代码生成器生成的代码去读写这个数据结构。
  * 版本：protoc 3.0
  * 安装:
    1. 在.proto文件中定义需要做串行化的数据结构信息。例如
    ```
    message Message{
	    enum DirectionCode {
	    UNKNOWN_DIRECTIONCODE       = 0;
	    REQUEST                     = 1;    // 客户端 -> 服务端(负责生成 seqId)
	    RESPONSE                    = 2;    // 服务端 -> 客户端响应(负责回复 seqId)
	    EVENT                       = 3;    // 服务端 -> 客户端(负责生成 seqId)
	    ACK                         = 4;    // 客户端 -> 服务端响应(负责回复 seqId)
  	    }
    }
    ```
    2. 定义好报文格式（message）之后，调用protobuf的编辑器protoc 将.proto文件编译成特定语言的类
    3. 运用生成的类串行化或者反串行化数据。
- 给maven设置环境变量
  1. Menu->Tool Windows 打开maven界面
  2. tahiti.protocal -> Lifecycles -> compile -> Create[tahiti.protocol] 新建profile
  3. 将环境变量PROTOC_PATH设置成你的protoc的安装路径
- 可以使用octoteam.tahiti.protocl.SocketMessageProtos生成串行化数据。

###接口说明
- client端：
  * HeartbeatHandler:
  	* 客户端响应来自服务端的心跳信息

  * ResponseCallbackHandler:
  	* 根据服务端传来的登陆结果的消息更新UI。

  * LoginResponseHandler:
  	* 提高程序的可扩展性。

  * SendMessageHandler:
	* 提高程序的可扩展性。

- server端：
  * AuthFilterHandler: 
	* 验证服务端收发消息的身份有效性

  * AuthRequestHandler：
	* 处理客户端登陆消息。

  * HeartbeatHandler:
	* 在收到 IdleStateEvent 发送 HEARTBEAT_EVENT，从而在配合 IdleStateHandler 的情况下可以实现心跳功能

  * MessageForwardHandler:
	* 收集所有客户端的链接
	* 如果客户端接受CHAT_SEND_MESSAGE_REQUEST类的消息，则群发给其它所有的客户端。

  * MessageRequestHandler:
	* 对于群发的消息，回复发起群发的客户端成功的消息。

  * RequestRateLimitHandler:
	* 实现了每秒钟不能超过5次，每个登陆的账号不能发超过100条的限制（具体数量可以在配置文件中修改）。

  * SessionExpireHandler
	* 提醒用户在发送超过100条之后session过期

  * UserEventHandler
	* 	把所有的event都传给eventbus，根据event类型来调用不同的订阅者。

