package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import octoteam.tahiti.protocol.SocketMessageProtos.ChatBroadcastEventBody;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.MessageForwardEvent;
import octoteam.tahiti.server.session.Credential;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;


@ChannelHandler.Sharable
public class MessageForwardHandler extends MessageHandler {

    private final static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService().equals(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)) {
            Credential currentCredential = (Credential) PipelineHelper.getSession(ctx).get("credential");
            if (currentCredential == null) {
                currentCredential = Credential.getGuestCredential();
            }
            Message.Builder resp = Message
                    .newBuilder()
                    .setSeqId(msg.getSeqId())
                    .setDirection(Message.DirectionCode.EVENT)
                    .setService(Message.ServiceCode.CHAT_BROADCAST_EVENT)
                    .setChatBroadcastEvent(ChatBroadcastEventBody.newBuilder()
                            .setPayload(msg.getChatMessageReq().getPayload())
                            .setTimestamp(msg.getChatMessageReq().getTimestamp())
                            .setSenderUID(currentCredential.getUID())
                            .setSenderUsername(currentCredential.getUsername())
                    );
            clients.writeAndFlush(resp.build(), channel -> channel != ctx.channel());
            ctx.fireUserEventTriggered(new MessageForwardEvent(msg));
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
