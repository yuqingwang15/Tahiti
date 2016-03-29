package octoteam.tahiti.server.service;

import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.repository.AccountRepository;

public class DefaultAccountService implements AccountService {

    private AccountRepository accountRepository;

    /**
     * 构造方法，获得一个用户集合
     *
     * @param accountRepository 用户集合 DatabaseAccountRepository 或 MemoryAccountRepository 中的一种
     */
    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    @Override
    public Account getMatchedAccount(String username, String password)
            throws AccountNotFoundException, AccountNotMatchException {
        Account account = accountRepository.lookupAccountByUsername(username);
        if (account == null) throw new AccountNotFoundException();
        if (!account.isPasswordMatches(password)) throw new AccountNotMatchException();
        return account;
    }

}
