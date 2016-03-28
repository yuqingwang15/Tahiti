package octoteam.tahiti.server.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * TODO
 */
public class PipelineHelper {

    private final static AttributeKey<SessionContainer> ATTR_KEY_SESSION = AttributeKey.valueOf("__session");

    /**
     * TODO
     *
     * @param channel
     * @return
     */
    public static SessionContainer initSession(Channel channel) {
        SessionContainer sessionContainer = new SessionContainer();
        setSession(channel, sessionContainer);
        return sessionContainer;
    }

    /**
     * TODO
     *
     * @param channel
     * @return
     */
    public static SessionContainer getSession(Channel channel) {
        SessionContainer sessionContainer = channel.attr(ATTR_KEY_SESSION).get();
        if (sessionContainer == null) {
            return initSession(channel);
        } else {
            return sessionContainer;
        }
    }

    public static SessionContainer getSession(ChannelHandlerContext ctx) {
        return getSession(ctx.channel());
    }

    private static void setSession(Channel channel, SessionContainer sessionContainer) {
        channel.attr(ATTR_KEY_SESSION).set(sessionContainer);
    }

    /**
     * TODO
     *
     * @param channel
     */
    public static void clearSession(Channel channel) {
        getSession(channel).clear();
    }

    public static void clearSession(ChannelHandlerContext ctx) {
        clearSession(ctx.channel());
    }

}
