package octoteam.tahiti.ratelimiter;

/**
 * 提供基于计数的限流器接口。获得令牌超过允许次数限制后，就无法继续获得令牌。
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
