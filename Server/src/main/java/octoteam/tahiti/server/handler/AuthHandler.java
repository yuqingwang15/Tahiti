package octoteam.tahiti.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInRespBody;
import octoteam.tahiti.server.Session;
import octoteam.tahiti.server.TahitiServer;
import octoteam.tahiti.server.configuration.AccountConfiguration;
import octoteam.tahiti.server.configuration.ServerConfiguration;

import java.util.List;
import java.util.UUID;

public class AuthHandler extends SimpleChannelInboundHandler<Message> {

    private ServerConfiguration config;

    public AuthHandler(ServerConfiguration config) {
        this.config = config;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        // Already authenticated: pass everything to next handler
        if (ctx.channel().attr(TahitiServer.ATTR_KEY_SESSION).get() != null) {
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

        List<AccountConfiguration> accounts = this.config.getAccounts();
        for (AccountConfiguration account : accounts) {
            if (account.getUsername().equals(body.getUsername())) {
                if (account.getPassword().equals(body.getPassword())) {
                    // correct username, correct password
                    Session sess = new Session();
                    sess.setSessionId(UUID.randomUUID().toString());
                    sess.setUsername(account.getUsername());
                    ctx.channel().attr(TahitiServer.ATTR_KEY_SESSION).set(sess);

                    resp
                            .setStatus(Message.StatusCode.SUCCESS)
                            .setUserSignInResp(UserSignInRespBody
                                    .newBuilder()
                                    .setSessionId(sess.getSessionId())
                                    .setClientId(body.getUsername()) // TODO
                            );
                    ctx.writeAndFlush(resp.build());
                    return;
                } else {
                    // correct username, incorrect password
                    resp.setStatus(Message.StatusCode.PASSWORD_INCORRECT);
                    ctx.writeAndFlush(resp.build());
                    return;
                }
            }
        }

        // username not found
        resp.setStatus(Message.StatusCode.USERNAME_NOT_FOUND);
        ctx.writeAndFlush(resp.build());
    }

}
