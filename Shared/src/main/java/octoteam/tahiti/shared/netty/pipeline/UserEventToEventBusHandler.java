package octoteam.tahiti.shared.netty.pipeline;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.shared.event.BaseEvent;

public class UserEventToEventBusHandler extends ChannelHandlerAdapter {

    private final EventBus eventBus;

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
