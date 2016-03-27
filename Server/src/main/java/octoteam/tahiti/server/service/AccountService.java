package octoteam.tahiti.server.service;

import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.repository.AccountRepository;

public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public Account getMatchedAccount(String username, String password)
            throws AccountNotFoundException, AccountNotMatchException {
        Account account = accountRepository.lookupAccountByUsername(username);
        if (account == null) throw new AccountNotFoundException();
        if (!account.isPasswordMatches(password)) throw new AccountNotMatchException();
        return account;
    }

}
