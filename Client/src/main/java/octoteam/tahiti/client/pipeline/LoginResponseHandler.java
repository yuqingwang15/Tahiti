package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.event.LoginAttemptEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class LoginResponseHandler extends MessageHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() == Message.ServiceCode.USER_SIGN_IN_REQUEST) {
            ctx.fireUserEventTriggered(new LoginAttemptEvent(msg.getStatus() == Message.StatusCode.SUCCESS));
        }
        ctx.fireChannelRead(msg);
    }

}
