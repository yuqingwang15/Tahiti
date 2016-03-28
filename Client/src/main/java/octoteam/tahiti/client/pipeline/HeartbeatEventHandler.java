package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;
import octoteam.tahiti.shared.protocol.ProtocolUtil;

/**
 * 该模块处理下行消息中的心跳事件 (HEARTBEAT_EVENT),
 * 在收到心跳事件时发送上行消息 HEARTBEAT_EVENT ACK.
 */
@ChannelHandler.Sharable
public class HeartbeatEventHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() == Message.ServiceCode.HEARTBEAT_EVENT) {
            ctx.writeAndFlush(ProtocolUtil.buildAck(msg));
        }
        ctx.fireChannelRead(msg);
    }

}
