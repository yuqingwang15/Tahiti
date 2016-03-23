package octoteam.tahiti.server.pipeline;

import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.server.event.TimeBasedRateLimitedExceededEvent;

public class RateLimitHandler extends PipelineMessageHandler {

    public final static AttributeKey<RateLimiter> ATTR_KEY_RATELIMITER = new AttributeKey<>("ratelimiter");

    public RateLimitHandler(TahitiServer server) {
        super(server);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {

        RateLimiter rateLimiter = ctx.channel().attr(RateLimitHandler.ATTR_KEY_RATELIMITER).get();
        if (rateLimiter != null) {
            if (rateLimiter.tryAcquire()) {
                ctx.fireChannelRead(msg);
            } else {
                // post event and response msg
                this.server.getEventBus().post(new TimeBasedRateLimitedExceededEvent(ctx.channel()));
                Message.Builder resp = Message
                        .newBuilder()
                        .setSeqId(msg.getSeqId())
                        .setDirection(Message.DirectionCode.RESPONSE)
                        .setStatus(Message.StatusCode.LIMIT_EXCEEDED);
                ctx.writeAndFlush(resp.build());
            }
        }
    }

}
