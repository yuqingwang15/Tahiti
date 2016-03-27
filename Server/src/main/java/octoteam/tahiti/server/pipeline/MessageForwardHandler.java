package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class MessageForwardHandler extends MessageHandler {

    @Deprecated
    private final
    TahitiServer server;

    public MessageForwardHandler(TahitiServer server) {
        this.server = server;
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
            this.server.getAllConnected().writeAndFlush(resp.build());
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
