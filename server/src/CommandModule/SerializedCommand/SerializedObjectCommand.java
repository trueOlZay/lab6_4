package CommandModule.SerializedCommand;

import CommandModule.GeneralCommand;

import java.io.Serializable;

public class SerializedObjectCommand extends SerializedCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    private Object object;

    public SerializedObjectCommand(GeneralCommand command, Object object, String login, String password) {
        super(command, login, password);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

}
