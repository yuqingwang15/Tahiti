package octoteam.tahiti.server.session;

import com.google.common.base.MoreObjects;
import octoteam.tahiti.server.model.Account;

/**
 * TODO
 */
public class Credential {

    /**
     * TODO
     */
    private int UID;

    /**
     * TODO
     */
    private String username;

    /**
     * TODO
     */
    private Account account;

    /**
     * TODO
     */
    private boolean authenticated;

    /**
     * TODO
     *
     * @param UID
     * @param username
     * @param authenticated
     */
    public Credential(int UID, String username, boolean authenticated) {
        this.UID = UID;
        this.username = username;
        this.account = null;
        this.authenticated = authenticated;
    }

    /**
     * 由 Account Model 构造一个标记为已登录的认证信息
     *
     * @param account
     */
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

    /**
     * TODO
     *
     * @return
     */
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

    /**
     * 获得一个表示游客的认证信息
     *
     * @return 游客认证信息
     */
    public static Credential getGuestCredential() {
        return new Credential(0, "Guest", false);
    }

}
