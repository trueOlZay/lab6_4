package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;
import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

import java.io.IOException;

public class Info extends GeneralCommand {
    private static final long serialVersionUID = 32L;
    transient private CommandReceiver commandReceiverImpl;

    @Inject
    public Info(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return String.format(commandReceiverImpl.getResourceBundle().getString("infoInf"), "info");
    }

    @Override
    protected void execute(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        if (args.length > 1) {
            Messenger.printMessage("not_correct_args");
        }
        commandReceiverImpl.info();
    }

    @Override
    public String getName() {
        return "info";
    }
}
