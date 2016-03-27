package octoteam.tahiti.server.configuration;

import com.google.common.base.MoreObjects;

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
