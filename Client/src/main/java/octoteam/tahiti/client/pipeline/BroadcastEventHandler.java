package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.event.ChatMessageEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * 该模块处理下行消息中的聊天消息 (CHAT_BROADCAST_EVENT).
 * 收到聊天消息时, 会产生 ChatMessageEvent 事件
 */
@ChannelHandler.Sharable
public class BroadcastEventHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() == Message.ServiceCode.CHAT_BROADCAST_EVENT) {
            ctx.fireUserEventTriggered(new ChatMessageEvent(
                    msg.getChatBroadcastEvent().getPayload(),
                    msg.getChatBroadcastEvent().getSenderUsername(),
                    msg.getChatBroadcastEvent().getTimestamp()
            ));
        }
        ctx.fireChannelRead(msg);
    }

}
