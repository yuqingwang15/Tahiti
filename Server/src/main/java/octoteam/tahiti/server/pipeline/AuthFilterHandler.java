package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.MessageEvent;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class AuthFilterHandler extends MessageHandler {

    private boolean isContextAuthenticated(ChannelHandlerContext ctx) {
        return PipelineHelper.getSession(ctx).containsKey("credential");
    }

    @Override
    protected void messageSent(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) {
        if (msg.getDirection() != Message.DirectionCode.EVENT) {
            ctx.write(msg, promise);
            return;
        }

        Boolean authenticated = isContextAuthenticated(ctx);
        if (authenticated) {
            ctx.write(msg, promise);
        } else {
            promise.setSuccess();
        }
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getDirection() != Message.DirectionCode.REQUEST) {
            // Not a request: pass to next handler
            ctx.fireChannelRead(msg);
            return;
        }

        Boolean authenticated = isContextAuthenticated(ctx);
        ctx.fireUserEventTriggered(new MessageEvent(authenticated, msg));

        if (authenticated) {
            // Already authenticated: pass everything to next handler
            ctx.fireChannelRead(msg);
        } else {
            // Not authenticated: response NOT_AUTHENTICATED
            Message.Builder resp = Message
                    .newBuilder()
                    .setSeqId(msg.getSeqId())
                    .setDirection(Message.DirectionCode.RESPONSE)
                    .setStatus(Message.StatusCode.NOT_AUTHENTICATED);
            ctx.writeAndFlush(resp.build());
        }
    }

}