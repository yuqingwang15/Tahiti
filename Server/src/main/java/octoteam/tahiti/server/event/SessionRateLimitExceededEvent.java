package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import io.netty.channel.Channel;

public class SessionRateLimitExceededEvent extends BaseEvent {
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public SessionRateLimitExceededEvent(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel remote address", channel.remoteAddress())
                .toString();
    }
}
