package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class RateLimitExceededEvent extends BaseEvent {

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
