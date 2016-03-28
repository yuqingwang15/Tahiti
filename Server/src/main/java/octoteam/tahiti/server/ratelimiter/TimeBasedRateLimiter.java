package octoteam.tahiti.server.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * TODO
 */
public class TimeBasedRateLimiter implements SimpleRateLimiter {

    private final RateLimiter rl;

    /**
     * TODO
     *
     * @param QPS
     */
    public TimeBasedRateLimiter(double QPS) {
        rl = RateLimiter.create(QPS);
    }

    public boolean tryAcquire() {
        return rl.tryAcquire();
    }

}
