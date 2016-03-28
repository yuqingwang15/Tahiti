package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * TODO
 */
public class MessageForwardEvent extends BaseEvent {

    /**
     * TODO
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
