
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServerConfigurator());
        ServerConnectionManager serverConnectionManager = injector.getInstance(ServerConnectionManager.class);
        serverConnectionManager.run(1900);
    }
}
