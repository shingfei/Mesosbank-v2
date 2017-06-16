package nl.hro.rick.mesosbank.server;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Created by Rick on 24-3-2017.
 */
public class Server
{
    private static Database DB_INSTANCE;
    private int port = 8025;

    public Server() throws Exception
    {
        HttpServer server = initWebserver();
        server.start();
        while (true)
        {
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws Exception {
      DB_INSTANCE = (args.length == 1 && args[0].equals("--mock-db"))
              ? new MockDatabase()
              : new Databaseimpl();

      new Server();
    }

    private HttpServer initWebserver()
    {
        ResourceConfig config = new ResourceConfig(BankEndpoint.class);
        config.register(JacksonJaxbJsonProvider.class);
        URI uri = URI.create("http://0.0.0.0:" + port);


//        SSLContextConfigurator sslConf = new SSLContextConfigurator();
//        sslConf.setKeyStoreFile("meso.jks"); // contains server keypair
//        sslConf.setKeyStorePass("meso123");
//        sslConf.setTrustStoreFile("cacerts.jks"); // contains client certificate
//        sslConf.setTrustStorePass("meso123");

//        return GrizzlyHttpServerFactory.createHttpServer(uri, config, true, new SSLEngineConfigurator(sslConf));

        return GrizzlyHttpServerFactory.createHttpServer(uri,config, true);
    }

    public static Database getDatabase()
    {
        return DB_INSTANCE;
    }
}
