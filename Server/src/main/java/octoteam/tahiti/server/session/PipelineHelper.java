package octoteam.tahiti.server.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 提供对 Channel 中 SessionContainer 的操作
 */
public class PipelineHelper {

    private final static AttributeKey<SessionContainer> ATTR_KEY_SESSION = AttributeKey.valueOf("__session");

    /**
     * 为指定 Channel 设置 SessionContainer
     *
     * @param channel 需要设置 SessionContainer 的 Channel （Channel）
     * @return 为 channel 设置的 SessionContainer
     */
    public static SessionContainer initSession(Channel channel) {
        SessionContainer sessionContainer = new SessionContainer();
        setSession(channel, sessionContainer);
        return sessionContainer;
    }

    /**
     * 返回指定的 Channel 的 SessionContainer
     *
     * @param channel 与返回的 SeesionContainer 所绑定的 Channel （Channel）
     * @return 该 Channel 的 SessionContainer
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
     * 清空指定的 Channel 的 SessionContainer 内的所有数据
     *
     * @param channel 需要清空 SessionContainer 内数据的 channel （Channel）
     */
    public static void clearSession(Channel channel) {
        getSession(channel).clear();
    }

    public static void clearSession(ChannelHandlerContext ctx) {
        clearSession(ctx.channel());
    }

}
