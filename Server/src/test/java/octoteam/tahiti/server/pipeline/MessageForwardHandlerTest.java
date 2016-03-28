package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.ChatMessageReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageForwardHandlerTest {

    @Test
    public void testMessageForward() {

        // MessageForwardHandler should construct CHAT_BROADCAST_EVENT message only when received CHAT_SEND_MESSAGE_REQUEST message

        EmbeddedChannel channel = new EmbeddedChannel(new MessageForwardHandler());

        Message msgRequest = Message.newBuilder()
                .setSeqId(1038)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)
                .setChatMessageReq(ChatMessageReqBody.newBuilder()
                        .setPayload("HELLO :P")
                        .setTimestamp(Calendar.getInstance().getTimeInMillis())
                ).build();

        channel.writeInbound(msgRequest);
        channel.finish();

        assertEquals(msgRequest, channel.readInbound());

        // not writing CHAT_BROADCAST_EVENT message to itself
        assertNull(channel.readOutbound());
    }

    @Test
    public void testOtherRequest() {

        EmbeddedChannel channel = new EmbeddedChannel(new MessageForwardHandler());

        Message msgRequest = Message.newBuilder()
                .setSeqId(1038)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.PING_REQUEST)
                .setChatMessageReq(ChatMessageReqBody.newBuilder()
                        .setPayload("HELLO :P")
                        .setTimestamp(Calendar.getInstance().getTimeInMillis())
                ).build();

        channel.writeInbound(msgRequest);
        channel.finish();

        // message is not handled so passed to next handler and show up in inbound
        assertEquals(msgRequest, channel.readInbound());
    }
}