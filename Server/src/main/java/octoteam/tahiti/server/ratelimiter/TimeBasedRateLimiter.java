package octoteam.tahiti.server.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

public class TimeBasedRateLimiter implements SimpleRateLimiter {

    private final RateLimiter rl;

    public TimeBasedRateLimiter(double QPS) {
        rl = RateLimiter.create(QPS);
    }

    public boolean tryAcquire() {
        return rl.tryAcquire();
    }

}
