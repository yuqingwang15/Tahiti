package octoteam.tahiti.shared.protocol;

import octoteam.tahiti.protocol.SocketMessageProtos.Message;

/**
 * 用于构造不同消息的工具类
 */
public class ProtocolUtil {

    /**
     * 根据客户端请求消息构造服务端回应消息
     *
     * @param request 请求消息 （Message）
     * @return builder 响应消息的构造器 （Message.Builder）
     */
    public static Message.Builder buildResponse(Message request) {
        return Message.newBuilder()
                .setDirection(Message.DirectionCode.RESPONSE)
                .setService(request.getService())
                .setSeqId(request.getSeqId());
    }

    /**
     * 根据服务端向客户端下发的消息构造客户端向服务端回复的确认消息
     *
     * @param event 服务端向客户端下发的消息 （Message）
     * @return builder 确认消息的构造器 (Message.Builder)
     */
    public static Message.Builder buildAck(Message event) {
        return Message.newBuilder()
                .setDirection(Message.DirectionCode.ACK)
                .setService(event.getService());
    }

}
