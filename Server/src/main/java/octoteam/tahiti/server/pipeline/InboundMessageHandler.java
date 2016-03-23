package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;

public abstract class InboundMessageHandler extends ChannelInboundHandlerAdapter {

    protected final TahitiServer server;

    public InboundMessageHandler(TahitiServer server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            channelRead0(ctx, (Message) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    protected abstract void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception;

}
