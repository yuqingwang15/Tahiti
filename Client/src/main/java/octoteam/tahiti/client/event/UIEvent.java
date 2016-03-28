package octoteam.tahiti.client.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.shared.event.BaseEvent;

public class UIEvent extends BaseEvent {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }

}
