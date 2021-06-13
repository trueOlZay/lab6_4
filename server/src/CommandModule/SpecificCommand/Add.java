package CommandModule.SpecificCommand;

import CommandModule.SerializedCommand.SerializedObjectCommand;
import CommandModule.ServerCommandReceiver;
import CommandModule.GeneralCommand;
import DataBase.DataBaseException;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Add extends GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    @Override
    public void execute(Object object, Socket socket, ServerCommandReceiver commandReceiver) throws IOException, DataBaseException {
        SerializedObjectCommand command = (SerializedObjectCommand) object;
        commandReceiver.add(command, socket);
    }
}
