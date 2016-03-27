package octoteam.tahiti.server.session;

import com.google.common.base.MoreObjects;

public class Credential {

    public String userId;

    public Credential(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .toString();
    }

}
