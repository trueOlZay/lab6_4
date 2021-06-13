package Connection;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ClientConnectionManager {
    void closeConnection() throws IOException;
    SocketChannel getSocketChannel();
}
