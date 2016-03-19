package octoteam.tahiti.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

public class OutputResponseHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        System.out.println(msg);
    }

}
