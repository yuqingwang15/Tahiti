package octoteam.tahiti.server;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.server.event.LoginAttemptEvent;
import octoteam.tahiti.server.event.MessageEvent;
import octoteam.tahiti.server.event.MessageForwardEvent;
import octoteam.tahiti.shared.logging.StatisticsLogger;

public class Logger extends StatisticsLogger {

    public static final String VALID_LOGIN = "Valid login";
    public static final String INVALID_LOGIN = "Invalid login";
    public static final String RECEIVED_MESSAGE = "Received message";
    public static final String IGNORED_MESSAGE = "Ignored message";
    public static final String FORWARDED_MESSAGE = "Forwarded message";

    public Logger(String filePath, int periodSeconds) {
        super(filePath, periodSeconds);
        clear(VALID_LOGIN);
        clear(INVALID_LOGIN);
        clear(RECEIVED_MESSAGE);
        clear(IGNORED_MESSAGE);
        clear(FORWARDED_MESSAGE);
    }

    @Subscribe
    public void onLoginAttempt(LoginAttemptEvent event) {
        if (event.getSuccess()) {
            increase(VALID_LOGIN);
        } else {
            increase(INVALID_LOGIN);
        }
    }

    @Subscribe
    public void onMessage(MessageEvent event) {
        if (event.isAuthenticated()) {
            increase(RECEIVED_MESSAGE);
        } else {
            increase(IGNORED_MESSAGE);
        }
    }

    @Subscribe
    public void onForwardedMessage(MessageForwardEvent event) {
        increase(FORWARDED_MESSAGE);
    }

}
