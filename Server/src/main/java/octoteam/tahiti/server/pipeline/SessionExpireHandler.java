package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class SessionExpireHandler extends MessageHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // TODO

        ctx.fireUserEventTriggered(evt);

    }

}
