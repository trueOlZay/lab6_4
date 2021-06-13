import CommandModule.CommandReceiver;
import Connection.DecryptingImpl;
import GUI.ControllerModule.WelcomeController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws IOException {
        Injector injector = Guice.createInjector(new ClientConfigurator());
        CommandReceiver commandReceiver = injector.getInstance(CommandReceiver.class);

        FXMLLoader loader = new FXMLLoader(DecryptingImpl.class.getResource("/GUI/Views/welcome.fxml"));
        Parent parent = loader.load();
        WelcomeController controller = (loader.getController());
        controller.setCommandReceiver(commandReceiver);
        controller.setStage(stage);
        stage.setTitle("CityProject");
        stage.setScene(new Scene(parent, 350, 350));
        stage.centerOnScreen();
        stage.show();
    }
}
