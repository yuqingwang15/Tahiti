package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;

public class ReconnectAttemptEvent extends BaseEvent {

    private int reconnectInSeconds;

    public ReconnectAttemptEvent(int reconnectInSeconds) {
        this.reconnectInSeconds = reconnectInSeconds;
    }

    public int getReconnectInSeconds() {
        return reconnectInSeconds;
    }

    public void setReconnectInSeconds(int reconnectInSeconds) {
        this.reconnectInSeconds = reconnectInSeconds;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reconnectInSeconds", reconnectInSeconds)
                .toString();
    }

}
