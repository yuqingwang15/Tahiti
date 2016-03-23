package octoteam.tahiti.server.pipeline;

import com.google.common.base.Function;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.server.event.RateLimitExceededEvent;
import octoteam.tahiti.server.ratelimiter.SimpleRateLimiter;

public class RateLimitHandler extends InboundMessageHandler {

    private String name;
    private String sessionKey;
    private Function<Void, SimpleRateLimiter> rateLimiterFactory;

    public RateLimitHandler(TahitiServer server, String name, Function<Void, SimpleRateLimiter> factory) {
        super(server);
        this.name = name;
        this.sessionKey = "ratelimiter_" + name;
        this.rateLimiterFactory = factory;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        SimpleRateLimiter rateLimiter = (SimpleRateLimiter)getSession(ctx).get(sessionKey);
        if (rateLimiter == null) {
            rateLimiter = this.rateLimiterFactory.apply(null);
            getSession(ctx).put(sessionKey, rateLimiter);
        }
        if (rateLimiter.tryAcquire()) {
            ctx.fireChannelRead(msg);
        } else {
            RateLimitExceededEvent ev = new RateLimitExceededEvent(name);
            this.server.getEventBus().post(ev);
            ctx.fireUserEventTriggered(ev);
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
