package CommandModule;

import CommandModule.SpecificCommand.Add;
import MessengerUtils.Messenger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.*;

@Singleton
public class CommandInvokerImpl implements CommandInvoker{
    private final HashMap<String, GeneralCommand> commandHashMap = new HashMap<>();

    @Inject
    public CommandInvokerImpl(Set<GeneralCommand> commandSet) {
        register(commandSet.toArray(new GeneralCommand[0]));
        System.out.println(commandHashMap);
    }

    public void register(GeneralCommand... command) {
        Arrays.stream(command).forEach(command1 -> commandHashMap.put(command1.getName(), command1));
    }

    public void execute(String[] commandName) {
        try {
            if (commandName.length > 0 && !commandName[0].equals("")) {
                GeneralCommand command = commandHashMap.get(commandName[0]);
                System.out.println(command.toString());
                System.out.println("command Invoker");
                command.execute(commandName);
            }
            else {
                System.out.println("Вы не ввели команду");
            }
        } catch (NullPointerException e) {
            System.out.println("не существует команды " + commandName[0] + ". Для справки введите help.");
        } catch (IllegalStateException | IOException | ClassNotFoundException | InterruptedException ex) {
            if (ex.getMessage().equals("Connection reset by peer")) {
                System.out.println("Сервер умер, почему не знаю.");
                System.exit(0);
            }
        }
    }

    public HashMap<String, GeneralCommand> getCommandHashMap() {
        return commandHashMap;
    }

    public List<String> getCommandsName() {
        ArrayList<String> names = new ArrayList<>();
        commandHashMap.forEach((key, value) -> names.add(key));
        return names;
    }
}
