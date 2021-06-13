package Connection;

import java.io.IOException;

public interface Sender {
    void send(Object o) throws IOException;
}
