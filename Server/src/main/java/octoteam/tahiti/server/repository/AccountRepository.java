package octoteam.tahiti.server.repository;

import octoteam.tahiti.server.model.Account;

public interface AccountRepository {

    /**
     * TODO
     *
     * @param username
     * @return
     */
    public Account lookupAccountByUsername(String username);

    /**
     * TODO
     *
     * @param username
     * @param password
     * @return
     */
    public Account createAccount(String username, String password);

}
