package Utils;

import DataBase.DataBaseException;

import java.io.IOException;
import java.net.Socket;

public interface Decrypting {
    void decrypt(Object o, Socket socket) throws DataBaseException, IOException;
}
