package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos;
import octoteam.tahiti.server.TahitiServer;

/**
 * jdquanyi
 * Created by nick on 2016/3/23.
 */
public class ForwardHandler extends PipelineMessageHandler{

    public ForwardHandler(TahitiServer server) {
        super(server);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SocketMessageProtos.Message message) throws Exception {
        this.server.getAllConnected().write(message);
    }
}
