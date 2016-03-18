package octoteam.tahiti.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import octoteam.tahiti.client.handler.PongHandler;
import octoteam.tahiti.protocol.SocketMessageProtos;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Console {

    public static void main(String[] args) throws Exception {

        // TODO: use jline

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast("encoder", new ProtobufEncoder());
                    ch.pipeline().addLast("decoder", new ProtobufDecoder(SocketMessageProtos.Message.getDefaultInstance()));
                    ch.pipeline().addLast("ping", new PongHandler());
                }
            });

            Channel ch = bootstrap.connect("127.0.0.1", 6666).sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (line == null || "".equals(line)) {
                    continue;
                }
                SocketMessageProtos.Message pingMsg = SocketMessageProtos.Message
                        .newBuilder()
                        .setType(SocketMessageProtos.Message.MessageType.PING)
                        .setPingPong(
                                SocketMessageProtos.PingPongBody
                                        .newBuilder()
                                        .setPayload(line)
                        )
                        .build();
                ch.writeAndFlush(pingMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
