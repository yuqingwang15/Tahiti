package octoteam.tahiti.server.channelhandler;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.configuration.ServerConfiguration;

public class HeartbeatHandler extends SimpleChannelInboundHandler<Message> {

    private ServerConfiguration config;
    private EventBus eventBus;

    public HeartbeatHandler(ServerConfiguration config, EventBus eventBus) {
        this.config = config;
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        // eat HEARTBEAT ack
        if (msg.getService() != Message.ServiceCode.HEARTBEAT_EVENT) {
            ctx.fireChannelRead(msg);
        }
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
