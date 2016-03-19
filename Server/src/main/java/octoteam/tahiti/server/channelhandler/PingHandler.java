package octoteam.tahiti.server.channelhandler;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.configuration.ServerConfiguration;

public class PingHandler extends SimpleChannelInboundHandler<Message> {

    private ServerConfiguration config;
    private EventBus eventBus;

    public PingHandler(ServerConfiguration config, EventBus eventBus) {
        this.config = config;
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() != Message.ServiceCode.PING_REQUEST) {
            ctx.fireChannelRead(msg);
            return;
        }

        Message.Builder resp = Message
                .newBuilder()
                .setSeqId(msg.getSeqId())
                .setDirection(Message.DirectionCode.RESPONSE)
                .setStatus(Message.StatusCode.SUCCESS)
                .setPingPong(msg.getPingPong());

        ctx.writeAndFlush(resp.build());
    }

}
