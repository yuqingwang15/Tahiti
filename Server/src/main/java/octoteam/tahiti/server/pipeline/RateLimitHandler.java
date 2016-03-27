package octoteam.tahiti.server.pipeline;

import com.google.common.base.Function;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.PipelineUtil;
import octoteam.tahiti.server.event.RateLimitExceededEvent;
import octoteam.tahiti.server.ratelimiter.SimpleRateLimiter;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class RateLimitHandler extends MessageHandler {

    private final String name;
    private final String sessionKey;
    private final Function<Void, SimpleRateLimiter> rateLimiterFactory;

    public RateLimitHandler(String name, Function<Void, SimpleRateLimiter> factory) {
        this.name = name;
        this.sessionKey = "ratelimiter_" + name;
        this.rateLimiterFactory = factory;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        SimpleRateLimiter rateLimiter = (SimpleRateLimiter) PipelineUtil.getSession(ctx).get(sessionKey);
        if (rateLimiter == null) {
            rateLimiter = this.rateLimiterFactory.apply(null);
            PipelineUtil.getSession(ctx).put(sessionKey, rateLimiter);
        }
        if (rateLimiter.tryAcquire()) {
            ctx.fireChannelRead(msg);
        } else {
            RateLimitExceededEvent evt = new RateLimitExceededEvent(name);
            ctx.fireUserEventTriggered(evt);
            ctx.writeAndFlush(buildExceededMsg(msg));
        }
    }

    private Message buildExceededMsg(Message req) {
        Message.Builder resp = Message
                .newBuilder()
                .setSeqId(req.getSeqId())
                .setDirection(Message.DirectionCode.RESPONSE)
                .setStatus(Message.StatusCode.LIMIT_EXCEEDED);
        return resp.build();
    }

}
