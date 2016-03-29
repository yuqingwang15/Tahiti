package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.event.ReceiveMessageEvent;
import octoteam.tahiti.protocol.SocketMessageProtos;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * 客户端接受服务转发的消息, 触发一个ReceiveMessageEvent.
 */
@ChannelHandler.Sharable
public class ReceiveMessageHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, SocketMessageProtos.Message msg) throws Exception {
        if (msg.getService().equals(SocketMessageProtos.Message.ServiceCode.CHAT_BROADCAST_EVENT)) {
            ctx.fireUserEventTriggered(new ReceiveMessageEvent());
        }
        super.messageReceived(ctx, msg);
    }

}
