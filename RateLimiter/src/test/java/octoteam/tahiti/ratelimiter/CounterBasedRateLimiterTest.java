package octoteam.tahiti.ratelimiter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CounterBasedRateLimiterTest {

    @Test
    public void testRateLimiting() {
        CounterBasedRateLimiter limiter = new CounterBasedRateLimiter(2);
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
    }

}