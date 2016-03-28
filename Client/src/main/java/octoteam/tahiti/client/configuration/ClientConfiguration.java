package octoteam.tahiti.client.configuration;

import com.google.common.base.MoreObjects;

public class ClientConfiguration {

    private ChatServiceConfiguration chatService;

    private String logFile;

    public ChatServiceConfiguration getChatService() {
        return chatService;
    }

    public void setChatService(ChatServiceConfiguration chatService) {
        this.chatService = chatService;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("chatService", chatService)
                .add("logFile", logFile)
                .toString();
    }
}
