package octoteam.tahiti.client.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Store {

    private HashMap<String, Object> store = new HashMap<>();
    private HashMap<String, List<Runnable>> observations = new HashMap<>();

    private Boolean batchOp = false;
    private HashSet<Runnable> changedObservations = new HashSet<>();

    public void beginUpdate() {
        if (batchOp) {
            return;
        }
        batchOp = true;
        changedObservations.clear();
    }

    public void init(String key, Object value) {
        store.put(key, value);
    }

    public void put(String key, Object value) {
        if (!batchOp) {
            throw new RuntimeException("beginUpdate not called");
        }
        store.put(key, value);
        if (observations.containsKey(key)) {
            changedObservations.addAll(observations.get(key));
        }
    }

    public void endUpdate() {
        if (!batchOp) {
            return;
        }
        changedObservations.forEach(Runnable::run);
        changedObservations.clear();
        batchOp = false;
    }

    public Object get(String key) {
        return store.get(key);
    }

    public void observe(String key, Runnable r) {
        if (!observations.containsKey(key)) {
            observations.put(key, new LinkedList<>());
        }
        observations.get(key).add(r);
    }

}
