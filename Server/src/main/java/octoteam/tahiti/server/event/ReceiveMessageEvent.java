package octoteam.tahiti.server.event;

import octoteam.tahiti.protocol.SocketMessageProtos.*;

public class ReceiveMessageEvent {

    private boolean authenticated;

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

    public ReceiveMessageEvent(boolean authenticated, Message message) {
        this.authenticated = authenticated;
        this.message = message;
    }
}
