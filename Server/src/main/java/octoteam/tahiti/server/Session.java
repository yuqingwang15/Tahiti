package octoteam.tahiti.server;

import com.google.common.base.MoreObjects;

public class Session {

    private String sessionId;

    // TODO: Change to UserBean after introducing database
    private String username;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sessionId", sessionId)
                .add("username", username)
                .toString();
    }
}
