package client;

import java.io.*;
import java.net.*;

public class Client {
    public static void sendFile(String host, int port, String filePath) {
        try (Socket socket = new Socket(host, port);
             DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
             FileInputStream fis = new FileInputStream(filePath)) {

            File file = new File(filePath);
            String fileName = file.getName();
            long fileSize = file.length();

            dataOut.writeUTF(fileName);
            dataOut.writeLong(fileSize);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }

            byte[] response = new byte[7];
            socket.getInputStream().read(response);
            String status = new String(response).trim();

            if ("SUCCESS".equals(status)) {
                System.out.println("File transfer completed successfully.");
            } else {
                System.out.println("File transfer failed.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
