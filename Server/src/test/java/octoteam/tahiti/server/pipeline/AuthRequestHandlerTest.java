package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.UserSignInReqBody;
import octoteam.tahiti.server.configuration.AccountConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

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
    public void testUsernameNotFoundSignInRequest() {

        // AuthRequestHandler should handle username_not_found login request

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

        // AuthRequestHandler should handle wrong_password login request

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

        // AuthRequestHandler should handle success login request

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