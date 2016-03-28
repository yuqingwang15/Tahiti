package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.event.SessionExpiredEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * 该模块处理下行消息中的会话过期事件 (SESSION_EXPIRED_EVENT)
 * 收到该消息时, 会产生 SessionExpiredEvent
 */
@ChannelHandler.Sharable
public class SessionExpireEventHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() == Message.ServiceCode.SESSION_EXPIRED_EVENT) {
            ctx.fireUserEventTriggered(new SessionExpiredEvent(msg.getSessionExpiredEvent().getReason()));
        }
        ctx.fireChannelRead(msg);
    }

}
