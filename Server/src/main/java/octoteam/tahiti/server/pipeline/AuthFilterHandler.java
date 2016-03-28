package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.MessageEvent;
import octoteam.tahiti.server.session.Credential;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;
import octoteam.tahiti.shared.protocol.ProtocolUtil;

/**
 * 该模块可以在未登录情况下过滤管道中的上行 EVENT 消息和下行 REQUEST 消息.
 * 该模块会对每一个收到的下行 REQUEST 消息产生 MessageEvent 事件, 事件中标记了该消息收到时是否已经登录.
 * 对于被过滤的下行消息, 该模块还会发送状态为 NOT_AUTHENTICATED 的上行消息.
 *
 * 被过滤的消息会被丢弃.
 */
@ChannelHandler.Sharable
public class AuthFilterHandler extends MessageHandler {

    /**
     * TODO
     *
     * @param ctx
     * @return
     */
    private boolean isContextAuthenticated(ChannelHandlerContext ctx) {
        Credential c = (Credential) PipelineHelper.getSession(ctx).get("credential");
        return (c != null && c.isAuthenticated());
    }


    /**
     *服务器有两种回应方式:
     * 被动回应直接通过；
     * 主动回应需要判断客户端用户身份的有效性，如果该用户已经登陆，则下发信息;如果该用户处于未登陆状态,则不处理该消息.					，则不处理该消息。
     * @param ctx
     * @param msg
     * @param promise
     */
    @Override
    protected void messageSent(ChannelHandlerContext ctx, Message msg, ChannelPromise promise) {
        if (msg.getDirection() != Message.DirectionCode.EVENT) {
            ctx.write(msg, promise);
            return;
        }

        Boolean authenticated = isContextAuthenticated(ctx);
        if (authenticated) {
            ctx.write(msg, promise);
        } else {
            promise.setSuccess();
        }
    }

    /**
     * 如果不属于request类消息则直接下发到下一个handler，如果属于request则先验证合法性。
     * @param ctx
     * @param msg
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getDirection() != Message.DirectionCode.REQUEST) {
            // Not a request: pass to next handler
            ctx.fireChannelRead(msg);
            return;
        }

        Boolean authenticated = isContextAuthenticated(ctx);
        ctx.fireUserEventTriggered(new MessageEvent(authenticated, msg));

        if (authenticated) {
            // Already authenticated: pass everything to next handler
            ctx.fireChannelRead(msg);
        } else {
            // Not authenticated: response NOT_AUTHENTICATED
            Message.Builder resp = ProtocolUtil
                    .buildResponse(msg)
                    .setStatus(Message.StatusCode.NOT_AUTHENTICATED);
            ctx.writeAndFlush(resp.build());
        }
    }

}
