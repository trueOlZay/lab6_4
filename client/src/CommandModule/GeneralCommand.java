package CommandModule;

import java.io.IOException;
import java.io.Serializable;

public abstract class GeneralCommand implements Serializable {
    private static final long serialVersionUID = 32L;
    protected abstract String displayInfo();
    protected abstract void execute(String[] args) throws InterruptedException, IOException, ClassNotFoundException;
    public abstract String getName();
}
