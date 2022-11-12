import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static final Map<String, String> users = new HashMap<>();
    public static List<ClientConnectionThread> connectedToServer = new LinkedList<>();
    public static final ServerLogger logger = ServerLogger.getInstance();
    private static final String PATH_TO_SETTINGS = "src/main/resources/settings.txt";

    public static void main(String[] args) {
        logger.log("Reading settings");
        final int port = readPort(PATH_TO_SETTINGS);
        logger.log("Connecting through port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                System.out.println("Waiting for connection...");
                logger.log("Waiting for connection...");
                Socket clientSocket = serverSocket.accept();
                logger.log("New connection accepted with user: " + clientSocket);
                System.out.println("New connection accepted");
                connectedToServer.add(new ClientConnectionThread(clientSocket));
                logger.log("User " + clientSocket + " was added to list of users connected to server");
            }
        } catch (IOException e) {
            logger.log("Exception in Server main: " + e.getMessage());
        }
    }

    public static int readPort(String path) {
        int port = 0;
        try (FileInputStream portFromFile = new FileInputStream(path)) {
            byte[] portInBytes = portFromFile.readAllBytes();
            String portRead = new String(portInBytes);
            port = Integer.parseInt(portRead);
        } catch (IOException e) {
            logger.log("Exception in Server readPort: " + e.getMessage());
        }
        if (port == 0) {
            System.out.println("Failed to read the port");
            logger.log("Failed to read the port");
        } else {
            logger.log("Port was successfully read from the file: " + port);
        }
        return port;
    }
}