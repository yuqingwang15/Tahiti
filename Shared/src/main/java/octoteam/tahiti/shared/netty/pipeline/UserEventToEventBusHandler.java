package octoteam.tahiti.shared.netty.pipeline;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 将消息处理队列中产生的用户事件提交至服务端事件总线中
 */
public class UserEventToEventBusHandler extends ChannelDuplexHandler {

    private final EventBus eventBus;

    /**
     * 设置处理用户事件的服务端事件总线
     *
     * @param eventBus 服务端事件总线 （EventBus）
     */
    public UserEventToEventBusHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof BaseEvent) {
            eventBus.post(evt);
        }
        ctx.fireUserEventTriggered(evt);
    }

}
