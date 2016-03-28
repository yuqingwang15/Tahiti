package octoteam.tahiti.server.repository;

import octoteam.tahiti.server.model.Account;

import java.util.HashMap;

/**
 * TODO
 */
public class MemoryAccountRepository implements AccountRepository {

    private HashMap<String, Account> accounts = new HashMap<>();

    @Override
    public Account lookupAccountByUsername(String username) {
        return accounts.get(username);
    }

    @Override
    public Account createAccount(String username, String password) {
        Account account = new Account(username, password);
        account.assignId(accounts.size() + 1);
        accounts.put(username, account);
        return account;
    }

}
