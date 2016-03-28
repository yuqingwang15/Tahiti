package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.protocol.SocketMessageProtos.SessionExpiredEventBody;

public class SessionExpiredEvent {

    private SessionExpiredEventBody.Reason reason;

    public SessionExpiredEvent(SessionExpiredEventBody.Reason reason) {
        this.reason = reason;
    }

    public SessionExpiredEventBody.Reason getReason() {
        return reason;
    }

    public void setReason(SessionExpiredEventBody.Reason reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reason", reason)
                .toString();
    }

}
