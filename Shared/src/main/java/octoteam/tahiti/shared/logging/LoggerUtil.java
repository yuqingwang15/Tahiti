package octoteam.tahiti.shared.logging;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    /**
     * 在服务端/客户端启动时重置日志系统的锚点.
     */
    public static void reset() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
    }

}
