package octoteam.tahiti.server.pipeline;

import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.protocol.SocketMessageProtos.ChatSendMessageReqBody;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class MessageForwardHandlerTest {

    @Test
    public void testMessageForward() {

        // MessageForwardHandler should construct CHAT_BROADCAST_EVENT message only when received CHAT_SEND_MESSAGE_REQUEST message

        EmbeddedChannel channelSender = new EmbeddedChannel(new NaiveChannelId(0), new MessageForwardHandler());
        EmbeddedChannel channelReceivers[] = new EmbeddedChannel[3];
        for (int i = 0; i < 3; ++i) {
            channelReceivers[i] = new EmbeddedChannel(new NaiveChannelId(i + 1), new MessageForwardHandler());
        }

        Message msgRequest = Message.newBuilder()
                .setSeqId(1038)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.CHAT_SEND_MESSAGE_REQUEST)
                .setChatSendMessageReq(ChatSendMessageReqBody.newBuilder()
                        .setPayload("HELLO :P")
                        .setTimestamp(Calendar.getInstance().getTimeInMillis())
                ).build();

        channelSender.writeInbound(msgRequest);
        channelSender.finish();

        assertEquals(msgRequest, channelSender.readInbound());
        assertNull(channelSender.readOutbound());

        for (int i = 0; i < 3; ++i) {
            channelReceivers[i].finish();

            assertNull(channelReceivers[i].readInbound());

            Object response = channelReceivers[i].readOutbound();
            assertTrue(response instanceof Message);

            Message responseMsg = (Message) response;
            assertEquals(Message.DirectionCode.PUSH, responseMsg.getDirection());
            assertEquals(Message.ServiceCode.CHAT_BROADCAST_PUSH, responseMsg.getService());
            assertEquals(Message.BodyCase.CHATBROADCASTPUSH, responseMsg.getBodyCase());
            assertEquals("HELLO :P", responseMsg.getChatBroadcastPush().getPayload());
            assertEquals("Guest", responseMsg.getChatBroadcastPush().getSenderUsername());
        }

    }

    @Test
    public void testOtherRequest() {

        EmbeddedChannel channel = new EmbeddedChannel(new MessageForwardHandler());

        Message msgRequest = Message.newBuilder()
                .setSeqId(1038)
                .setDirection(Message.DirectionCode.REQUEST)
                .setService(Message.ServiceCode.PING_REQUEST)
                .setChatSendMessageReq(ChatSendMessageReqBody.newBuilder()
                        .setPayload("HELLO :P")
                        .setTimestamp(Calendar.getInstance().getTimeInMillis())
                ).build();

        channel.writeInbound(msgRequest);
        channel.finish();

        // message is not handled so passed to next handler and show up in inbound
        assertEquals(msgRequest, channel.readInbound());
    }
}