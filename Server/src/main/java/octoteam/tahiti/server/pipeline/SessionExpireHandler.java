package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.RateLimitExceededEvent;
import octoteam.tahiti.shared.netty.MessageHandler;

import static octoteam.tahiti.server.PipelineUtil.setSession;

@ChannelHandler.Sharable
public class SessionExpireHandler extends MessageHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof RateLimitExceededEvent) {
            RateLimitExceededEvent event = (RateLimitExceededEvent)evt;
            if (event.getName().equals(RateLimitExceededEvent.NAME_PER_SESSION)) {
                Message.Builder resp = Message.newBuilder()
                        .setDirection(Message.DirectionCode.RESPONSE)
                        .setService(Message.ServiceCode.SESSION_EXPIRED_EVENT);
                ctx.channel().writeAndFlush(resp.build());
                setSession(ctx, null);
            }
        }
        ctx.fireUserEventTriggered(evt);

    }

}
