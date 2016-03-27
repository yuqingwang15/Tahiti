package octoteam.tahiti.server.session;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.server.model.Account;

public class Credential {

    private int UID;

    private String username;

    private Account account;

    private boolean authenticated;

    public Credential(int UID, String username, boolean authenticated) {
        this.UID = UID;
        this.username = username;
        this.account = null;
        this.authenticated = authenticated;
    }

    public Credential(Account account) {
        this.UID = account.getId();
        this.username = account.getUsername();
        this.account = account;
        this.authenticated = true;
    }

    public int getUID() {
        return UID;
    }

    public String getUsername() {
        return username;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("UID", UID)
                .add("username", username)
                .add("account", account)
                .add("authenticated", authenticated)
                .toString();
    }

    public static Credential getGuestCredential() {
        return new Credential(0, "Guest", false);
    }

}
