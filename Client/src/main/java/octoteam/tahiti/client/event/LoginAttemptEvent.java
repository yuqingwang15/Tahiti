package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 用户登录结果事件
 */
public class LoginAttemptEvent extends BaseEvent {

    /**
     * 登录是否成功
     */
    private boolean success;

    public LoginAttemptEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", success)
                .toString();
    }

}
