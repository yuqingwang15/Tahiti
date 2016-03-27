package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class MessageForwardHandler extends MessageHandler {

    private final static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);;

    public MessageForwardHandler() {
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService().equals(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)) {
            Message.Builder resp = Message
                    .newBuilder()
                    .setSeqId(msg.getSeqId())
                    .setDirection(Message.DirectionCode.EVENT)
                    .setService(Message.ServiceCode.CHAT_BROADCAST_EVENT)
                    .setChatMessageReq(msg.getChatMessageReq());
            clients.writeAndFlush(resp.build(), channel -> channel != ctx.channel());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        ctx.fireChannelActive();
    }
}
