package octoteam.tahiti.server.configuration;

public class ServerConfiguration {

    private ChatServiceConfiguration chatService;

    private String database;

    private String logging;

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

    public String getLogging() {
        return logging;
    }

    public void setLogging(String logging) {
        this.logging = logging;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("chatService", chatService)
                .add("database", database)
                .add("logging", logging)
                .toString();
    }
}
