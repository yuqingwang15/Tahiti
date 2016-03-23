package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.server.event.RateLimitExceededEvent;

public class SessionExpireHandler extends InboundMessageHandler {

    public SessionExpireHandler(TahitiServer server) {
        super(server);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketMessageProtos.Message msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof RateLimitExceededEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        // TODO
    }

}
