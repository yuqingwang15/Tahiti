package octoteam.tahiti.server.repository;

import octoteam.tahiti.server.model.Account;

public interface AccountRepository {

    public Account lookupAccountByUsername(String username);

    public Account createAccount(String username, String password);

}
