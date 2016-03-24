package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;

@ChannelHandler.Sharable
public class MessageForwardHandler extends InboundMessageHandler {

    public MessageForwardHandler(TahitiServer server) {
        super(server);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Message.Builder resp = Message
                .newBuilder()
                .setSeqId(message.getSeqId())
                .setDirection(Message.DirectionCode.EVENT)
                .setService(Message.ServiceCode.CHAT_BROADCAST_EVENT)
                .setChatMessageReq(message.getChatMessageReq());
        this.server.getAllConnected().flushAndWrite(resp.build());
    }
}
