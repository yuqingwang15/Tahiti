package octoteam.tahiti.server.configuration;

import com.google.common.base.MoreObjects;

import java.util.List;

public class ServerConfiguration {

    private ChatServiceConfiguration chatService;

    private String database;

    private String logging;

    private List<AccountConfiguration> accounts;

    public List<AccountConfiguration> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountConfiguration> accounts) {
        this.accounts = accounts;
    }

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
        return MoreObjects.toStringHelper(this)
                .add("chatService", chatService)
                .add("database", database)
                .add("logging", logging)
                .add("accounts", accounts)
                .toString();
    }
}
