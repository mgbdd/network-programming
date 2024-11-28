import Proxy.ProxyServer;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Not enough arguments: port is required");
            return;
        }

        int port = Integer.parseInt(args[0]);
        String host = "localhost";

        log.info("SOCKS5 proxy server started at " + host + ":" + port);
        ProxyServer proxy = new ProxyServer(host, port);
        proxy.run();
    }
}
