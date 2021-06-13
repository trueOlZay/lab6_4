package CommandModule.SpecificCommand;

import CommandModule.SerializedCommand.SerializedCommand;
import CommandModule.ServerCommandReceiver;
import CommandModule.GeneralCommand;
import DataBase.DataBaseException;
import Utils.DecryptingImpl;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Clear extends GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    private static final Logger logger = LoggerFactory.getLogger(DecryptingImpl.class);

    @Override
    public void execute(Object object, Socket socket, ServerCommandReceiver commandReceiver) throws IOException, DataBaseException {
        logger.error("clear1");
        SerializedCommand command = (SerializedCommand) object;
        logger.error("clear2");
        commandReceiver.clear(command, socket);
    }
}
