package octoteam.tahiti.server.ratelimiter;

public interface SimpleRateLimiter {

    boolean tryAcquire();

}
