package octoteam.tahiti.server.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import octoteam.tahiti.server.model.Account;

import java.sql.SQLException;

/**
 * 用于操作数据库中的用户表
 */
public class DatabaseAccountRepository implements AccountRepository {

    private Dao<Account, Integer> accountDAO;

    /**
     * 初始化程序与用户表之间的链接
     * @param connectionSource 数据库连接
     * @throws Exception
     */
    public DatabaseAccountRepository(ConnectionSource connectionSource) throws Exception {
        accountDAO = DaoManager.createDao(connectionSource, Account.class);
        try {
            TableUtils.createTable(connectionSource, Account.class);
        } catch (SQLException ignored) {
        }
    }

    @Override
    public Account lookupAccountByUsername(String username) {
        try {
            QueryBuilder<Account, Integer> statementBuilder = accountDAO.queryBuilder();
            statementBuilder.where().eq("username", username);
            Account account = accountDAO.queryForFirst(statementBuilder.prepare());
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Account createAccount(String username, String password) {
        try {
            Account account = new Account(username, password);
            accountDAO.create(account);
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
