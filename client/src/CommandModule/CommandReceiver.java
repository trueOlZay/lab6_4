package CommandModule;

import BaseClass.City;
import GUI.ControllerModule.MainController;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public interface CommandReceiver {
    void setLogData(String login, String password);
    void help();
    void info() throws ClassNotFoundException, InterruptedException;
    void show();
    void add();
    void update(String ID);
    void remove_by_id(String ID);
    void clear();
    void exit() throws IOException;
    void count_greater_than_government(String government);
    void add_if_min();
    void add_if_max();
    void remove_first();
    void filter_by_government(String government);
    void print_field_ascending_agglomeration();
    void execute_script(String path);
    void register(String login, String password);
    ResourceBundle getResourceBundle();
    void setResourceBundle(ResourceBundle resourceBundle);
    String getLogin();
    List<String> getCommandsName();
    CommandInvoker getCommandInvoker();
    void getCollection(String type);
    void setCity(City city);
    Stage getStage();
    void setStage(Stage stage);
    void setMainController(MainController mainController);
    MainController getMainController();
    void tryLog(String login, String password);
}
