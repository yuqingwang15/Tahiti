package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.TahitiClient;
import octoteam.tahiti.client.event.MessageEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class ResponseHandler extends MessageHandler {

    private TahitiClient client;

    public ResponseHandler(TahitiClient client) {
        this.client = client;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        client.resolveCallback(msg.getSeqId(), msg);
        client.getEventBus().post(new MessageEvent(msg));
        System.out.println(msg);
    }

}
