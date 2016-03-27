package octoteam.tahiti.server.repository;

import octoteam.tahiti.server.model.Account;
import org.apache.commons.lang3.NotImplementedException;

public class DatabaseAccountRepository implements AccountRepository {

    @Override
    public Account lookupAccountByUsername(String username) {
        throw new NotImplementedException("not implemented");
    }

    @Override
    public Account createAccount(String username, String password) {
        throw new NotImplementedException("not implemented");
    }

}
