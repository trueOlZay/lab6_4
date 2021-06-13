package CommandModule.SerializedCommand;

import java.io.Serializable;

public class SerializedReAuth implements Serializable {
    private static final long serialVersionUID = 32L;
    private boolean result;
    private String type;

    public SerializedReAuth(boolean result, String type) {
        this.result = result;
        this.type = type;
    }

    public boolean getResult() {
        return result;
    }

    public String getType() {
        return type;
    }
}
