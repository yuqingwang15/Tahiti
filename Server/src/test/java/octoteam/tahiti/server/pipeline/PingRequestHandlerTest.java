package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.PingPongBody;
import org.junit.Test;

import static org.junit.Assert.*;

public class PingRequestHandlerTest {

    @Test
    public void testPingRequest() {

        // PingRequestHandler should handle PING_REQUEST

        EmbeddedChannel channel = new EmbeddedChannel(new PingRequestHandler());

        Message pingRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.PING_REQUEST)
                .setPingPong(PingPongBody.newBuilder()
                        .setPayload("magic payload")
                )
                .build();

        channel.writeInbound(pingRequest);
        channel.finish();

        // this message should be consumed
        // so that next handler will not receive it
        assertNull(channel.readInbound());

        // expect a response from PingRequestHandler
        Object response = channel.readOutbound();
        assertTrue(response instanceof Message);

        Message responseMsg = (Message) response;
        assertTrue(responseMsg.isInitialized());
        assertEquals(123, responseMsg.getSeqId());
        assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
        assertEquals(Message.BodyCase.PINGPONG, responseMsg.getBodyCase());
        assertEquals("magic payload", responseMsg.getPingPong().getPayload());

    }

    @Test
    public void testOtherRequest() {

        // PingRequestHandler should NOT handle messages other than PING_REQUEST

        EmbeddedChannel channel = new EmbeddedChannel(new PingRequestHandler());

        Message otherRequest = Message.newBuilder()
                .setSeqId(321)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.USER_SIGN_IN_REQUEST)
                .build();

        channel.writeInbound(otherRequest);
        channel.finish();

        // next handler should be able to read this "otherRequest"
        assertEquals(otherRequest, channel.readInbound());

        // no outbound data should be written
        assertNull(channel.readOutbound());

    }

}