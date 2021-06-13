package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;
import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

import java.io.Serializable;

public class Help extends GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    transient private CommandReceiver commandReceiverImpl;

    @Inject
    public Help(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return String.format(commandReceiverImpl.getResourceBundle().getString("helpInf"), "help");
    }

    @Override
    protected void execute(String[] args) {
        if (args.length > 1) {
            Messenger.printMessage("not_correct_args");
        }
        System.out.println("help ");
        commandReceiverImpl.help();
    }

    @Override
    public String getName() {
        return "help";
    }
}
