package octoteam.tahiti.protocol;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;

public class TestSocketMessage {

    @Test
    public void testUserSignInReq() throws InvalidProtocolBufferException {
        SocketMessageProtos.Message msg = SocketMessageProtos.Message
                .newBuilder()
                .setType(SocketMessageProtos.Message.MessageType.USER_SIGN_IN_REQ)
                .setUserSignInReq(
                        SocketMessageProtos.UserSignInReqBody
                                .newBuilder()
                                .setUsername("foo")
                                .setPassword("bar")
                )
                .build();
        byte[] payload = msg.toByteArray();

        SocketMessageProtos.Message parsedMsg = SocketMessageProtos.Message
                .parseFrom(payload);
        System.out.println(parsedMsg);
        Assert.assertEquals(
                SocketMessageProtos.Message.MessageType.USER_SIGN_IN_REQ,
                parsedMsg.getType()
        );
        Assert.assertEquals(
                "foo",
                parsedMsg.getUserSignInReq().getUsername()
        );
        Assert.assertEquals(
                "bar",
                parsedMsg.getUserSignInReq().getPassword()
        );
    }

    @Test
    public void testUserSignInResp() throws InvalidProtocolBufferException {
        SocketMessageProtos.Message msg = SocketMessageProtos.Message
                .newBuilder()
                .setType(SocketMessageProtos.Message.MessageType.USER_SIGN_IN_RESP)
                .setUserSignInResp(
                        SocketMessageProtos.UserSignInRespBody
                                .newBuilder()
                                .setError(SocketMessageProtos.UserSignInRespBody.Error.SUCCESS)
                                .setClientId("foo#1")
                                .setSessionId("abc")
                )
                .build();
        byte[] payload = msg.toByteArray();

        SocketMessageProtos.Message parsedMsg = SocketMessageProtos.Message
                .parseFrom(payload);
        System.out.println(parsedMsg);
        Assert.assertEquals(
                SocketMessageProtos.Message.MessageType.USER_SIGN_IN_RESP,
                parsedMsg.getType()
        );
        Assert.assertEquals(
                SocketMessageProtos.UserSignInRespBody.Error.SUCCESS,
                parsedMsg.getUserSignInResp().getError()
        );
        Assert.assertEquals(
                "foo#1",
                parsedMsg.getUserSignInResp().getClientId()
        );
        Assert.assertEquals(
                "abc",
                parsedMsg.getUserSignInResp().getSessionId()
        );
    }

}
