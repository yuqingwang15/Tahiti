package octoteam.tahiti.server.event;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 消息超额事件<br>
 * 当客户端发送的消息超出限制（每秒或每次回话限额）时，RateLimitExceededEvent 被实例化
 * 并由已加入到 pipeline 中的下一 Handler 处理
 */
public class RateLimitExceededEvent extends BaseEvent {

    public static final String NAME_PER_SESSION = "perSession";
    public static final String NAME_PER_SECOND = "perSecond";

    /**
     * TODO
     */
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
