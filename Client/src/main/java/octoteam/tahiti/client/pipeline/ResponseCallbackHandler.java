package octoteam.tahiti.client.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import octoteam.tahiti.client.CallbackRepository;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;
import octoteam.tahiti.shared.netty.MessageHandler;

/**
 * login 的时候传入一个回调方法，这个方法是根据登陆是否成功的状态改变界面。每个人 login 的时候都会有自己的回调方法。
 * 在多人同时 login 的时候，程序会把这些方法和每个人的seqid 绑定，存入callbacks这个map 中，一旦收到服务器返回的登
 * 陆状态消息，就调用 resolveCallback, get 出来这个方法，然后调用方法去根据登陆状态消息处理界面
 */
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
