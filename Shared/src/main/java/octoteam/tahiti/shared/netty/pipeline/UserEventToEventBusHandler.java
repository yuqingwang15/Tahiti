package octoteam.tahiti.shared.netty.pipeline;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.shared.event.BaseEvent;

/**
 * TODO
 */
public class UserEventToEventBusHandler extends ChannelHandlerAdapter {

    private final EventBus eventBus;

    /**
     * TODO
     *
     * @param eventBus
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
