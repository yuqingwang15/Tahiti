package octoteam.tahiti.server;

import octoteam.tahiti.server.configuration.ServerConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class Console {

    public static void main(String[] args) throws Exception {

        Yaml yaml = new Yaml();
        InputStream in = Console.class.getClass().getResourceAsStream("/tahiti_server.yaml");
        ServerConfiguration config = yaml.loadAs(in, ServerConfiguration.class);

        TahitiServer server = new TahitiServer(config);
        server.run();

    }

}
