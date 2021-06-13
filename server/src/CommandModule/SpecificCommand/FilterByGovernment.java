package CommandModule.SpecificCommand;

import CommandModule.SerializedCommand.SerializedArgumentCommand;
import CommandModule.ServerCommandReceiver;
import CommandModule.GeneralCommand;
import DataBase.DataBaseException;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class FilterByGovernment extends GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    @Override
    public void execute(Object object, Socket socket, ServerCommandReceiver commandReceiver) throws IOException, DataBaseException {
        SerializedArgumentCommand command = (SerializedArgumentCommand) object;
        commandReceiver.filter_by_government(command, socket);
    }
}
