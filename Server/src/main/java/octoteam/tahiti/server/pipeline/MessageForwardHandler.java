package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import octoteam.tahiti.protocol.SocketMessageProtos.ChatBroadcastPushBody;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.MessageForwardEvent;
import octoteam.tahiti.server.session.Credential;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * messageReceived负责群发消息，channelActive收集所有的客户端链接。
 */
@ChannelHandler.Sharable
public class MessageForwardHandler extends MessageHandler {

    /**
     * 收集所有与服务端成功建立起连接的客户端
     */
    private final static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() == Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST) {
            Credential currentCredential = (Credential) PipelineHelper.getSession(ctx).get("credential");
            if (currentCredential == null) {
                currentCredential = Credential.getGuestCredential();
            }
            Message.Builder resp = Message
                    .newBuilder()
                    .setDirection(Message.DirectionCode.PUSH)
                    .setService(Message.ServiceCode.CHAT_BROADCAST_PUSH)
                    .setChatBroadcastPush(ChatBroadcastPushBody.newBuilder()
                            .setPayload(msg.getChatSendMessageReq().getPayload())
                            .setTimestamp(msg.getChatSendMessageReq().getTimestamp())
                            .setSenderUID(currentCredential.getUID())
                            .setSenderUsername(currentCredential.getUsername())
                    );
            clients.writeAndFlush(resp.build(), channel -> channel != ctx.channel());
            ctx.fireUserEventTriggered(new MessageForwardEvent(msg));
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        ctx.fireChannelActive();
    }

}
