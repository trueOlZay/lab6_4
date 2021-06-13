package CommandModule.SerializedCommand;

import java.io.Serializable;

public class SerializedLogOrReg implements Serializable {
    private static final long serialVersionUID = 32L;
    private String login;
    private String password;
    private String type;

    public SerializedLogOrReg(String login, String password, String type) {
        this.login = login;
        this.password = password;
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }
}
