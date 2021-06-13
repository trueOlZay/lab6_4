package CommandModule;

import DataBase.DataBaseException;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public abstract class GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    public abstract void execute(Object object, Socket socket, ServerCommandReceiver commandReceiver) throws IOException, DataBaseException;
}
