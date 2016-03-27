package octoteam.tahiti.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class PipelineUtil {

    private final static AttributeKey<Session> ATTR_KEY_SESSION = AttributeKey.valueOf("__session");

    public static Session getSession(Channel channel) {
        return channel.attr(ATTR_KEY_SESSION).get();
    }

    public static Session getSession(ChannelHandlerContext ctx) {
        return getSession(ctx.channel());
    }

    public static void setSession(Channel channel, Session session) {
        channel.attr(ATTR_KEY_SESSION).set(session);
    }

    public static void setSession(ChannelHandlerContext ctx, Session session) {
        setSession(ctx.channel(), session);
    }

}
