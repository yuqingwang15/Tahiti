package octoteam.tahiti.server.channelhandler;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.server.event.RawMessageEvent;

public class RawHandler extends SimpleChannelInboundHandler<Message> {

    private EventBus eventBus;

    public RawHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        eventBus.post(new RawMessageEvent(msg));
        ctx.fireChannelRead(msg);
    }

}
