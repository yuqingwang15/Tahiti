package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.server.Session;
import octoteam.tahiti.server.TahitiServer;

interface SessionHandlerTrait {

    default Session getSession(ChannelHandlerContext ctx) {
        return ctx.channel().attr(TahitiServer.ATTR_KEY_SESSION).get();
    }

    default void setSession(ChannelHandlerContext ctx, Session session) {
        ctx.channel().attr(TahitiServer.ATTR_KEY_SESSION).set(session);
    }

}
