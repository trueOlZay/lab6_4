package CommandModule.SpecificCommand;

import CommandModule.ServerCommandReceiver;
import CommandModule.GeneralCommand;
import CommandModule.SerializedCommand.SerializedCombinedCommand;
import DataBase.DataBaseException;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Update extends GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    @Override
    public void execute(Object object, Socket socket, ServerCommandReceiver commandReceiver) throws IOException, DataBaseException {
        SerializedCombinedCommand command = (SerializedCombinedCommand) object;
        commandReceiver.update(command, socket);
    }
}
