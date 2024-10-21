import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import static java.lang.Thread.sleep;

public class MulticastDiscoveryApp {
    private static final int PORT = 4446;
    private static final String MESSAGE = "are you a copy???";
    private final MulticastSocket multicastSocket;
    private final InetAddress multicastGroup;
    private final Set<InetAddress> discoveredCopies= new HashSet<>();

    public MulticastDiscoveryApp(String multicastAddress) throws IOException{
        multicastGroup = InetAddress.getByName(multicastAddress);
        multicastSocket = new MulticastSocket(PORT);
        multicastSocket.joinGroup(multicastGroup);
    }

    public void start() {
        // Запускаем поток для отправки multicast сообщений
        new Thread(this::sendDiscoveryMessage).start();

        // Запускаем поток для получения ответов от других копий
        new Thread(this::receiveResponses).start();
    }

    private void sendDiscoveryMessage() {
        try {
            while (true) {
                DatagramPacket sentPacket = new DatagramPacket(MESSAGE.getBytes(), MESSAGE.length(), multicastGroup, PORT);
                multicastSocket.send(sentPacket);
                sleep(5000); // Отправляем сообщение каждые 5 секунд
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receiveResponses() {
        try {
            byte[] buffer = new byte[256];
            while (true) {
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(receivedPacket);

                String received = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                if (received.equals(MESSAGE)) {
                    InetAddress senderAddress = receivedPacket.getAddress();
                    if (!discoveredCopies.contains(senderAddress)) {
                        discoveredCopies.add(senderAddress);
                        System.out.println("New copy discovered: " + senderAddress.getHostAddress());
                    }
                }

                displayCopies();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayCopies() {
        System.out.println("Active copies:");
        for (InetAddress address : discoveredCopies) {
            System.out.println(address.getHostAddress());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("wrong number of arguments!!");
            return;
        }

        try {
            MulticastDiscoveryApp app = new MulticastDiscoveryApp(args[0]);
            app.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
