package octoteam.tahiti.shared.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

/**
 * TODO
 */
public abstract class MessageHandler extends ChannelDuplexHandler {

    @Override
    final public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            messageReceived(ctx, (Message) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * TODO
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    @Override
    final public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Message) {
            messageSent(ctx, (Message) msg, promise);
        } else {
            ctx.write(msg, promise);
        }
    }

    /**
     * TODO
     *
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    protected void messageSent(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

}
