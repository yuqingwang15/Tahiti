package octoteam.tahiti.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import octoteam.tahiti.protocol.SocketMessageProtos;

public class DiscardServerHandler extends SimpleChannelInboundHandler<SocketMessageProtos.Message> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketMessageProtos.Message msg) {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof DecoderException) {
            // TODO
        } else {
            cause.printStackTrace();
        }
        ctx.close();
    }

}
