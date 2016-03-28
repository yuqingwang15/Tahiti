package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.client.event.SendMessageEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class SendMessageFilterHandler extends MessageHandler {

    @Override
    protected void messageSent(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) throws Exception {
        if (msg.getService() == Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST) {
            ctx.fireUserEventTriggered(new SendMessageEvent());
        }
        super.messageSent(ctx, msg, promise);
    }

}
