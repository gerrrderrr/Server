import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnectionThreadTest {

    ServerSocket serverSocket = new ServerSocket(8888);
    Socket clientSocket = new Socket("localHost", 8888);

    Socket socket = serverSocket.accept();
    ClientConnectionThread connectionThread = new ClientConnectionThread(clientSocket);

    public ClientConnectionThreadTest() throws IOException {
    }

    @Test
    public void addUserToTheListTest() {
        String id = "123456789";
        String name = "User name";
        connectionThread.addUserToTheList(id, name);
        assert Server.users.containsKey(id);
        assert Server.users.containsValue(name);
    }

    @Test
    public void generateIdTest() throws IOException {
        String id = connectionThread.generateId();
        Assertions.assertNotNull(id);
    }
}
