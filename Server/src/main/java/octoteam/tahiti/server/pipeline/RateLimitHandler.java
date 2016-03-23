package octoteam.tahiti.server.pipeline;

import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.server.event.TimeBasedRateLimitedExceededEvent;

public class RateLimitHandler extends PipelineMessageHandler {

    public final static AttributeKey<RateLimiter> ATTR_KEY_RATELIMITER = new AttributeKey<>("ratelimiter");
    public final static AttributeKey<Integer> ATTR_KEY_COUNTER = new AttributeKey<>("counter");

    public RateLimitHandler(TahitiServer server) {
        super(server);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {

        RateLimiter rateLimiter = ctx.channel().attr(RateLimitHandler.ATTR_KEY_RATELIMITER).get();
        if (rateLimiter != null) {
            if (rateLimiter.tryAcquire()) {
                int counter = ctx.channel().attr(ATTR_KEY_COUNTER).get();
                if (--counter >= 0) { ctx.fireChannelRead(msg); }
                else { ctx.writeAndFlush(buildExceededMsg(msg.getSeqId()));}
            } else {
                // post event and response msg
                this.server.getEventBus().post(new TimeBasedRateLimitedExceededEvent(ctx.channel()));
                ctx.writeAndFlush(buildExceededMsg(msg.getSeqId()));
            }
        }
    }

    private Message buildExceededMsg(long seqId) {
        Message.Builder resp = Message
                .newBuilder()
                .setSeqId(seqId)
                .setDirection(Message.DirectionCode.RESPONSE)
                .setStatus(Message.StatusCode.LIMIT_EXCEEDED);
        return resp.build();
    }

}
