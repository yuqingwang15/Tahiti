package octoteam.tahiti.client;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.client.event.LoginAttemptEvent;
import octoteam.tahiti.client.event.SendMessageEvent;
import octoteam.tahiti.shared.logging.StatisticsLogger;

public class Logger extends StatisticsLogger {

    public static final String SUCCESSFUL_LOGIN = "Successful login";
    public static final String FAILED_LOGIN = "Failed login";
    public static final String SEND_MESSAGE = "Send message";

    Logger(String filePath, int periodSeconds) {
        super(filePath, periodSeconds);
        clear(SUCCESSFUL_LOGIN);
        clear(FAILED_LOGIN);
        clear(SEND_MESSAGE);
    }

    @Subscribe
    public void onLogin(LoginAttemptEvent event) {
        if (event.isSuccess()) {
            increase(SUCCESSFUL_LOGIN);
        } else {
            increase(FAILED_LOGIN);
        }
    }

    @Subscribe
    public void onSendMessage(SendMessageEvent event) {
        increase(SEND_MESSAGE);
    }

}
