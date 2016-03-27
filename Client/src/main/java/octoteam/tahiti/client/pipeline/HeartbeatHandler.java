package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.TahitiClient;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class HeartbeatHandler extends MessageHandler {

    private TahitiClient client;

    public HeartbeatHandler(TahitiClient client) {
        this.client = client;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        // TODO: handle heartbeat event
        ctx.fireChannelRead(msg);
    }

}
