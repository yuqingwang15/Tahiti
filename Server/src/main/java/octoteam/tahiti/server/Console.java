package octoteam.tahiti.server;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.server.configuration.ServerConfiguration;
import octoteam.tahiti.server.event.BaseEvent;
import octoteam.tahiti.server.repository.AccountRepository;
import octoteam.tahiti.server.repository.MemoryAccountRepository;
import octoteam.tahiti.server.service.AccountService;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;


public class Console {

    public static void main(String[] args) throws Exception {

        Yaml yaml = new Yaml();
        InputStream in = Console.class.getClass().getResourceAsStream("/tahiti_server.yaml");
        ServerConfiguration config = yaml.loadAs(in, ServerConfiguration.class);

        // TODO: replace to DatabaseAccountRepository
        AccountRepository repository = new MemoryAccountRepository();
        AccountService accountService = new AccountService(repository);
        config.getAccounts().forEach(account -> repository.createAccount(account.getUsername(), account.getPassword()));

        EventBus serverEventBus = new EventBus();

        TahitiServer server = new TahitiServer(config, serverEventBus, accountService);
        serverEventBus.register(new Logging());
        serverEventBus.register(new Object() {
            @Subscribe
            public void listenAllEvent(BaseEvent event) {
                System.out.println(event);
            }
        });

        server.run();

    }

}
