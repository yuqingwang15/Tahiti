package octoteam.tahiti.shared.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsLogger {

    private final Logger logger = (Logger) LoggerFactory.getLogger(StatisticsLogger.class);

    private HashMap<String, AtomicInteger> counters = new HashMap<>();

    private void initLogger(String filePath) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("File");
        fileAppender.setFile(filePath);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setImmediateFlush(true);
        encoder.setPattern("%d %-5level - %msg%n");
        encoder.start();

        fileAppender.setEncoder(encoder);
        fileAppender.start();

        logger.setLevel(Level.ALL);
        logger.addAppender(fileAppender);
    }

    private void initScheduler(int periodSeconds) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(this::logStatistics, periodSeconds, periodSeconds, TimeUnit.SECONDS);
    }

    private void logStatistics() {
        counters.forEach((key, value) -> logger.info(String.format("Statistics: %s: %d", key, value.get())));
        logger.info("------------------");
    }

    public StatisticsLogger(String filePath, int periodSeconds) {
        initLogger(filePath);
        initScheduler(periodSeconds);
    }

    protected void clear(String key) {
        counters.put(key, new AtomicInteger());
    }

    protected int increase(String key) {
        return counters.get(key).incrementAndGet();
    }

}
