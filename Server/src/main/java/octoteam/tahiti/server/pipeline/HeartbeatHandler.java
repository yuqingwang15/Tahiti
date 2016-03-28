package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * 每隔30秒接受到IdleStateHandler发来的定时事件，然后现客户端下发心跳消息。
 */
@ChannelHandler.Sharable
public class HeartbeatHandler extends MessageHandler {

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
