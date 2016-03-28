package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * 触发对应的Channel读取Message。
 */
@ChannelHandler.Sharable
public class HeartbeatEventHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        // TODO: handle heartbeat event
        ctx.fireChannelRead(msg);
    }

}
