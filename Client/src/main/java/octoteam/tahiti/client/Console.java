package octoteam.tahiti.client;

import com.google.common.eventbus.EventBus;
import octoteam.tahiti.client.configuration.ClientConfiguration;
import octoteam.tahiti.client.ui.Reactor;
import octoteam.tahiti.client.ui.Renderer;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Console {

    public static void main(String[] args) throws Exception {

        // Read config
        Yaml yaml = new Yaml();
        ClientConfiguration config;
        InputStream in;
        try {
            in = new FileInputStream("resource/tahiti_client.yaml");
        } catch (FileNotFoundException e) {
            in = Console.class.getClass().getResourceAsStream("/tahiti_client.yaml");
        }
        config = yaml.loadAs(in, ClientConfiguration.class);

        // Create event bus
        EventBus clientEventBus = new EventBus();

        TahitiClient client = new TahitiClient(config, clientEventBus);
        Renderer renderer = new Renderer(clientEventBus);
        Reactor reactor = new Reactor(client, renderer);

        clientEventBus.register(reactor);
        clientEventBus.register(new Logger(config.getLogFile(), 60));

        renderer.actionShowLoginDialog();

    }

}
