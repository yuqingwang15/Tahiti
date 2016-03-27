package octoteam.tahiti.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class PipelineUtil {

    private final static AttributeKey<Session> ATTR_KEY_SESSION = AttributeKey.valueOf("__session");

    public static Session initSession(Channel channel) {
        Session session = new Session();
        setSession(channel, session);
        return session;
    }

    public static Session getSession(Channel channel) {
        Session session = channel.attr(ATTR_KEY_SESSION).get();
        if (session == null) {
            return initSession(channel);
        } else {
            return session;
        }
    }

    public static Session getSession(ChannelHandlerContext ctx) {
        return getSession(ctx.channel());
    }

    private static void setSession(Channel channel, Session session) {
        channel.attr(ATTR_KEY_SESSION).set(session);
    }

    public static void clearSession(Channel channel) {
        getSession(channel).clear();
    }

    public static void clearSession(ChannelHandlerContext ctx) {
        clearSession(ctx.channel());
    }

}
