package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.protocol.SocketMessageProtos;

public class MessageForwardEvent extends BaseEvent {

    private SocketMessageProtos.Message message;

    public SocketMessageProtos.Message getMessage() {
        return message;
    }

    public void setMessage(SocketMessageProtos.Message message) {
        this.message = message;
    }

    public MessageForwardEvent(SocketMessageProtos.Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .toString();
    }

}
