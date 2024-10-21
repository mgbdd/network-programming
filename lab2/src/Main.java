import client.Client;
import server.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("not enough arguments");
            return;
        }

        String mode = args[0];

        if ("server".equalsIgnoreCase(mode)) {
            int port = Integer.parseInt(args[1]);
            Server.start(port);
        } else if ("client".equalsIgnoreCase(mode)) {
            String host = args[1];
            int port = Integer.parseInt(args[2]);  // Получаем номер порта из аргументов
            String filePath = args[3];  // Путь к файлу
            Client.sendFile(host, port, filePath);
        } else {
            System.err.println("Invalid mode. Use 'server' or 'client'.");
        }
    }
}
