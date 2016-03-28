package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.RateLimitExceededEvent;
import octoteam.tahiti.server.ratelimiter.SimpleRateLimiter;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;
import octoteam.tahiti.shared.protocol.ProtocolUtil;

import java.util.concurrent.Callable;


/**构造函数：根据不同的传入参数构造相应的方法：
 * 若传入perSecond,则基于时间，
 * 若传入perSession,则基于session
 *
 */

@ChannelHandler.Sharable
public class RequestRateLimitHandler extends MessageHandler {

    private final Message.ServiceCode serviceCode;
    private final String name;
    private final String sessionKey;
    private final Callable<SimpleRateLimiter> rateLimiterFactory;

    public RequestRateLimitHandler(
            Message.ServiceCode serviceCode,
            String name,
            Callable<SimpleRateLimiter> factory
    ) {
        this.serviceCode = serviceCode;
        this.name = name;
        this.sessionKey = "ratelimiter_" + name;
        this.rateLimiterFactory = factory;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (msg.getDirection() != Message.DirectionCode.REQUEST || msg.getService() != serviceCode) {
            ctx.fireChannelRead(msg);
            return;
        }
        SimpleRateLimiter rateLimiter = (SimpleRateLimiter) PipelineHelper.getSession(ctx).get(sessionKey);
        if (rateLimiter == null) {
            rateLimiter = this.rateLimiterFactory.call();
            PipelineHelper.getSession(ctx).put(sessionKey, rateLimiter);
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
        Message.Builder resp = ProtocolUtil
                .buildResponse(req)
                .setStatus(Message.StatusCode.LIMIT_EXCEEDED);
        return resp.build();
    }

}
