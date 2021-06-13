package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;
import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

import java.io.IOException;

public class CountGreaterThanGovernment extends GeneralCommand {
    private static final long serialVersionUID = 32L;
    transient private CommandReceiver commandReceiverImpl;

    @Inject
    public CountGreaterThanGovernment(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return String.format(commandReceiverImpl.getResourceBundle().getString("countGreaterThanGovernment"), "count_greater_than_government");
    }

    @Override
    protected void execute(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        if (args.length == 2) {
            commandReceiverImpl.count_greater_than_government(args[1]);
        }
        else {
            Messenger.printMessage("not_correct_args");
        }
    }

    @Override
    public String getName() {
        return "count_greater_than_government";
    }
}
