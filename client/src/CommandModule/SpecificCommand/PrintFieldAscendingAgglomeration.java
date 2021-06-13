package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;
import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

import java.io.IOException;

public class PrintFieldAscendingAgglomeration extends GeneralCommand {
    private static final long serialVersionUID = 32L;
    transient private CommandReceiver commandReceiverImpl;

    @Inject
    public PrintFieldAscendingAgglomeration(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return String.format(commandReceiverImpl.getResourceBundle().getString("printFieldAscendingAgglomeration"), "print_field_ascending_agglomeration");
    }

    @Override
    protected void execute(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        if (args.length > 1) {
            Messenger.printMessage("not_correct_args");
        }
        commandReceiverImpl.print_field_ascending_agglomeration();
    }

    @Override
    public String getName() {
        return "print_field_ascending_agglomeration";
    }
}
