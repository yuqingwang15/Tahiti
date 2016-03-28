package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;
import octoteam.tahiti.shared.protocol.ProtocolUtil;

@ChannelHandler.Sharable
public class PingRequestHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() != Message.ServiceCode.PING_REQUEST) {
            ctx.fireChannelRead(msg);
            return;
        }

        Message.Builder resp = ProtocolUtil
                .buildResponse(msg)
                .setStatus(Message.StatusCode.SUCCESS)
                .setPingPong(msg.getPingPong());

        ctx.writeAndFlush(resp.build());
        ctx.fireChannelRead(msg);
    }

}
