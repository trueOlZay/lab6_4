package CommandModule;

import BaseClass.City;
import BaseClass.Government;
import CollectionUtils.CollectionManager;
import CollectionUtils.SupportManager;
import CommandModule.SerializedCommand.*;
import DataBase.DataBaseException;
import DataBase.DataBaseManager;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Singleton
public class ServerCommandReceiverImpl implements ServerCommandReceiver{
    private final CollectionManager collectionManager;
    private final SupportManager supportManager;
    private final DataBaseManager dataBaseManager;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Logger logger = LoggerFactory.getLogger(ServerCommandReceiverImpl.class);

    @Inject
    public ServerCommandReceiverImpl(CollectionManager collectionManager, SupportManager supportManager, DataBaseManager dataBaseManager) {
        this.collectionManager = collectionManager;
        this.supportManager = supportManager;
        this.dataBaseManager = dataBaseManager;
    }

    public void sendObject(Socket socket, SerializedMessage serializedMessage) throws IOException {
        Thread executorThread = new Thread(() -> {
            try {
                logger.error("дошли до commandReceiver(send Object)");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                logger.error("дошли до commandReceiver(send Object) 2");
                objectOutputStream.writeObject(serializedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executor.submit(executorThread);
    }

    public boolean checkUser(String login, String password, Socket socket) throws DataBaseException, IOException {
        boolean exist = dataBaseManager.CheckUserExist(login);

        return exist;
    }

    public void tryRegister(String login, String password, Socket socket) throws DataBaseException, IOException {
        boolean result = checkUser(login, password, socket);
        Thread executorThread = new Thread(() -> {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(new SerializedReAuth(result, "log"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executor.submit(executorThread);
    }

    public void info(SerializedCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            sendObject(socket, new SerializedMessage(collectionManager.info()));
        }
    }

    public void show(SerializedCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            sendObject(socket, new SerializedMessage(collectionManager.show()));
        }
    }

    public void add(SerializedObjectCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            try {
                City city = (City) command.getObject();
                city.setId(dataBaseManager.addCity(city, command.getLogin()));
                collectionManager.add(city);

                sendObject(socket, new SerializedMessage("Элемент успешно добавлен в коллекцию."));
            } catch (DataBaseException | IOException e) {
                sendObject(socket, new SerializedMessage("Элемент не добавлен в коллекцию."));
                e.printStackTrace();
            }
        }
    }

    public void add_if_max(SerializedObjectCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            City city = (City) command.getObject();
            if (collectionManager.check_max(city)) {
                sendObject(socket, new SerializedMessage(collectionManager.add_if_max(city)));
            }
            else {
                sendObject(socket, new SerializedMessage("Элемент не является максимальным, добавления не произошло."));
            }
        }
        else {
            sendObject(socket, new SerializedMessage("Такого пользователя не существует, дальнейшая работа невозможна."));
        }
    }

    public void add_if_min(SerializedObjectCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            City city = (City) command.getObject();
            if (collectionManager.check_min(city)) {
                sendObject(socket, new SerializedMessage(collectionManager.add_if_min(city)));
            }
            else {
                sendObject(socket, new SerializedMessage("Элемент не является минимальным, добавления не произошло."));
            }
        }
    }

    public void update(SerializedCombinedCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            Integer cityId;
            try {
                cityId = Integer.parseInt(command.getArg());
                if (supportManager.checkExist(cityId)) {
                    try {
                        City city = (City) command.getObject();
                        if (dataBaseManager.update_by_id(city, cityId, command.getLogin())) {
                            dataBaseManager.update_by_id(city, cityId, command.getLogin());
                            collectionManager.update(city, cityId);

                            sendObject(socket, new SerializedMessage(String.format("Команда выполнена успешно.", command.getLogin())));
                        }
                        else {
                            sendObject(socket, new SerializedMessage("Элемент с указанным id зарегистрирован другим пользователем."));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendObject(socket, new SerializedMessage("Элемент не добавлен."));
                    }
                }
                else {
                    sendObject(socket, new SerializedMessage("Элемента с таким ID нет в коллекции."));
                }
            } catch (NumberFormatException e) {
                sendObject(socket, new SerializedMessage("Команда update не выполнена."));
            }
        }
    }

    public void remove_by_id(SerializedArgumentCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            int cityId;
            try {
                cityId = Integer.parseInt(command.getArg());
                if (supportManager.checkExist(cityId)) {
                    if (dataBaseManager.remove_by_id(cityId, command.getLogin())) {
                        collectionManager.remove_by_id(cityId);
                        sendObject(socket, new SerializedMessage(String.format("Элемент успешно удален из коллекции.", command.getLogin(), cityId)));
                    }
                    else {
                        sendObject(socket, new SerializedMessage("Элемент с указанным ID зарегестрирован другим пользователем."));
                    }
                }
                else {
                    sendObject(socket, new SerializedMessage("Элемента с указанным ID в коллекции нет."));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sendObject(socket, new SerializedMessage("Команда remove_by_id не выполнена."));
            }
        }
    }

    public void clear(SerializedCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            logger.error("commandReceiver1");
            List<Integer> listDeletedId = dataBaseManager.clearUserDB(command.getLogin());
            logger.error("commandReceiver2");
            listDeletedId.forEach(collectionManager::remove_by_id);
            logger.error("commandReceiver3");
            sendObject(socket, new SerializedMessage("Ваша коллекция отчищена."));
        }
    }

    public void remove_first(SerializedCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            sendObject(socket, new SerializedMessage(collectionManager.remove_first()));
        }
    }

    public void count_greater_than_government(SerializedArgumentCommand command, Socket socket) throws DataBaseException, IOException {
        Government government;
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            if (command.getArg().equals(Government.DIARCHY.toString())) {
                government = Government.DIARCHY;
            }
            else if (command.getArg().equals(Government.JUNTA.toString())) {
                government = Government.JUNTA;
            }
            else if (command.getArg().equals(Government.TIMOCRACY.toString())) {
                government = Government.TIMOCRACY;
            }
            else if (command.getArg().equals(Government.DICTATORSHIP.toString())) {
                government = Government.DICTATORSHIP;
            }
            else {
                government = Government.CORPORATOCRACY;
            }
            sendObject(socket, new SerializedMessage(collectionManager.count_greater_than_government(government)));
        }
        else {
            sendObject(socket, new SerializedMessage("На сервере что-то пошло не так при выполнении команды count_greater_than_government."));
        }
    }

