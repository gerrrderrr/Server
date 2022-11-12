import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class ClientConnectionThread extends Thread {
    private final Socket socket;
    private String name;
    private String id;
    protected final PrintWriter out;
    private final BufferedReader in;

    public ClientConnectionThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        start();
    }

    @Override
    public void run() {
        setNickname();
        addUserToTheList(id, name);
        while (!socket.isClosed()) {
            String message = null;
            try {
                message = in.readLine();
            } catch (IOException ignored) {
            }
            if (message == null) {
                exit();
                break;
            } else if (message.equalsIgnoreCase("end")) {
                Server.logger.log("User " + Thread.currentThread().getName() + " exited the chat");
                out.println("Bye Bye");
                exit();
                break;
            } else {
                for (ClientConnectionThread user : Server.connectedToServer) {
                    user.send(message);
                }
            }
        }
    }

    protected void send(String message) {
        out.println(Thread.currentThread().getName() + ": " + message + "\n");
        Server.logger.log(Thread.currentThread().getName() + " sent a message to the chat: " + message);
    }

    private void setNickname() {
        if (!userExist()) {
            this.name = createNewUser();
            Thread.currentThread().setName(name);
        } else {
            this.name = returnExistingUser(id);
        }
    }

    public boolean userExist() {
        try {
            String idFromUser = in.readLine();
            if (!idFromUser.equals("No Id") && Server.users.containsKey(idFromUser)) {
                this.id = idFromUser;
                out.println("User exist");
                return true;
            } else if (!idFromUser.equals("No Id") && !Server.users.containsKey(idFromUser)) {
                out.println("User doesn't exist");
                return false;
            } else {
                return false;
            }
        } catch (IOException e) {
            Server.logger.log("Exception in ClientConnectionThread userExist: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean nameExist(String userName) {
        return Server.users.containsValue(userName);
    }

    private String createNewUser() {
        out.println(generateId());
        while (true) {
            out.println("Please enter your name");
            try {
                String nickname = in.readLine();
                if (!nameExist(nickname)) {
                    out.println(String.format("Hi %s, welcome to the chat", nickname));
                    Server.logger.log("User " + nickname + " created nickname");
                    return nickname;
                } else {
                    out.println("User with this nickname already exists, try another one");
                }
            } catch (IOException e) {
                Server.logger.log("Exception in ClientConnectionThread createNewUser: " + e.getMessage());
            }
        }
    }

    private String returnExistingUser(String userId) {
        String nickname = Server.users.get(userId);
        out.println(String.format("Hi %s, welcome to the chat", nickname));
        Server.logger.log("User " + nickname + " was in system");
        return nickname;
    }

    public String generateId() {
        while (true) {
            this.id = UUID.randomUUID().toString();
            if (!Server.users.containsKey(id)) {
                Server.logger.log("Id was generated: " + id);
                return id;
            }
        }
    }

    public void addUserToTheList(String userId, String userName) {
        Server.users.put(userId, userName);
        Server.logger.log("User " + userName + " was added yo list of users");
    }

    private void exit() {
        try {
            socket.close();
            Server.logger.log("Socket closed");
            in.close();
            Server.logger.log("BufferedReader closed");
            out.close();
            Server.logger.log("PrintWriter closed");
        } catch (IOException e) {
            Server.logger.log("Exception in ClientConnectionThread exit: " + e.getMessage());
        }
        Server.connectedToServer.removeIf(this::equals);
        this.interrupt();
    }
}
