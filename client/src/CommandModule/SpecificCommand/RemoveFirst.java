package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;

import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

import java.io.IOException;

public class RemoveFirst extends GeneralCommand {
    private static final long serialVersionUID = 32L;
    transient private CommandReceiver commandReceiverImpl;

    @Inject
    public RemoveFirst(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return String.format(commandReceiverImpl.getResourceBundle().getString("removeFirst"), "remove_first");
    }

    @Override
    protected void execute(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        if (args.length > 1) {
            Messenger.printMessage("not_correct_args");
        }
        commandReceiverImpl.remove_first();
    }

    @Override
    public String getName() {
        return "remove_first";
    }
}
