package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

abstract class InboundMessageHandler
        extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            channelRead0(ctx, (Message) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    protected abstract void channelRead0(ChannelHandlerContext ctx, Message msg);

}
