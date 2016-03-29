package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.ChatSendMessageReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageRequestHandlerTest {

    @Test
    public void testMessageRequest() {

        // MessageRequestHandler should handle CHAT_SEND_MESSAGE_REQUEST

        EmbeddedChannel channel = new EmbeddedChannel(new MessageRequestHandler());

        Message msgRequest = Message.newBuilder()
                .setSeqId(123)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)
                .setChatSendMessageReq(ChatSendMessageReqBody.newBuilder()
                        .setPayload("foo")
                        .setTimestamp(333)
                )
                .build();

        channel.writeInbound(msgRequest);
        channel.finish();

        assertEquals(msgRequest, channel.readInbound());

        Object response = channel.readOutbound();
        assertTrue(response instanceof Message);

        Message responseMsg = (Message) response;
        assertTrue(responseMsg.isInitialized());
        assertEquals(123, responseMsg.getSeqId());
        assertEquals(Message.DirectionCode.RESPONSE, responseMsg.getDirection());
        assertEquals(Message.StatusCode.SUCCESS, responseMsg.getStatus());

    }

    @Test
    public void testOtherRequest() {

        // MessageRequestHandler should not handle other request

        EmbeddedChannel channel = new EmbeddedChannel(new MessageRequestHandler());

        Message otherRequest = Message.newBuilder()
                .setSeqId(212)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.PING_REQUEST)
                .build();

        channel.writeInbound(otherRequest);
        channel.finish();

        assertEquals(otherRequest, channel.readInbound());
        assertNull(channel.readOutbound());

    }

}