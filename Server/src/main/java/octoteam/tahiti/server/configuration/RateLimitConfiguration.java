package octoteam.tahiti.server.configuration;

import com.google.common.base.MoreObjects;

/**
 * RequestRateLimitHandler 可用参数<br>
 *     perSecond， 每秒消息次数限制<br>
 *     perSession，总消息次数限制<br>
 */

public class RateLimitConfiguration {

    private int perSecond;

    private int perSession;

    public int getPerSecond() {
        return perSecond;
    }

    public void setPerSecond(int perSecond) {
        this.perSecond = perSecond;
    }

    public int getPerSession() {
        return perSession;
    }

    public void setPerSession(int perSession) {
        this.perSession = perSession;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("perSecond", perSecond)
                .add("perSession", perSession)
                .toString();
    }

}
