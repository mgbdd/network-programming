package server;

import java.io.*;
import java.net.*;

public class Server {
    private static final String UPLOAD_DIR = "upload";

    public static void start(int port) {
        System.out.println("Server is listening on port " + port);
        new File(UPLOAD_DIR).mkdirs(); // Создаем директорию для загрузок

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                new Thread(new FileHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FileHandler implements Runnable {
        private Socket socket;

        public FileHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream dataIn = new DataInputStream(socket.getInputStream())) {
                String fileName = dataIn.readUTF();
                long fileSize = dataIn.readLong();
                File outputFile = new File(UPLOAD_DIR, fileName);
                long totalBytesRead = 0;

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    long startTime = System.nanoTime();
                    long lastOutputTime = System.nanoTime();
                    printSpeed(totalBytesRead, System.nanoTime() - startTime);

                    while (totalBytesRead < fileSize) {
                        int bytesRead = dataIn.read(buffer);
                        if (bytesRead == -1) break;

                        fos.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        long currentTime = System.nanoTime();
                        if (currentTime - lastOutputTime >= 3_000_000_000L) { // каждые 3 секунды
                            printSpeed(totalBytesRead, currentTime - startTime);
                            lastOutputTime = currentTime;
                        }
                    }

                    if (totalBytesRead == fileSize) {
                        System.out.println("File received successfully: " + fileName);
                        socket.getOutputStream().write("SUCCESS".getBytes());
                    } else {
                        System.out.println("File transfer incomplete: " + fileName);
                        socket.getOutputStream().write("FAIL".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void printSpeed(long totalBytesRead, long elapsedTime) {
            double elapsedSeconds = elapsedTime / 1_000_000_000.0;
            double currentSpeed = (totalBytesRead / 1024.0) / elapsedSeconds; // KB/s
            System.out.printf("Current speed: %.2f KB/s%n", currentSpeed);
        }

        private void printAverageSpeed(long totalBytesRead, long elapsedTime) {
            double elapsedSeconds = elapsedTime / 1_000_000_000.0;
            double averageSpeed = (totalBytesRead / 1024.0) / elapsedSeconds; // KB/s
            System.out.printf("Average speed: %.2f KB/s%n", averageSpeed);
        }
    }


}
