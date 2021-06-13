package CommandModule.SerializedCommand;

import CommandModule.GeneralCommand;

import java.io.Serializable;

public class SerializedCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    private GeneralCommand command;
    private String login;
    private String password;

    public SerializedCommand(GeneralCommand command, String login, String password) {
        this.command = command;
        this.login = login;
        this.password = password;
    }

    public GeneralCommand getCommand() {
        return command;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
