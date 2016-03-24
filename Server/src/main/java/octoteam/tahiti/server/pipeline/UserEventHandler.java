package octoteam.tahiti.server.pipeline;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import octoteam.tahiti.server.event.BaseEvent;

public class UserEventHandler extends ChannelInboundHandlerAdapter {

    private EventBus eventBus;

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
