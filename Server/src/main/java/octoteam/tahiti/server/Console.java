package octoteam.tahiti.server;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.server.configuration.ServerConfiguration;
import octoteam.tahiti.server.event.BaseEvent;
import org.yaml.snakeyaml.Yaml;
import java.util.Timer;
import java.io.InputStream;

public class Console {

    public static void main(String[] args) throws Exception {

        Yaml yaml = new Yaml();
        InputStream in = Console.class.getClass().getResourceAsStream("/tahiti_server.yaml");
        ServerConfiguration config = yaml.loadAs(in, ServerConfiguration.class);

        TahitiServer server = new TahitiServer(config);
        server.getEventBus().register(new Logging());
        server.getEventBus().register(new Object() {
            @Subscribe
            public void listenAllEvent(BaseEvent event) {
                System.out.println(event);
            }
        });

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new LoggingPerMinTask(), 60*1000, 60*1000); //after 60s, count for per 60s
        server.run();


    }

}
