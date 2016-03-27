package octoteam.tahiti.server;

import com.google.common.base.MoreObjects;

import java.util.HashMap;

public class Session {

    private final HashMap<String, Object> data;

    public Session() {
        this.data = new HashMap<>();
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

    public void clear() {
        data.clear();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .toString();
    }

}
