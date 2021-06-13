package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;
import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

import java.io.IOException;

public class Register extends GeneralCommand {
    private static final long serialVersionUID = 32L;

    transient private final CommandReceiver commandReceiverImpl;

    @Inject
    public Register(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return "";
    }

    @Override
    protected void execute(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        if (args.length == 3) {
            commandReceiverImpl.register(args[1], args[2]);
        }
        else {
            System.out.println("Некорректное количество аргументов.");
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
