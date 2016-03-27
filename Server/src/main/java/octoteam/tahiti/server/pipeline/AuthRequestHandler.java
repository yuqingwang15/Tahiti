package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInRespBody;
import octoteam.tahiti.server.PipelineUtil;
import octoteam.tahiti.server.Session;
import octoteam.tahiti.server.configuration.AccountConfiguration;
import octoteam.tahiti.server.event.LoginAttemptEvent;
import octoteam.tahiti.server.event.MessageEvent;
import octoteam.tahiti.shared.netty.MessageHandler;

import java.util.List;
import java.util.UUID;

@ChannelHandler.Sharable
public class AuthRequestHandler extends MessageHandler {

    // TODO: Replace with database based
    private final List<AccountConfiguration> accounts;

    public AuthRequestHandler(List<AccountConfiguration> accounts) {
        this.accounts = accounts;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {

        if (msg.getDirection() != Message.DirectionCode.REQUEST) {
            ctx.fireChannelRead(msg);
            return;
        }

        Boolean authenticated = PipelineUtil.getSession(ctx) != null;
        ctx.fireUserEventTriggered(new MessageEvent(authenticated, msg));

        // Already authenticated: pass everything to next handler
        if (authenticated) {
            ctx.fireUserEventTriggered(new MessageEvent(true, msg));
            ctx.fireChannelRead(msg);
            return;
        }

        // Only allows USER_SIGN_IN_REQUEST, otherwise, emit NOT_AUTHENTICATED
        if (msg.getService() != Message.ServiceCode.USER_SIGN_IN_REQUEST) {
            Message.Builder resp = Message
                    .newBuilder()
                    .setSeqId(msg.getSeqId())
                    .setDirection(Message.DirectionCode.RESPONSE)
                    .setStatus(Message.StatusCode.NOT_AUTHENTICATED);
            ctx.writeAndFlush(resp.build());
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
                    Session sess = new Session(UUID.randomUUID().toString());
                    sess.put("username", account.getUsername());        // TODO
                    PipelineUtil.setSession(ctx, sess);

                    resp
                            .setStatus(Message.StatusCode.SUCCESS)
                            .setUserSignInResp(UserSignInRespBody
                                    .newBuilder()
                                    .setSessionId(sess.getSessionId())
                                    .setClientId(body.getUsername()) // TODO
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

        // TODO:
        /*
        if (resp.getStatus() == Message.StatusCode.SUCCESS) {
            this.server.getAllConnected().add(ctx.channel());
        }
        */

        ctx.writeAndFlush(resp.build());
    }

}
