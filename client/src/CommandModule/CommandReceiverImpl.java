package CommandModule;

import BaseClass.City;
import BaseClass.Human;
import CommandModule.SerializedCommand.*;
import CommandModule.SpecificCommand.*;
import Connection.ClientConnectionManager;
import Connection.DecryptingImpl;
import Connection.Receiver;
import Connection.Sender;
import GUI.ControllerModule.MainController;
import MessengerUtils.Messenger;
import Utils.PasswordEncrypter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

@Singleton
public class CommandReceiverImpl implements CommandReceiver{
    private final CommandInvoker commandInvokerImpl;
    private final Sender senderImpl;
    private final Receiver receiverImpl;
    private final SocketChannel socketChannel;
    private final Register register;
    private final Selector selector;
    private final PasswordEncrypter passwordEncrypter;
    private String login;
    private String password;
    private City city;
    private Human governor;
    private Stage stage;
    private MainController mainController;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ru"));

    @Inject
    public CommandReceiverImpl(CommandInvoker commandInvokerImpl, ClientConnectionManager connectionManager, Sender senderImpl, Receiver receiverImpl, Register register, PasswordEncrypter passwordEncrypter) throws IOException {
        this.commandInvokerImpl = commandInvokerImpl;
        this.senderImpl = senderImpl;
        this.receiverImpl = receiverImpl;
        socketChannel = connectionManager.getSocketChannel();
        this.register = register;
        selector = Selector.open();
        this.passwordEncrypter = passwordEncrypter;
    }

    public void setLogData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void help() {
        StringBuilder sb = new StringBuilder();
        FXMLLoader loader = new FXMLLoader(DecryptingImpl.class.getResource("/GUI/Views/main.fxml"));
        MainController controller = (loader.getController());
        commandInvokerImpl.getCommandHashMap().forEach((name, command) -> sb.append(command.displayInfo()).append("\n"));
        controller.showInfo(sb.toString());
    }

    @Override
    public void info() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }

        requestHandler(new SerializedCommand(commandInvokerImpl.getCommandHashMap().get("info"), login, password));
    }

    public void show() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedCommand(commandInvokerImpl.getCommandHashMap().get("show"), login, password));
    }

    public void add() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedObjectCommand(commandInvokerImpl.getCommandHashMap().get("add"), city, login, password));
    }

    public void update(String ID) {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedCombinedCommand(commandInvokerImpl.getCommandHashMap().get("update"), city, ID, login, password));
    }

    public void remove_by_id(String ID) {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedArgumentCommand(commandInvokerImpl.getCommandHashMap().get("remove_by_id"), ID, login, password));
    }

    public void clear() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedCommand(commandInvokerImpl.getCommandHashMap().get("clear"), login, password));
    }

    public void exit() throws IOException {
        socketChannel.close();
        Messenger.printMessage("end_client_work");
        System.exit(0);
    }

    public void count_greater_than_government(String government) {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedArgumentCommand(commandInvokerImpl.getCommandHashMap().get("count_greater_than_government"), government, login, password));
    }

    public void add_if_min() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedObjectCommand(commandInvokerImpl.getCommandHashMap().get("add_if_min"), city, login, password));
    }

    public void add_if_max() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedObjectCommand(commandInvokerImpl.getCommandHashMap().get("add_if_max"), city, login, password));
    }

    public void remove_first() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedCommand(commandInvokerImpl.getCommandHashMap().get("remove_first"), login, password));
    }

    public void filter_by_government(String government) {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedArgumentCommand(commandInvokerImpl.getCommandHashMap().get("filter_by_government"), government, login, password));
    }

    public void print_field_ascending_agglomeration() {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы");
            return;
        }
        requestHandler(new SerializedCommand(commandInvokerImpl.getCommandHashMap().get("print_field_ascending_agglomeration"), login, password));
    }

    public void execute_script(String path) {
        if (login.equals("") || password.equals("")) {
            System.out.println("Вы не авторизованы.");
            return;
        }
        String line;
        String command;
        ArrayList<String> parameters = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.split(" ")[0].matches("add | update | add_if_max | add_if_min")) {
                    parameters.clear();
                    command = line;
                    for (int i = 0; i < 10; i++) {
                        if (line != null) {
                            line = bufferedReader.readLine();
                            parameters.add(line);
                        } else {
                            Messenger.printMessage("not_enough_params");
                            break;
                        }
                    }
                }
                else if (line.split(" ")[0].equals("execute_script") &&
                line.split(" ")[1].equals(((ExecuteScript)commandInvokerImpl.getCommandHashMap().get("execute_script")).getPath())) {
                    System.out.println("пресечена попытка рекурсивного вызова скрипта.");
                }
            }
        } catch (IOException e) {
            Messenger.printMessage("error");
        }
    }

    private void requestHandler(Object serializedObject) {
        try {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_WRITE);

            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isWritable()) {
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("command_Receiver (requestHandler)");
                        senderImpl.send(serializedObject);
                    }

                    if (selectionKey.isReadable()) {
                        System.out.println("command_Receiver (requestHandler) 2");
                        receiverImpl.receive(socketChannel);
                        return;
                    }
                }
            }
        } catch (PortUnreachableException e) {
            System.out.println("Не удалось получить данные по указанному порту.");
            System.exit(0);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void register(String login, String password) {
        System.out.println("command_receiver");
        setLogData(login, passwordEncrypter.encryptString(password));
        requestHandler(new SerializedLogOrReg(login, passwordEncrypter.encryptString(password), "reg"));
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void getCollection(String type) {
        requestHandler(new SerializedMessage(type));
    }

    public String getLogin() {
        return login;
    }

    public List<String> getCommandsName() {
        return commandInvokerImpl.getCommandsName();
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvokerImpl;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void tryLog(String login, String password) {
        setLogData(login, passwordEncrypter.encryptString(password));
        requestHandler(new SerializedLogOrReg(login, passwordEncrypter.encryptString(password), "log"));
    }
}