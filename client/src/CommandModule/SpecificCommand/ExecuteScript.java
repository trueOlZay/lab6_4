package CommandModule.SpecificCommand;

import CommandModule.CommandReceiver;
import CommandModule.GeneralCommand;
import MessengerUtils.Messenger;
import com.google.inject.Inject;

public class ExecuteScript extends GeneralCommand {
    private static final long serialVersionUID = 32L;
    private static String path;
    transient private CommandReceiver commandReceiverImpl;

    @Inject
    public ExecuteScript(CommandReceiver commandReceiverImpl) {
        this.commandReceiverImpl = commandReceiverImpl;
    }

    @Override
    protected String displayInfo() {
        return String.format(commandReceiverImpl.getResourceBundle().getString("executeScriptInf"), "execute_script");
    }

    @Override
    protected void execute(String[] args) {
        try {
            if (args.length == 2) { path = args[1]; commandReceiverImpl.execute_script(args[1]); }
            else { Messenger.printMessage("not_correct_args"); }
        } catch (StackOverflowError e) {
            Messenger.printMessage("stack_err");
        }
    }

    public static String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return "execute_script";
    }
}
