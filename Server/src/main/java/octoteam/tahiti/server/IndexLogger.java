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

    private static final String VALID_LOGIN = "VALID_LOGIN";
    private static final String INVALID_LOGIN = "INVALID_LOGIN";
    private static final String RECEIVED_MESSAGE = "RECEIVED_MESSAGE";
    private static final String IGNORED_MESSAGE = "IGNORED_MESSAGE";
    private static final String FORWARDED_MESSAGE = "FORWARDED_MESSAGE";

    private PerformanceMonitor monitor;

    IndexLogger(String filePattern, int periodSeconds) {
        monitor = new PerformanceMonitor(
                new RollingFileReporter(filePattern),
                periodSeconds,
                TimeUnit.SECONDS
        );
        monitor.addRecorder(VALID_LOGIN, new CountingRecorder("Valid login times"));
        monitor.addRecorder(INVALID_LOGIN, new CountingRecorder("Invalid login times"));
        monitor.addRecorder(RECEIVED_MESSAGE, new CountingRecorder("Received messages"));
        monitor.addRecorder(IGNORED_MESSAGE, new CountingRecorder("Ignored messages"));
        monitor.addRecorder(FORWARDED_MESSAGE, new CountingRecorder("Forwarded messages"));
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
            monitor.record(VALID_LOGIN);
        } else {
            monitor.record(INVALID_LOGIN);
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
            monitor.record(RECEIVED_MESSAGE);
        } else {
            monitor.record(IGNORED_MESSAGE);
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
        monitor.record(FORWARDED_MESSAGE);
    }

}
