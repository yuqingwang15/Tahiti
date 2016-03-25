package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

@ChannelHandler.Sharable
public class MessageRequestHandler extends InboundMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() != Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST) {
            ctx.fireChannelRead(msg);
            return;
        }

        Message.Builder resp = Message
                .newBuilder()
                .setSeqId(msg.getSeqId())
                .setDirection(Message.DirectionCode.RESPONSE)
                .setStatus(Message.StatusCode.SUCCESS);

        ctx.writeAndFlush(resp.build());

    }

}
