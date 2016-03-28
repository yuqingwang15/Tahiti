package octoteam.tahiti.shared.logging;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    public static void reset() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
    }

}
