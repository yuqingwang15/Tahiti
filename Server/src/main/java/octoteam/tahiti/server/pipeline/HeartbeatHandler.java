package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

@ChannelHandler.Sharable
public class HeartbeatHandler extends InboundMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        Message.Builder heartbeat = Message
                .newBuilder()
                .setDirection(Message.DirectionCode.EVENT)
                .setService(Message.ServiceCode.HEARTBEAT_EVENT);

        ctx
                .writeAndFlush(heartbeat.build())
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        ctx.fireUserEventTriggered(evt);

    }

}
