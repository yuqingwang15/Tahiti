package octoteam.tahiti.server.channelhandler;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import octoteam.tahiti.protocol.SocketMessageProtos.*;
import octoteam.tahiti.server.configuration.ServerConfiguration;

public class FinalHandler extends SimpleChannelInboundHandler<Message> {

    private ServerConfiguration config;
    private EventBus eventBus;

    public FinalHandler(ServerConfiguration config, EventBus eventBus) {
        this.config = config;
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        // Not handled message here
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (!(cause instanceof DecoderException)) {
            cause.printStackTrace();
        }
        ctx.close();
    }

}
