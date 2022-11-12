import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ServerTest {
    @Test
    public void readPortTest() throws IOException {
        String path = "src/test/java/readPortTest.txt";
        String port = "123456789";
        File file = new File(path);
        file.createNewFile();
        Files.write(Path.of(path), port.getBytes(), StandardOpenOption.WRITE);
        int portActual = Server.readPort(path);
        file.deleteOnExit();
        assert portActual == Integer.parseInt(port);
    }

    @Test
    public void readPortTest_WITH_NumberFormatException() throws IOException {
        String path = "src/test/java/readPortTest2.txt";
        String port = "Hello";
        File file = new File(path);
        file.createNewFile();
        file.deleteOnExit();
        Files.write(Path.of(path), port.getBytes(), StandardOpenOption.WRITE);
        Assertions.assertThrows(NumberFormatException.class, () -> Server.readPort(path));
    }
}
