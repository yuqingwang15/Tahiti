package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;

public class AuthFilterHandler extends OutboundMessageHandler {

    public AuthFilterHandler(TahitiServer server) {
        super(server);
    }

    @Override
    protected void write0(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) {
        if (msg.getDirection() != Message.DirectionCode.EVENT) {
            ctx.write(msg, promise);
            return;
        }

        Boolean authenticated = ctx.channel().attr(TahitiServer.ATTR_KEY_SESSION).get() != null;
        if (authenticated || msg.getService() == Message.ServiceCode.HEARTBEAT_EVENT) {
            ctx.write(msg, promise);
        }
    }
}
