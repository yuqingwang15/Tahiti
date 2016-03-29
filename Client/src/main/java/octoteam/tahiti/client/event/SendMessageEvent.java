package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 用户准备发送消息事件
 */
public class SendMessageEvent extends BaseEvent {

    /**
     * 消息内容
     */
    private String payload;

    /**
     * 发送时间
     */
    private long timestamp;

    public SendMessageEvent(String payload, long timestamp) {
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("payload", payload)
                .add("timestamp", timestamp)
                .toString();
    }

}
