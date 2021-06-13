package CommandModule.SerializedCommand;

import CommandModule.GeneralCommand;

import java.io.Serializable;

public class SerializedArgumentCommand extends SerializedCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    private String arg;

    public SerializedArgumentCommand(GeneralCommand command, String arg, String login, String password) {
        super(command, login, password);
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }
}
