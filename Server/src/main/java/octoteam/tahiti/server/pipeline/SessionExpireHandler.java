package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
<<<<<<< e7c0f36d32ca1a46bc5d5d2f9eeba1bdfe7989ad
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class SessionExpireHandler extends MessageHandler {
=======
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.server.event.RateLimitExceededEvent;

@ChannelHandler.Sharable
public class SessionExpireHandler extends InboundMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        ctx.fireChannelRead(msg);
    }
>>>>>>> handle RateLimitExceededEvent

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof RateLimitExceededEvent) {
            RateLimitExceededEvent event = (RateLimitExceededEvent)evt;
            if (event.getName().equals("perSession")) {
                Message.Builder resp = Message.newBuilder()
                        .setDirection(Message.DirectionCode.RESPONSE)
                        .setService(Message.ServiceCode.SESSION_EXPIRED_EVENT);
                ctx.channel().writeAndFlush(resp.build());
                setSession(ctx, null);
            } else {
                Message.Builder resp = Message.newBuilder()
                        .setDirection(Message.DirectionCode.RESPONSE)
                        .setStatus(Message.StatusCode.LIMIT_EXCEEDED);
                ctx.channel().writeAndFlush(resp.build());
            }
        }
        ctx.fireUserEventTriggered(evt);

    }

}
