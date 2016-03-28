package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.shared.event.BaseEvent;

public class ConnectedEvent extends BaseEvent {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }

}
