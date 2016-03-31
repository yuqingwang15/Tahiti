package octoteam.tahiti.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 计时器，限制 1 秒内的阈值
 * 使用 tryAcquire() 判断是否到达阈值
 */
public class TimeBasedRateLimiter implements SimpleRateLimiter {

    private final RateLimiter rl;

    /**
     * 初始化阈值
     *
     * @param QPS 每秒中允许的阈值
     */
    public TimeBasedRateLimiter(double QPS) {
        rl = RateLimiter.create(QPS);
    }

    public boolean tryAcquire() {
        return rl.tryAcquire();
    }

}
