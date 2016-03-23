package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;

/**
 * jdquanyi
 * Created by nick on 2016/3/23.
 */
public class ForwardHandler extends PipelineMessageHandler{

    public ForwardHandler(TahitiServer server) {
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
