package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.protocol.SocketMessageProtos.SessionExpiredEventBody;
import octoteam.tahiti.server.event.RateLimitExceededEvent;
import octoteam.tahiti.server.ratelimiter.CounterBasedRateLimiter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SessionExpireHandlerTest {

    /**
     * clear session once count of messages from client exceeded
     */

    @Test
    public void testSessionExpired() {
        EmbeddedChannel channel = new EmbeddedChannel(new RequestRateLimitHandler(
                Message.ServiceCode.PING_REQUEST,
                RateLimitExceededEvent.NAME_PER_SESSION,
                () -> new CounterBasedRateLimiter(2)), new SessionExpireHandler());

        for (int i = 0; i < 3; i++) {
            Message pingRequest = Message.newBuilder()
                    .setSeqId(1)
                    .setDirection(Message.DirectionCode.REQUEST)
                    .setService(Message.ServiceCode.PING_REQUEST)
                    .build();
            channel.writeInbound(pingRequest);
            if (i == 2) {
                // only care about EVENT message
                Object response = channel.readOutbound();
                assertTrue(response instanceof Message);

                Message responseMsg = (Message) response;
                assertEquals(responseMsg.getDirection(), Message.DirectionCode.EVENT);
                assertEquals(responseMsg.getService(), Message.ServiceCode.SESSION_EXPIRED_EVENT);
                assertEquals(responseMsg.getSessionExpiredEvent().getReason(),
                        SessionExpiredEventBody.Reason.EXPIRED);
            }
        }

    }

}