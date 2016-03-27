package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;

public class RateLimitExceededEvent extends BaseEvent {

    public static final String NAME_PER_SESSION = "perSession";
    public static final String NAME_PER_SECOND = "perSecond";

    private String name;

    public RateLimitExceededEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }

}
