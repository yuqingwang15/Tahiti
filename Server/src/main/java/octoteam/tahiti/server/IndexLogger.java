package octoteam.tahiti.server;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.server.event.LoginAttemptEvent;
import octoteam.tahiti.server.event.MessageEvent;
import octoteam.tahiti.server.event.MessageForwardEvent;
import otcoteam.tahiti.performance.PerformanceMonitor;
import otcoteam.tahiti.performance.recorder.CountingRecorder;
import otcoteam.tahiti.performance.reporter.RollingFileReporter;

import java.util.concurrent.TimeUnit;

class IndexLogger {

    private CountingRecorder validLoginRecorder;
    private CountingRecorder invalidLoginRecorder;
    private CountingRecorder receivedMessageRecorder;
    private CountingRecorder ignoredMessageRecorder;
    private CountingRecorder forwardedMessageRecorder;

    IndexLogger(String filePattern, int periodSeconds) {
        new PerformanceMonitor(new RollingFileReporter(filePattern))
                .addRecorder(validLoginRecorder = new CountingRecorder("Valid login times"))
                .addRecorder(invalidLoginRecorder = new CountingRecorder("Invalid login times"))
                .addRecorder(receivedMessageRecorder = new CountingRecorder("Received messages"))
                .addRecorder(ignoredMessageRecorder = new CountingRecorder("Ignored messages"))
                .addRecorder(forwardedMessageRecorder = new CountingRecorder("Forwarded messages"))
                .start(periodSeconds, TimeUnit.SECONDS);
    }

    /**
     * 订阅用户尝试登陆 LoginAttemptEvent 事件, 该事件的类型为该函数的参数类型.
     * 监听用户尝试登录的行为, 事件中记录了该登陆是否成功,
     * 若登陆成功则增加 VALID_LOGIN 的次数, 否则增加 INVALID_LOGIN 次数.
     *
     * @param event 监听的事件类型
     */
    @Subscribe
    public void onLoginAttempt(LoginAttemptEvent event) {
        if (event.getSuccess()) {
            validLoginRecorder.record();
        } else {
            invalidLoginRecorder.record();
        }
    }

    /**
     * 订阅用户发送消息 MessageEvent 事件, 该事件的类型为该函数的参数类型.
     * 监听用户发送消息的行为, 事件中记录了该消息是否通过服务器认证, 认证成功表示消息被服务器接受, 否则被忽略.
     * 若成功则增加 RECEIVED_MESSAGE 的次数, 否则增加 IGNORED_MESSAGE 次数.
     *
     * @param event 监听的事件类型
     */
    @Subscribe
    public void onMessage(MessageEvent event) {
        if (event.isAuthenticated()) {
            receivedMessageRecorder.record();
        } else {
            ignoredMessageRecorder.record();
        }
    }

    /**
     * 订阅消息转发 MessageForwardEvent 事件, 该事件的类型为该函数的参数类型.
     * 监听群发消息的行为,
     * 每有一个已登录的客户端(除发送方), 都增加一次 FORWARDED_MESSAGE 的次数, .
     *
     * @param event 监听的事件类型
     */
    @Subscribe
    public void onForwardedMessage(MessageForwardEvent event) {
        forwardedMessageRecorder.record();
    }

}
