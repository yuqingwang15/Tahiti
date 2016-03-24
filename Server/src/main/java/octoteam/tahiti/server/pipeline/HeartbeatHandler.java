package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;

@ChannelHandler.Sharable
public class HeartbeatHandler extends InboundMessageHandler {

    public HeartbeatHandler(TahitiServer server) {
        super(server);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        Message.Builder heartbeat = Message
                .newBuilder()
                .setDirection(Message.DirectionCode.EVENT)
                .setService(Message.ServiceCode.HEARTBEAT_EVENT);

        ctx
                .writeAndFlush(heartbeat.build())
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

}
