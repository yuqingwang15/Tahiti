package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.PipelineUtil;

@ChannelHandler.Sharable
public class AuthFilterHandler extends OutboundMessageHandler {

    @Override
    protected void write0(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) {
        if (msg.getDirection() != Message.DirectionCode.EVENT) {
            ctx.write(msg, promise);
            return;
        }

        Boolean authenticated = PipelineUtil.getSession(ctx) != null;
        if (authenticated) {
            ctx.write(msg, promise);
        } else {
            promise.setSuccess();
        }
    }
}
