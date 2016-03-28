package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;
import octoteam.tahiti.shared.protocol.ProtocolUtil;

/**判断消息类型，如果属于CHAT_SEND_MESSAGE_REQUEST类型则回复成功。
 *
 */
@ChannelHandler.Sharable
public class MessageRequestHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() != Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST) {
            ctx.fireChannelRead(msg);
            return;
        }

        Message.Builder resp = ProtocolUtil
                .buildResponse(msg)
                .setStatus(Message.StatusCode.SUCCESS);

        ctx.writeAndFlush(resp.build());
        ctx.fireChannelRead(msg);
    }

}
