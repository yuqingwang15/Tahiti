package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInRespBody;
import octoteam.tahiti.server.event.LoginAttemptEvent;
import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.service.AccountNotFoundException;
import octoteam.tahiti.server.service.AccountNotMatchException;
import octoteam.tahiti.server.service.AccountService;
import octoteam.tahiti.server.session.Credential;
import octoteam.tahiti.server.session.PipelineHelper;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class AuthRequestHandler extends MessageHandler {

    private final AccountService accountService;

    public AuthRequestHandler(AccountService accountService) {
        this.accountService = accountService;
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

        try {
            Account account = accountService.getMatchedAccount(body.getUsername(), body.getPassword());
            PipelineHelper.clearSession(ctx);
            PipelineHelper.getSession(ctx).put("credential", new Credential(account));
            resp
                    .setStatus(Message.StatusCode.SUCCESS)
                    .setUserSignInResp(UserSignInRespBody
                            .newBuilder()
                            .setUID(account.getId())
                    );
        } catch (AccountNotMatchException e) {
            resp.setStatus(Message.StatusCode.PASSWORD_INCORRECT);
        } catch (AccountNotFoundException e) {
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
