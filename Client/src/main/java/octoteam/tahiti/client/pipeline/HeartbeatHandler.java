package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.client.TahitiClient;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

public class HeartbeatHandler extends SimpleChannelInboundHandler<Message> {

    private TahitiClient client;

    public HeartbeatHandler(TahitiClient client) {
        this.client = client;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        // TODO: handle heartbeat event
        ctx.fireChannelRead(msg);
    }

}
