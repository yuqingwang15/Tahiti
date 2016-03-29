package octoteam.tahiti.server.pipeline;

import io.netty.channel.ChannelId;

public class NaiveChannelId implements ChannelId {

    private int id;

    public NaiveChannelId(int id) {
        this.id = id;
    }

    @Override
    public String asShortText() {
        return "0x" + Integer.toHexString(id);
    }

    @Override
    public String asLongText() {
        return "0x" + Integer.toHexString(id);
    }

    @Override
    public int compareTo(ChannelId o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof NaiveChannelId)) {
            return false;
        }

        return id == ((NaiveChannelId) obj).id;
    }

}
