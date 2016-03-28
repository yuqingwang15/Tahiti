package octoteam.tahiti.server.session;

import com.google.common.base.MoreObjects;

import java.util.HashMap;

/**
 * TODO
 */
public class SessionContainer {

    private final HashMap<String, Object> data;

    public SessionContainer() {
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
