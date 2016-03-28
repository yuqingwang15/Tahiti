package octoteam.tahiti.server.service;

import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.repository.AccountRepository;

public class AccountService {

    private AccountRepository accountRepository;

    /**
     * TODO
     *
     * @param accountRepository
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * TODO
     *
     * @return
     */
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    /**
     * TODO
     *
     * @param username
     * @param password
     * @return
     * @throws AccountNotFoundException
     * @throws AccountNotMatchException
     */
    public Account getMatchedAccount(String username, String password)
            throws AccountNotFoundException, AccountNotMatchException {
        Account account = accountRepository.lookupAccountByUsername(username);
        if (account == null) throw new AccountNotFoundException();
        if (!account.isPasswordMatches(password)) throw new AccountNotMatchException();
        return account;
    }

}
