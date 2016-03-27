package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInRespBody;
import octoteam.tahiti.server.configuration.AccountConfiguration;
import octoteam.tahiti.server.event.LoginAttemptEvent;
import octoteam.tahiti.server.session.Credential;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;

import java.util.List;

@ChannelHandler.Sharable
public class AuthRequestHandler extends MessageHandler {

    // TODO: Replace with database based
    private final List<AccountConfiguration> accounts;

    public AuthRequestHandler(List<AccountConfiguration> accounts) {
        this.accounts = accounts;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        if (msg.getService() != Message.ServiceCode.USER_SIGN_IN_REQUEST) {
            ctx.fireChannelRead(msg);
            return;
        }

        UserSignInReqBody body = msg.getUserSignInReq();
        Message.Builder resp = Message
                .newBuilder()
                .setSeqId(msg.getSeqId())
                .setDirection(Message.DirectionCode.RESPONSE);

        Boolean found = false;
        for (AccountConfiguration account : accounts) {
            if (account.getUsername().equals(body.getUsername())) {
                if (account.getPassword().equals(body.getPassword())) {
                    // correct username, correct password
                    PipelineHelper.clearSession(ctx);
                    PipelineHelper.getSession(ctx).put("credential", new Credential(0, account.getUsername(), true));
                    resp
                            .setStatus(Message.StatusCode.SUCCESS)
                            .setUserSignInResp(UserSignInRespBody
                                    .newBuilder()
                                    .setUID(0) // TODO
                            );
                } else {
                    // correct username, incorrect password
                    resp.setStatus(Message.StatusCode.PASSWORD_INCORRECT);
                }
                found = true;
                break;
            }
        }

        if (!found) {
            resp.setStatus(Message.StatusCode.USERNAME_NOT_FOUND);
        }

        ctx.fireUserEventTriggered(new LoginAttemptEvent(
                resp.getStatus() == Message.StatusCode.SUCCESS,
                body.getUsername()
        ));

        ctx.writeAndFlush(resp.build());
        ctx.fireChannelRead(msg);
    }

}
