package octoteam.tahiti.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.protocol.SocketMessageProtos;

public class PongHandler extends SimpleChannelInboundHandler<SocketMessageProtos.Message> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketMessageProtos.Message msg) {
        if (msg.getType() == SocketMessageProtos.Message.MessageType.PONG) {
            System.out.println(String.format(
                    "Received pong: %s",
                    msg.getPingPong().getPayload()
            ));
        }
    }

}
