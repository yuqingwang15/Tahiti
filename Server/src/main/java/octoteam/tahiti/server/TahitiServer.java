package octoteam.tahiti.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.util.AttributeKey;
import octoteam.tahiti.protocol.SocketMessageProtos;
import octoteam.tahiti.server.configuration.ChatServiceConfiguration;
import octoteam.tahiti.server.configuration.ServerConfiguration;
import octoteam.tahiti.server.handler.AuthHandler;
import octoteam.tahiti.server.handler.DiscardServerHandler;
import octoteam.tahiti.server.handler.PingHandler;

public class TahitiServer {

    public final static AttributeKey<Session> ATTR_KEY_SESSION = new AttributeKey<>("session");

    private ServerConfiguration config;

    public TahitiServer(ServerConfiguration config) {
        this.config = config;
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
                            ch.pipeline().addLast("decoder", new ProtobufDecoder(SocketMessageProtos.Message.getDefaultInstance()));
                            ch.pipeline().addLast("ping", new PingHandler());
                            ch.pipeline().addLast("auth", new AuthHandler(config));
                            ch.pipeline().addLast("discard", new DiscardServerHandler());
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
