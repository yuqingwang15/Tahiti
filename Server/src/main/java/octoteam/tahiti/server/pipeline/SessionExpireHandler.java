package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos;

@ChannelHandler.Sharable
public class SessionExpireHandler extends InboundMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketMessageProtos.Message msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // TODO

        ctx.fireUserEventTriggered(evt);

    }

}
