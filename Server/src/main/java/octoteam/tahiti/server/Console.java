package octoteam.tahiti.server;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import octoteam.tahiti.server.configuration.ServerConfiguration;
import octoteam.tahiti.server.model.Account;
import octoteam.tahiti.server.repository.AccountRepository;
import octoteam.tahiti.server.repository.DatabaseAccountRepository;
import octoteam.tahiti.server.service.AccountService;
import octoteam.tahiti.shared.event.BaseEvent;
import octoteam.tahiti.shared.logging.LoggerUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Console {

    public static void main(String[] args) throws Exception {

        LoggerUtil.reset();

        // Commandline Options
        Options options = new Options();
        options.addOption("a", "add", false, "Add user");
        options.addOption("u", "username", true, "Username");
        options.addOption("p", "password", true, "Password");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Read config
        Yaml yaml = new Yaml();
        ServerConfiguration config;
        InputStream in;
        try {
            in = new FileInputStream("resource/tahiti_server.yaml");
        } catch (FileNotFoundException e) {
            in = Console.class.getClass().getResourceAsStream("/tahiti_server.yaml");
        }
        config = yaml.loadAs(in, ServerConfiguration.class);

        // Open database connection
        ConnectionSource connectionSource = new JdbcConnectionSource(config.getDatabase());
        AccountRepository repository = new DatabaseAccountRepository(connectionSource);
        AccountService accountService = new AccountService(repository);

        if (cmd.hasOption("a")) {

            Account account = repository.lookupAccountByUsername(cmd.getOptionValue("u"));

            if (account != null) {
                System.out.println("Create user failed: Username exists.");
            } else {
                repository.createAccount(cmd.getOptionValue("u"), cmd.getOptionValue("p"));
                System.out.println("Create user succeeded.");
            }

            connectionSource.close();

        } else {

            // Create event bus
            EventBus serverEventBus = new EventBus();
            serverEventBus.register(new Logger(config.getLogFile(), 60));
            serverEventBus.register(new Object() {
                @Subscribe
                public void listenAllEvent(BaseEvent event) {
                    System.out.println(event);
                }
            });

            // Create server
            TahitiServer server = new TahitiServer(config, serverEventBus, accountService);

            server.run();

        }
    }

}
