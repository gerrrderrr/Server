import org.junit.jupiter.api.Test;

public class ServerLoggerTest {

    @Test
    public void getInstanceTest() {
        ServerLogger logger = ServerLogger.getInstance();
        ServerLogger logger2 = ServerLogger.getInstance();
        assert logger == logger2;
    }
}
