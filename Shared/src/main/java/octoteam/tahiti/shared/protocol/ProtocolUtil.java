package octoteam.tahiti.shared.protocol;

import octoteam.tahiti.protocol.SocketMessageProtos.Message;

public class ProtocolUtil {

    public static Message.Builder buildResponse(Message request) {
        return Message.newBuilder()
                .setDirection(Message.DirectionCode.RESPONSE)
                .setService(request.getService())
                .setSeqId(request.getSeqId());
    }

}
