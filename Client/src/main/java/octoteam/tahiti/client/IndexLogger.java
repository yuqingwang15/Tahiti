package octoteam.tahiti.client;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.client.event.ChatMessageEvent;
import octoteam.tahiti.client.event.LoginAttemptEvent;
import octoteam.tahiti.client.event.SendMessageEvent;
import otcoteam.tahiti.performance.PerformanceMonitor;
import otcoteam.tahiti.performance.recorder.CountingRecorder;
import otcoteam.tahiti.performance.reporter.RollingFileReporter;

import java.util.concurrent.TimeUnit;

class IndexLogger {

    private static final String SUCCESSFUL_LOGIN = "SUCCESSFUL_LOGIN";
    private static final String FAILED_LOGIN = "FAILED_LOGIN";
    private static final String SENT_MESSAGE = "SENT_MESSAGE";
    private static final String RECEIVED_MESSAGE = "RECEIVED_MESSAGE";

    private PerformanceMonitor monitor;

    IndexLogger(String filePattern, int periodSeconds) {
        monitor = new PerformanceMonitor(
                new RollingFileReporter(filePattern),
                periodSeconds,
                TimeUnit.SECONDS
        );
        monitor.addRecorder(SUCCESSFUL_LOGIN, new CountingRecorder("Successful login times"));
        monitor.addRecorder(FAILED_LOGIN, new CountingRecorder("Failed login times"));
        monitor.addRecorder(SENT_MESSAGE, new CountingRecorder("Sent messages"));
        monitor.addRecorder(RECEIVED_MESSAGE, new CountingRecorder("Received messages"));
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
            monitor.record(SUCCESSFUL_LOGIN);
        } else {
            monitor.record(FAILED_LOGIN);
        }
    }

    /**
     * 订阅用户准备发送消息事件 SendMessageEvent
     * 监听该用户准备发送消息的行为,
     * 用户每发送一个消息, 增加一次 SENT_MESSAGE 的次数
     *
     * @param event 事件对象
     */
    @Subscribe
    public void onSendMessage(SendMessageEvent event) {
        monitor.record(SENT_MESSAGE);
    }


    /**
     * 订阅用户收到他人消息事件 ChatMessageEvent
     * 监听已登录用户接受来自服务器转发的消息的行为,
     * 用户每接受一次转发的消息, 增加 RECEIVED_MESSAGE 的次数
     *
     * @param event 事件对象
     */
    @Subscribe
    public void onReceiveChatMessage(ChatMessageEvent event) {
        monitor.record(RECEIVED_MESSAGE);
    }

}
