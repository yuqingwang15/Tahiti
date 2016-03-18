package octoteam.tahiti.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.protocol.SocketMessageProtos;

public class PingHandler extends SimpleChannelInboundHandler<SocketMessageProtos.Message> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketMessageProtos.Message msg) {
        if (msg.getType() == SocketMessageProtos.Message.MessageType.PING) {
            SocketMessageProtos.Message resp = SocketMessageProtos.Message
                    .newBuilder()
                    .setType(SocketMessageProtos.Message.MessageType.PONG)
                    .setPingPong(msg.getPingPong())
                    .build();
            ctx.writeAndFlush(resp);
        }
    }

}
