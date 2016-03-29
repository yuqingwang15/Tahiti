package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;

/**
 * 用户点击发送按钮的事件
 */
public class UIOnSendCommandEvent extends UIEvent {

    /**
     * 填写的消息内容
     */
    private String payload;

    public UIOnSendCommandEvent(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("payload", payload)
                .toString();
    }

}
