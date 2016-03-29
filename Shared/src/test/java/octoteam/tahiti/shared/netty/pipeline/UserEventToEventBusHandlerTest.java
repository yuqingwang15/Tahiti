package octoteam.tahiti.shared.netty.pipeline;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import octoteam.tahiti.shared.event.BaseEvent;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserEventToEventBusHandlerTest {

    @Test
    public void testUserEvent() {

        List<Object> collectedEvents = new LinkedList<>();
        EventBus eventBus = new EventBus();

        eventBus.register(new Object() {
            @Subscribe
            public void collect(DeadEvent ev) {
                collectedEvents.add(ev.getEvent());
            }
        });

        BaseEvent event = new BaseEvent();

        ChannelHandler raiseEventChannelHandler = new ChannelDuplexHandler() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                ctx.fireUserEventTriggered(event);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(raiseEventChannelHandler, new UserEventToEventBusHandler(eventBus));
        channel.finish();

        assertEquals(1, collectedEvents.size());
        assertEquals(event, collectedEvents.get(0));

    }

}