package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.protocol.SocketMessageProtos.SessionExpiredPushBody;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 会话已过期事件
 */
public class SessionExpiredEvent extends BaseEvent {

    /**
     * 会话过期原因
     */
    private SessionExpiredPushBody.Reason reason;

    public SessionExpiredEvent(SessionExpiredPushBody.Reason reason) {
        this.reason = reason;
    }

    public SessionExpiredPushBody.Reason getReason() {
        return reason;
    }

    public void setReason(SessionExpiredPushBody.Reason reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reason", reason)
                .toString();
    }

}
