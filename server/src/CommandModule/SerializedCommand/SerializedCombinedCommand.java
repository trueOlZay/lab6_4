package CommandModule.SerializedCommand;

import CommandModule.GeneralCommand;

import java.io.Serializable;

public class SerializedCombinedCommand extends SerializedCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    private Object object;
    private String arg;

    public SerializedCombinedCommand(GeneralCommand command, Object object, String arg, String login, String password) {
        super(command, login, password);
        this.object = object;
        this.arg = arg;
    }

    public Object getObject() {
        return object;
    }

    public String getArg() {
        return arg;
    }
}
