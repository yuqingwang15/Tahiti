package octoteam.tahiti.server.ratelimiter;

/**
 * 计数器，限制阈值（不限时间）
 */
public class CounterBasedRateLimiter implements SimpleRateLimiter {

    private final int maxLimit;
    private int acquired = 0;

    /**
     * 根据传入数值设置阈值
     *
     * @param maxLimit 阈值（int）
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
