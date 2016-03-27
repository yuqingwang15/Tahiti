package octoteam.tahiti.server.pipeline;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.server.event.BaseEvent;

public class UserEventHandler extends ChannelHandlerAdapter {

    private final EventBus eventBus;

    public UserEventHandler(EventBus eventBus) {
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
