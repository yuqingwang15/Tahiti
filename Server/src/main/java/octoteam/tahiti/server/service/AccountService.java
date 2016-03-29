package octoteam.tahiti.server.service;

import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.repository.AccountRepository;

public class AccountService {

    private AccountRepository accountRepository;

    /**
     * 构造方法，获得一个用户集合
     *
     * @param accountRepository 用户集合 DatabaseAccountRepository 或 MemoryAccountRepository 中的一种
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * 返回持有的用户集合
     *
     * @return 用户集合 DatabaseAccountRepository 或 MemoryAccountRepository 中的一种
     */
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    /**
     * 根据用户名及密码在用户集合内中查找用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回查找到的用户
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
