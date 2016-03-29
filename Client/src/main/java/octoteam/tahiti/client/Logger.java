package octoteam.tahiti.client;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.client.event.ChatMessageEvent;
import octoteam.tahiti.client.event.LoginAttemptEvent;
import octoteam.tahiti.client.event.SendMessageEvent;
import octoteam.tahiti.shared.logging.StatisticsLogger;

public class Logger extends StatisticsLogger {

    public static final String SUCCESSFUL_LOGIN = "Successful login";
    public static final String FAILED_LOGIN = "Failed login";
    public static final String SEND_MESSAGE = "Send message";
    public static final String RECEIVE_MESSAGE = "Receive message";

    Logger(String filePath, int periodSeconds) {
        super(filePath, periodSeconds);
        clear(SUCCESSFUL_LOGIN);
        clear(FAILED_LOGIN);
        clear(SEND_MESSAGE);
        clear(RECEIVE_MESSAGE);
    }

    /**
     * 订阅用户尝试登录事件 LoginAttemptEvent
     * 监听用户尝试登录的行为, 事件中记录了该次登录是否成功,
     * 若登录成功则增加 VALID_LOGIN 的次数, 否则增加 INVALID_LOGIN 次数
     *
     * @param event 事件对象
     */
    @Subscribe
    public void onLogin(LoginAttemptEvent event) {
        if (event.isSuccess()) {
            increase(SUCCESSFUL_LOGIN);
        } else {
            increase(FAILED_LOGIN);
        }
    }

    /**
     * 订阅用户准备发送消息事件 SendMessageEvent
     * 监听该用户准备发送消息的行为,
     * 用户每发送一个消息, 增加一次 SEND_MESSAGE 的次数
     *
     * @param event 事件对象
     */
    @Subscribe
    public void onSendMessage(SendMessageEvent event) {
        increase(SEND_MESSAGE);
    }


    /**
     * 订阅用户收到他人消息事件 ChatMessageEvent
     * 监听已登录用户接受来自服务器转发的消息的行为,
     * 用户每接受一次转发的消息, 增加 RECEIVE_MESSAGE 的次数
     *
     * @param event 事件对象
     */
    @Subscribe
    public void onReceiveChatMessage(ChatMessageEvent event) {
        increase(RECEIVE_MESSAGE);
    }

}
