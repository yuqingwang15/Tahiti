package octoteam.tahiti.server;

import com.google.common.eventbus.EventBus;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.Message.DirectionCode;
import octoteam.tahiti.protocol.SocketMessageProtos.Message.ServiceCode;
import octoteam.tahiti.server.configuration.ChatServiceConfiguration;
import octoteam.tahiti.server.configuration.ServerConfiguration;
import octoteam.tahiti.server.pipeline.*;
import octoteam.tahiti.server.ratelimiter.CounterBasedRateLimiter;
import octoteam.tahiti.server.ratelimiter.TimeBasedRateLimiter;

import java.util.concurrent.TimeUnit;

public class TahitiServer {

    private final EventBus eventBus;

    private final ServerConfiguration config;

    private final ChannelGroup allConnected;

    public TahitiServer(EventBus eventBus, ServerConfiguration config) {
        this.eventBus = eventBus;
        this.config = config;
        this.allConnected = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @Deprecated
    public ChannelGroup getAllConnected() {
        return allConnected;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(Message.getDefaultInstance()))
                                    .addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS))
                                    .addLast(new HeartbeatHandler())
                                    .addLast(new PingRequestHandler())
                                    .addLast(new AuthRequestHandler(config.getAccounts()))
                                    .addLast(new AuthFilterHandler())
                                    .addLast(new RequestRateLimitHandler(
                                            ServiceCode.CHAT_SEND_MESSAGE_REQUEST,
                                            "perSecond", (unused) -> new TimeBasedRateLimiter(5.0))
                                    )
                                    .addLast(new RequestRateLimitHandler(
                                            ServiceCode.CHAT_SEND_MESSAGE_REQUEST,
                                            "perSession", (unused) -> new CounterBasedRateLimiter(100))
                                    )
                                    .addLast(new SessionExpireHandler())
                                    .addLast(new MessageRequestHandler())
                                    .addLast(new MessageForwardHandler(TahitiServer.this))
                                    .addLast(new UserEventHandler(eventBus))
                            ;
                        }
                    });

            ChatServiceConfiguration serviceConfig = this.config.getChatService();
            ChannelFuture cf = serverBootstrap.bind(serviceConfig.getBindHost(), serviceConfig.getBindPort()).sync();
            System.out.println(String.format(
                    "Tahiti server listening at %s:%s",
                    serviceConfig.getBindHost(),
                    serviceConfig.getBindPort()
            ));
            cf.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
