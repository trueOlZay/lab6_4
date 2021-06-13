package CommandModule;

import BaseClass.City;
import BaseClass.Government;
import CommandModule.SerializedCommand.SerializedArgumentCommand;
import CommandModule.SerializedCommand.SerializedCombinedCommand;
import CommandModule.SerializedCommand.SerializedCommand;
import CommandModule.SerializedCommand.SerializedObjectCommand;
import DataBase.DataBaseException;

import java.io.IOException;
import java.net.Socket;

public interface ServerCommandReceiver {
    void info(SerializedCommand command, Socket socket) throws IOException, DataBaseException;
    void show(SerializedCommand command, Socket socket) throws IOException, DataBaseException;
    void add(SerializedObjectCommand command, Socket socket) throws IOException, DataBaseException;
    void add_if_max(SerializedObjectCommand command, Socket socket) throws IOException, DataBaseException;
    void add_if_min(SerializedObjectCommand command, Socket socket) throws IOException, DataBaseException;
    void update(SerializedCombinedCommand command, Socket socket) throws IOException, DataBaseException;
    void remove_by_id(SerializedArgumentCommand command, Socket socket) throws IOException, DataBaseException;
    void clear(SerializedCommand command, Socket socket) throws IOException, DataBaseException;
    void remove_first(SerializedCommand command, Socket socket) throws IOException, DataBaseException;
    void count_greater_than_government(SerializedArgumentCommand command, Socket socket) throws IOException, DataBaseException;
    void filter_by_government(SerializedArgumentCommand command, Socket socket) throws IOException, DataBaseException;
    void print_field_ascending_agglomeration(SerializedCommand command, Socket socket) throws IOException, DataBaseException;
    void register(String login, String password, Socket socket) throws DataBaseException, IOException;
    void sendCollection(Socket socket, String type) throws DataBaseException, IOException;
    void tryRegister(String login, String password, Socket socket) throws DataBaseException, IOException;
}
