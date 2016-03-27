package octoteam.tahiti.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class PipelineUtil {

    public static Session getSession(Channel channel) {
        return channel.attr(TahitiServer.ATTR_KEY_SESSION).get();
    }

    public static Session getSession(ChannelHandlerContext ctx) {
        return getSession(ctx.channel());
    }

    public static void setSession(Channel channel, Session session) {
        channel.attr(TahitiServer.ATTR_KEY_SESSION).set(session);
    }

    public static void setSession(ChannelHandlerContext ctx, Session session) {
        setSession(ctx.channel(), session);
    }

}
