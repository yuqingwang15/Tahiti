package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.Message.ServiceCode;
import octoteam.tahiti.quota.CapacityLimiter;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestRateLimitHandlerTest {

    @Test
    public void testRateLimitBypass() {

        // RequestRateLimitHandler should not limit rate for request other than specified

        EmbeddedChannel channel = new EmbeddedChannel(new RequestRateLimitHandler(
                ServiceCode.PING_REQUEST,
                "foo", () -> new CapacityLimiter(2)
        ));

        for (int i = 0; i < 5; ++i) {
            Message otherRequest = Message.newBuilder()
                    .setSeqId(i)
                    .setDirection(Message.DirectionCode.REQUEST)
                    .setService(ServiceCode.USER_SIGN_IN_REQUEST)
                    .build();
            channel.writeInbound(otherRequest);
            assertEquals(otherRequest, channel.readInbound());
            assertNull(channel.readOutbound());
        }

        channel.finish();

    }

    @Test
    public void testRateLimit() {

        // RequestRateLimitHandler should limit rate for specified request

        EmbeddedChannel channel = new EmbeddedChannel(new RequestRateLimitHandler(
                ServiceCode.PING_REQUEST,
                "foo", () -> new CapacityLimiter(2)
        ));

        // Not limited request
        for (int i = 0; i < 2; ++i) {
            Message pingRequest = Message.newBuilder()
                    .setSeqId(i)
                    .setDirection(Message.DirectionCode.REQUEST)
                    .setService(ServiceCode.PING_REQUEST)
                    .build();
            channel.writeInbound(pingRequest);
            assertEquals(pingRequest, channel.readInbound());
            assertNull(channel.readOutbound());
        }

        // Limited request
        for (int i = 2; i < 5; ++i) {
            Message pingRequest = Message.newBuilder()
                    .setSeqId(i)
                    .setDirection(Message.DirectionCode.REQUEST)
                    .setService(ServiceCode.PING_REQUEST)
                    .build();
            channel.writeInbound(pingRequest);
            assertNull(channel.readInbound());

            Object response = channel.readOutbound();
            assertTrue(response instanceof Message);

            Message responseMsg = (Message) response;
            assertEquals(i, responseMsg.getSeqId());
            assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
            assertEquals(Message.StatusCode.LIMIT_EXCEEDED, responseMsg.getStatus());
        }

        channel.finish();

    }

}
