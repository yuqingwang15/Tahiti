package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 消息转发事件<br>
 * 当有消息被转发时，MessageForwardEvent 被实例化
 * 并由已加入到 pipeline 中的下一 Handler 处理
 *
 */
public class MessageForwardEvent extends BaseEvent {

    /**
     * 被转发的消息
     */
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageForwardEvent(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .toString();
    }

}
