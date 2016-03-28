package octoteam.tahiti.server.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import octoteam.tahiti.server.model.Account;

import java.sql.SQLException;

/**
 * TODO
 */
public class DatabaseAccountRepository implements AccountRepository {

    private Dao<Account, Integer> accountDAO;

    /**
     * TODO
     *
     * @param connectionSource
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
