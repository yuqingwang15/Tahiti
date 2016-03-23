package octoteam.tahiti.server.ratelimiter;

public class CounterBasedRateLimiter implements SimpleRateLimiter {

    private int maxLimit;
    private int acquired = 0;

    public CounterBasedRateLimiter(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public boolean tryAcquire() {
        if (acquired < maxLimit) {
            acquired++;
            return true;
        } else {
            return false;
        }
    }

}
