package octoteam.tahiti.server.ratelimiter;

/**
 * TODO
 */
public class CounterBasedRateLimiter implements SimpleRateLimiter {

    private final int maxLimit;
    private int acquired = 0;

    /**
     * TODO
     *
     * @param maxLimit
     */
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
