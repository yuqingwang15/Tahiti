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
  客户端每隔30秒将接受到来自服务端的心跳信息。

  * ResponseCallbackHandler
  根据服务端传来的登陆结果的消息更新UI。

  * LoginResponseHandler
  提高程序的可扩展性。

  * SendMessageHandler
提高程序的可扩展性。

- server端：
  1. AuthFilterHandler: 
作用：验证服务端收发消息的身份有效性

  2. AuthRequestHandler：
作用：处理客户端登陆消息。

  3. HeartbeatHandler
作用：每隔30秒，向客户端发送心跳。

  4. MessageForwardHandler
作用：收集所有客户端的链接。
	2》如果客户端接受CHAT_SEND_MESSAGE_REQUEST类的消息，则群发给其它所有的客户端。

>MessageRequestHandler
作用：对于群发的消息，回复发起群发的客户端成功的消息。
方法：判断消息类型，如果属于CHAT_SEND_MESSAGE_REQUEST类型则回复成功。

>RequestRateLimitHandler
作用：实现了每秒钟不能超过5次，每个登陆的账号不能发超过100条的限制（具体数量可以在配置文件中修改）。
方法：构造函数：根据不同的传入参数构造相应的方法：（perSecond）一种基于时间，（perSession）一种基于session

>SessionExpireHandler
作用：提醒用户在发送超过100条之后session过期
方法：传入一个RateLimitExceededEvent类型的event，如果event.getName()为NAME_PER_SESSION，给客户端发送session过期的消息。

>UserEventHandler
作用：把所有的event都传给eventbus，根据event类型来调用不同的订阅者。
方法：该Handler处于所有pipeline的末端，将pipeline所有的event post到eventbus中。

