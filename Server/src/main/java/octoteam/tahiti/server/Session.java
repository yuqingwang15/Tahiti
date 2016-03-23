package octoteam.tahiti.server;

import com.google.common.base.MoreObjects;

import java.util.HashMap;

public class Session {

    private HashMap<String, Object> data;

    private String sessionId;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.data = new HashMap<>();
    }

    public String getSessionId() {
        return sessionId;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .add("sessionId", sessionId)
                .toString();
    }

}
