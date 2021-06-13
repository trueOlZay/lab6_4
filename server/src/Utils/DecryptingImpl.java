package Utils;



import CommandModule.GeneralCommand;
import CommandModule.SerializedCommand.SerializedCommand;
import CommandModule.SerializedCommand.SerializedLogOrReg;
import CommandModule.SerializedCommand.SerializedMessage;
import CommandModule.ServerCommandReceiver;
import DataBase.DataBaseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class DecryptingImpl implements Decrypting{
    private final ServerCommandReceiver commandReceiver;
    private static final Logger logger = LoggerFactory.getLogger(DecryptingImpl.class);

    @Inject
    public DecryptingImpl(ServerCommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
    }

    public void decrypt(Object o, Socket socket) throws DataBaseException, IOException {
        if (o instanceof SerializedCommand) {
            logger.error(" дошли до decrypting");
            SerializedCommand serializedCommand = (SerializedCommand) o;
            logger.error(" дошли до decrypting 2");
            GeneralCommand command = serializedCommand.getCommand();
            logger.error(" дошли до decrypting 3");
            command.execute(serializedCommand, socket, commandReceiver);
        }

        if (o instanceof SerializedLogOrReg) {
            SerializedLogOrReg serializedLogOrReg = (SerializedLogOrReg) o;
            if (serializedLogOrReg.getType().equals("log")) {
                commandReceiver.tryRegister(serializedLogOrReg.getLogin(), serializedLogOrReg.getPassword(), socket);
            }
            if (serializedLogOrReg.getType().equals("reg")) {
                commandReceiver.register(serializedLogOrReg.getLogin(), serializedLogOrReg.getPassword(), socket);
            }
        }

        if (o instanceof SerializedMessage) {
            SerializedMessage serializedMessage = (SerializedMessage) o;
            if (serializedMessage.getMessage().equals("return_collection_init")) {
                commandReceiver.sendCollection(socket, "init");
            }

            else {
                commandReceiver.sendCollection(socket, "regular");
            }
        }
    }
}
