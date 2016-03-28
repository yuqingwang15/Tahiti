package octoteam.tahiti.shared.protocol;

import octoteam.tahiti.protocol.SocketMessageProtos.Message;

/**
 * TODO
 */
public class ProtocolUtil {

    /**
     * TODO
     *
     * @param request
     * @return
     */
    public static Message.Builder buildResponse(Message request) {
        return Message.newBuilder()
                .setDirection(Message.DirectionCode.RESPONSE)
                .setService(request.getService())
                .setSeqId(request.getSeqId());
    }

    /**
     * TODO
     *
     * @param event
     * @return
     */
    public static Message.Builder buildAck(Message event) {
        return Message.newBuilder()
                .setDirection(Message.DirectionCode.ACK)
                .setService(event.getService());
    }

}
