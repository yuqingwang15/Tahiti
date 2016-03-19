package octoteam.tahiti.server;

import com.google.common.eventbus.EventBus;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.util.AttributeKey;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.channelhandler.AuthHandler;
import octoteam.tahiti.server.channelhandler.FinalHandler;
import octoteam.tahiti.server.channelhandler.PingHandler;
import octoteam.tahiti.server.channelhandler.RawHandler;
import octoteam.tahiti.server.configuration.ChatServiceConfiguration;
import octoteam.tahiti.server.configuration.ServerConfiguration;

public class TahitiServer {

    public final static AttributeKey<Session> ATTR_KEY_SESSION = new AttributeKey<>("session");

    private final EventBus eventBus;

    private ServerConfiguration config;

    public TahitiServer(ServerConfiguration config) {
        this.eventBus = new EventBus();
        this.config = config;
    }

    public void register(Object obj) {
        eventBus.register(obj);
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
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast("decoder", new ProtobufDecoder(Message.getDefaultInstance()));
                            ch.pipeline().addLast("raw", new RawHandler(config, eventBus));
                            ch.pipeline().addLast("ping", new PingHandler(config, eventBus));
                            ch.pipeline().addLast("auth", new AuthHandler(config, eventBus));
                            ch.pipeline().addLast("final", new FinalHandler(config, eventBus));
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
