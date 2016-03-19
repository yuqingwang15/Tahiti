package octoteam.tahiti.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import jline.console.ConsoleReader;
import octoteam.tahiti.client.handler.OutputResponseHandler;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.PingPongBody;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.atomic.AtomicLong;

public class Console {

    private static AtomicLong msgSequence = new AtomicLong();

    public static void main(String[] args) throws Exception {

        // TODO: extract methods

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast("decoder", new ProtobufDecoder(Message.getDefaultInstance()));
                            ch.pipeline().addLast("output", new OutputResponseHandler());
                        }
                    });

            Channel ch = bootstrap.connect("127.0.0.1", 6666).sync().channel();

            ConsoleReader reader = new ConsoleReader();
            String line = null;
            do {
                line = reader.readLine("Tahiti>");
                if (line != null) {
                    if (line.equals("login")) {
                        String username = reader.readLine("Username: ");
                        String password = reader.readLine("Password: ", '*');
                        Message.Builder req = Message
                                .newBuilder()
                                .setSeqId(msgSequence.getAndIncrement())
                                .setDirection(Message.DirectionCode.REQUEST)
                                .setService(Message.ServiceCode.USER_SIGN_IN_REQUEST)
                                .setUserSignInReq(UserSignInReqBody.newBuilder()
                                        .setUsername(username)
                                        .setPassword(password)
                                );
                        ch.writeAndFlush(req.build());
                    } else if (line.equals("ping")) {
                        String payload = reader.readLine("Payload: ");
                        Message.Builder req = Message
                                .newBuilder()
                                .setSeqId(msgSequence.getAndIncrement())
                                .setDirection(Message.DirectionCode.REQUEST)
                                .setService(Message.ServiceCode.PING_REQUEST)
                                .setPingPong(PingPongBody.newBuilder()
                                        .setPayload(payload)
                                );
                        ch.writeAndFlush(req.build());
                    } else if (line.equals("send")) {
                        throw new NotImplementedException();
                    }
                }
            }
            while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
