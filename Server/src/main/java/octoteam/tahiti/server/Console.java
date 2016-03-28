package octoteam.tahiti.server;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import octoteam.tahiti.server.configuration.ServerConfiguration;
import octoteam.tahiti.server.repository.AccountRepository;
import octoteam.tahiti.server.repository.DatabaseAccountRepository;
import octoteam.tahiti.server.service.AccountService;
import octoteam.tahiti.shared.event.BaseEvent;
import octoteam.tahiti.shared.logging.LoggerUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Console {

    public static void main(String[] args) throws Exception {

        LoggerUtil.reset();

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
