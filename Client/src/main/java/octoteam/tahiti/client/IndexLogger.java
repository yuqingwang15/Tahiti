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

    private final CountingRecorder successfulLoginRecorder;
    private final CountingRecorder failedLoginRecorder;
    private final CountingRecorder sendMessageRecorder;
    private final CountingRecorder receivedMessageRecorder;

    IndexLogger(String filePattern, int periodSeconds) {
        new PerformanceMonitor(new RollingFileReporter(filePattern))
                .addRecorder(successfulLoginRecorder = new CountingRecorder("Successful login times"))
                .addRecorder(failedLoginRecorder = new CountingRecorder("Failed login times"))
                .addRecorder(sendMessageRecorder = new CountingRecorder("Sent messages"))
                .addRecorder(receivedMessageRecorder = new CountingRecorder("Received messages"))
                .start(periodSeconds, TimeUnit.SECONDS);
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
            successfulLoginRecorder.record();
        } else {
            failedLoginRecorder.record();
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
        sendMessageRecorder.record();
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
        receivedMessageRecorder.record();
    }

}