    public void filter_by_government(SerializedArgumentCommand command, Socket socket) throws DataBaseException, IOException {
        Government government;
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            if (command.getArg().equals(Government.DIARCHY.toString())) {
                government = Government.DIARCHY;
            }
            else if (command.getArg().equals(Government.JUNTA.toString())) {
                government = Government.JUNTA;
            }
            else if (command.getArg().equals(Government.TIMOCRACY.toString())) {
                government = Government.TIMOCRACY;
            }
            else if (command.getArg().equals(Government.DICTATORSHIP.toString())) {
                government = Government.DICTATORSHIP;
            }
            else {
                government = Government.CORPORATOCRACY;
            }
            sendObject(socket, new SerializedMessage(collectionManager.filter_by_government(government)));
        }
        else {
            sendObject(socket, new SerializedMessage("На сервере что-то пошло не так при выполнении команды filter_by_government."));
        }
    }

    public void print_field_ascending_agglomeration(SerializedCommand command, Socket socket) throws DataBaseException, IOException {
        if (checkUser(command.getLogin(), command.getPassword(), socket)) {
            sendObject(socket, new SerializedMessage(collectionManager.print_field_ascending_agglomeration()));
        }
        else {
            sendObject(socket, new SerializedMessage("что-то пошло не так на стороне сервера."));
        }
    }

    public void register(String login, String password, Socket socket) throws DataBaseException, IOException {
        logger.error("дошли до commandReceiver");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        boolean result = dataBaseManager.CheckUserExist(login);
        if (!result) {
            dataBaseManager.addUser(login, password);
            objectOutputStream.writeObject(new SerializedReAuth(true, "reg"));
        }
        else {
            objectOutputStream.writeObject(new SerializedReAuth(false, "reg"));
        }

        //if (!checkUser(command.getLogin(), command.getPassword(), socket)) {
            //dataBaseManager.addUser(command.getLogin(), command.getPassword());
            //sendObject(socket, new SerializedMessage("Пользователь успешно добавлен"));
        //}
        //else {
            //sendObject(socket, new SerializedMessage("Пользователь с таким логином уже существует."));
        //}
    }

    public void sendCollection(Socket socket, String type) throws DataBaseException, IOException {
        SerializedCollection serializedCollection;
        if (type.equals("init")) {
            serializedCollection = new SerializedCollection(Lists.newLinkedList(collectionManager.getCityLinkedList()), dataBaseManager.getUserElementsId(), "init");
        }
        else {
            serializedCollection = new SerializedCollection(Lists.newLinkedList(collectionManager.getCityLinkedList()), dataBaseManager.getUserElementsId(), "regular");
        }
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(serializedCollection);
    }
}
