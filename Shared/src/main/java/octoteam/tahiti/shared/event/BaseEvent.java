package octoteam.tahiti.shared.event;

import com.google.common.base.MoreObjects;

/**
 * 所有自定义事件的基类
 */
public class BaseEvent {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }

}
