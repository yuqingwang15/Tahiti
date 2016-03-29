package octoteam.tahiti.server.service;

import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.repository.AccountRepository;

public interface AccountService {
    /**
     * 返回持有的用户集合
     *
     * @return 用户集合 DatabaseAccountRepository 或 MemoryAccountRepository 中的一种
     */
    AccountRepository getAccountRepository();

    /**
     * 根据用户名及密码在用户集合内中查找用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回查找到的用户
     * @throws AccountNotFoundException
     * @throws AccountNotMatchException
     */
    Account getMatchedAccount(String username, String password)
            throws AccountNotFoundException, AccountNotMatchException;
}
