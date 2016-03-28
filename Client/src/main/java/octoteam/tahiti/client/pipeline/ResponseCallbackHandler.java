package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.CallbackRepository;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

@ChannelHandler.Sharable
public class ResponseCallbackHandler extends MessageHandler {

    private CallbackRepository callbackRepository;

    public ResponseCallbackHandler(CallbackRepository callbackRepository) {
        this.callbackRepository = callbackRepository;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) {
        callbackRepository.resolveCallback(msg.getSeqId(), msg);
    }

}
