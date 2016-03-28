package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 用户尝试登录事件
 */
public class LoginAttemptEvent extends BaseEvent {

    /**
     * 登录是否成功
     */
    private Boolean success;

    /**
     * 登录用户的用户名
     */
    private String username;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginAttemptEvent(Boolean success, String username) {
        this.success = success;
        this.username = username;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", success)
                .add("username", username)
                .toString();
    }

}
