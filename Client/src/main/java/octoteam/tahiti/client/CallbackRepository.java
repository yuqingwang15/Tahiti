package octoteam.tahiti.client;

import com.google.common.base.Function;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CallbackRepository {

    private static AtomicLong msgSequence = new AtomicLong();

    private ConcurrentHashMap<Long, Function<Message, Void>> callbacks = new ConcurrentHashMap<>();

    public long getNextSequence() {
        return msgSequence.incrementAndGet();
    }

    public long getNextSequence(Function<Message, Void> r) {
        long seq = getNextSequence();
        if (r != null) callbacks.put(seq, r);
        return seq;
    }

    public void resolveCallback(long seqId, Message msg) {
        if (callbacks.containsKey(seqId)) {
            Function<Message, Void> r = callbacks.get(seqId);
            callbacks.remove(seqId);
            r.apply(msg);
        }
    }

}
