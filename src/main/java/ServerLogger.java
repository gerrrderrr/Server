import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {
    private static ServerLogger logger;
    private static final String PATH_TO_LOG = "src/main/resources/File.log";

    private ServerLogger() {
    }

    public void log(String msg) {
        Path path = Path.of(PATH_TO_LOG);
        if (Files.notExists(path)) {
            if (createNewFile()) {
                System.out.println("File.log was successfully created");
            }
        }
        String message = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss, dd.MM.yyyy")) +
                "] : " + msg + "\n";
        try {
            Files.write(path, message.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Caught exception in ServerLogger log: " + e.getMessage());
        }
    }

    public static ServerLogger getInstance() {
        if (logger == null) {
            logger = new ServerLogger();
        }
        return logger;
    }

    private boolean createNewFile() {
        try {
            return new File(PATH_TO_LOG).createNewFile();
        } catch (IOException e) {
            System.out.println("Caught exception in ServerLogger createNewFile: " + e.getMessage());
        }
        return false;
    }
}
