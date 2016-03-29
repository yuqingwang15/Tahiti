package octoteam.tahiti.server.configuration;

import com.google.common.base.MoreObjects;

/**
 * 服务端配置信息<br>
 * 数据库<br>
 * 日志文件<br>
 * Socket ip 及端口配置<br>
 * RateLimit 相关配置（次数限制及频率限制）<br>
 */
public class ServerConfiguration {

    private ChatServiceConfiguration chatService;

    private String database;

    private String logFile;

    private RateLimitConfiguration rateLimit;

    public ChatServiceConfiguration getChatService() {
        return chatService;
    }

    public void setChatService(ChatServiceConfiguration chatService) {
        this.chatService = chatService;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public RateLimitConfiguration getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimitConfiguration rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("chatService", chatService)
                .add("database", database)
                .add("logFile", logFile)
                .add("rateLimit", rateLimit)
                .toString();
    }

}
