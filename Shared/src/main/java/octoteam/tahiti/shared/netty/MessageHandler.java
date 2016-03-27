package octoteam.tahiti.shared.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

public class MessageHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            messageReceived(ctx, (Message) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Message) {
            messageSent(ctx, (Message) msg, promise);
        } else {
            ctx.write(msg, promise);
        }
    }

    protected void messageSent(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

}
