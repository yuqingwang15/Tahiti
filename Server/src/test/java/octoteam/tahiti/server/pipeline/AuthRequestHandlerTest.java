package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import octoteam.tahiti.server.PipelineUtil;
import octoteam.tahiti.server.Session;
import octoteam.tahiti.server.configuration.AccountConfiguration;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class AuthRequestHandlerTest {

    private static List<AccountConfiguration> accounts;

    @Before
    public void initialize() {
        AccountConfiguration account = new AccountConfiguration();
        account.setUsername("testUser");
        account.setPassword("password123");

        accounts = new LinkedList<>();
        accounts.add(account);
    }

    @Test
    public void testNotAuthenticatedRequest() {

        // AuthRequestHandler should filter requests if user is not authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthRequestHandler(accounts));

        Message chatRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)
                .build();

        channel.writeInbound(chatRequest);
        channel.finish();

        assertNull(channel.readInbound());

        Object response = channel.readOutbound();
        assertTrue(response instanceof Message);

        Message responseMsg = (Message) response;
        assertEquals(123, responseMsg.getSeqId());
        assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
        assertEquals(Message.StatusCode.NOT_AUTHENTICATED, responseMsg.getStatus());

    }

    @Test
    public void testAuthenticatedRequest() {

        // AuthRequestHandler should not filter requests if user is authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthRequestHandler(accounts));
        PipelineUtil.setSession(channel, new Session("foo"));

        Message chatRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)
                .build();

        channel.writeInbound(chatRequest);
        channel.finish();

        assertEquals(chatRequest, channel.readInbound());
        assertNull(channel.readOutbound());

    }

    @Test
    public void testNotAuthenticatedAck() {

        // AuthRequestHandler should not filter ack even if user is not authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthRequestHandler(accounts));

        Message msgAck = Message.newBuilder()
                .setSeqId(321)
                .setDirection(Message.DirectionCode.ACK)
                .build();

        channel.writeInbound(msgAck);
        channel.finish();

        assertEquals(msgAck, channel.readInbound());
        assertNull(channel.readOutbound());

    }

    @Test
    public void testUsernameNotFoundSignInRequest() {

        // AuthRequestHandler should handle login request

        EmbeddedChannel channel = new EmbeddedChannel(new AuthRequestHandler(accounts));

        Message loginRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.USER_SIGN_IN_REQUEST)
                .setUserSignInReq(UserSignInReqBody.newBuilder()
                        .setUsername("notfound")
                        .setPassword("123")
                )
                .build();

        channel.writeInbound(loginRequest);
        channel.finish();

        assertNull(channel.readInbound());

        Object response = channel.readOutbound();
        assertTrue(response instanceof Message);

        Message responseMsg = (Message) response;
        assertEquals(123, responseMsg.getSeqId());
        assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
        assertEquals(Message.StatusCode.USERNAME_NOT_FOUND, responseMsg.getStatus());

    }

    @Test
    public void testWrongPasswordSignInRequest() {

        // AuthRequestHandler should handle login request

        EmbeddedChannel channel = new EmbeddedChannel(new AuthRequestHandler(accounts));

        Message loginRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.USER_SIGN_IN_REQUEST)
                .setUserSignInReq(UserSignInReqBody.newBuilder()
                        .setUsername("testUser")
                        .setPassword("wrongpass")
                )
                .build();

        channel.writeInbound(loginRequest);
        channel.finish();

        assertNull(channel.readInbound());

        Object response = channel.readOutbound();
        assertTrue(response instanceof Message);

        Message responseMsg = (Message) response;
        assertEquals(123, responseMsg.getSeqId());
        assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
        assertEquals(Message.StatusCode.PASSWORD_INCORRECT, responseMsg.getStatus());

    }

    @Test
    public void testSuccessSignInRequest() {

        // AuthRequestHandler should handle login request

        EmbeddedChannel channel = new EmbeddedChannel(new AuthRequestHandler(accounts));

        Message loginRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.USER_SIGN_IN_REQUEST)
                .setUserSignInReq(UserSignInReqBody.newBuilder()
                        .setUsername("testUser")
                        .setPassword("password123")
                )
                .build();

        channel.writeInbound(loginRequest);
        channel.finish();

        assertNull(channel.readInbound());

        Object response = channel.readOutbound();
        assertTrue(response instanceof Message);

        Message responseMsg = (Message) response;
        assertEquals(123, responseMsg.getSeqId());
        assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
        assertEquals(Message.StatusCode.SUCCESS, responseMsg.getStatus());
        assertTrue(responseMsg.getUserSignInResp().isInitialized());
        assertEquals("testUser", responseMsg.getUserSignInResp().getClientId());  // to be replaced by ID

    }

}