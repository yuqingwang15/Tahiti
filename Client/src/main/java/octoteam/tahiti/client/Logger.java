package octoteam.tahiti.client;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.client.event.LoginAttemptEvent;
import octoteam.tahiti.client.event.ReceiveMessageEvent;
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
     * 订阅用户尝试登陆 LoginAttemptEvent 事件, 该事件的类型为该函数的参数类型.
     * 监听用户尝试登录的行为, 事件中记录了该登陆是否成功,
     * 若登陆成功则增加 VALID_LOGIN 的次数, 否则增加 INVALID_LOGIN 次数.
     *
     * @param event 监听的事件类型
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
     * 订阅消息发送 SendMessageEvent 事件, 该事件的类型为该函数的参数类型.
     * 监听已登录用户发送需要被转发的消息的行为,
     * 用户每发送一个消息, 增加一次 SEND_MESSAGE 的次数, .
     *
     * @param event 监听的事件类型
     */
    @Subscribe
    public void onSendMessage(SendMessageEvent event) {
        increase(SEND_MESSAGE);
    }


    /**
     * 订阅用户接受转发消息 ReceiveMessageEvent 事件, 该事件的类型为该函数的参数类型.
     * 监听已登录用户接受来自服务器转发的消息的行为,
     * 用户每接受一次转发的消息, 增加 RECEIVE_MESSAGE 的次数, .
     *
     * @param event 监听的事件类型
     */
    @Subscribe
    public void onReceiveMessage(ReceiveMessageEvent event) {
        increase(RECEIVE_MESSAGE);
    }

}
