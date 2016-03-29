package octoteam.tahiti.shared.netty.pipeline;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * 该模块会将管道中产生的事件全部转发到给定的 EventBus 中
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
