package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.PipelineUtil;
import octoteam.tahiti.server.Session;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AuthFilterHandlerTest {

    @Test
    public void testFilteringNotAuthenticatedEvent() throws Exception {

        // AuthFilterHandler should eat all event when user is not authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthFilterHandler());

        Message chatEvent = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.EVENT)
                .setService(Message.ServiceCode.CHAT_BROADCAST_EVENT)
                .build();

        channel.writeOutbound(chatEvent);
        channel.finish();

        assertNull(channel.readInbound());
        assertNull(channel.readOutbound());

    }

    @Test
    public void testPassAuthenticatedEvent() throws Exception {

        // AuthFilterHandler should pass all event when user is authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthFilterHandler());
        PipelineUtil.setSession(channel, new Session("abc"));

        Message chatEvent = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.EVENT)
                .setService(Message.ServiceCode.CHAT_BROADCAST_EVENT)
                .build();

        channel.writeOutbound(chatEvent);
        channel.finish();

        assertEquals(chatEvent, channel.readOutbound());
        assertNull(channel.readInbound());

    }

    @Test
    public void testPassNotAuthenticatedResponse() throws Exception {

        // AuthFilterHandler should pass response to the next handler
        // if user is not authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthFilterHandler());

        Message someResponse = Message.newBuilder()
                .setSeqId(345)
                .setDirection(Message.DirectionCode.RESPONSE)
                .build();

        channel.writeOutbound(someResponse);
        channel.finish();

        assertEquals(someResponse, channel.readOutbound());
        assertNull(channel.readInbound());

    }

    @Test
    public void testPassAuthenticatedResponse() throws Exception {

        // AuthFilterHandler should pass response to the next handler
        // if user is authenticated

        EmbeddedChannel channel = new EmbeddedChannel(new AuthFilterHandler());
        PipelineUtil.setSession(channel, new Session("abc"));

        Message someResponse = Message.newBuilder()
                .setSeqId(345)
                .setDirection(Message.DirectionCode.RESPONSE)
                .build();

        channel.writeOutbound(someResponse);
        channel.finish();

        assertEquals(someResponse, channel.readOutbound());
        assertNull(channel.readInbound());

    }

    @Test
    public void testInboundMessage() throws Exception {

        // AuthFilterHandler should do nothing to the inbound message

        EmbeddedChannel channel = new EmbeddedChannel(new AuthFilterHandler());

        Message someRequest = Message.newBuilder()
                .setSeqId(345)
                .setDirection(Message.DirectionCode.REQUEST)
                .build();

        channel.writeInbound(someRequest);
        channel.finish();

        assertEquals(someRequest, channel.readInbound());
        assertNull(channel.readOutbound());

    }

}