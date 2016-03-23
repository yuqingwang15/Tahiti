package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;

public abstract class OutboundMessageHandler extends ChannelOutboundHandlerAdapter {

    protected final TahitiServer server;

    public OutboundMessageHandler(TahitiServer server) {
        this.server = server;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Message) {
            write0(ctx, (Message) msg, promise);
        } else {
            ctx.write(msg, promise);
        }
    }

    protected abstract void write0(ChannelHandlerContext ctx, Message msg, ChannelPromise promise);

}