package CommandModule;

import java.util.HashMap;
import java.util.List;

public interface CommandInvoker {
    void execute(String[] commandName);

    HashMap<String, GeneralCommand> getCommandHashMap();

    List<String> getCommandsName();
}
