package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 消息事件<br><br>
 *     消息<br>
 *     消息是否来自已验证的链接<br>
 */
public class MessageEvent extends BaseEvent {

    /**
     * TODO
     */
    private boolean authenticated;

    /**
     * TODO
     */
    private Message message;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageEvent(boolean authenticated, Message message) {
        this.authenticated = authenticated;
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("authenticated", authenticated)
                .add("message", message)
                .toString();
    }

}
