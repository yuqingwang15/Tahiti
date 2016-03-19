package octoteam.tahiti.server.event;

public class LoginEvent extends BaseEvent {

    private Boolean success;

    private String username;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginEvent(Boolean success, String username) {
        this.success = success;
        this.username = username;
    }
}
