package Connection;

import GUI.ControllerModule.WelcomeController;
import MessengerUtils.Messenger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;

@Singleton
public class ClientConnectionManagerImpl implements ClientConnectionManager{
    private SocketChannel socketChannel;
    private String hostName;
    private int port;

    @Inject
    public ClientConnectionManagerImpl() throws IOException {
        FileReader fileReader;

        FXMLLoader loader = new FXMLLoader(ClientConnectionManagerImpl.class.getResource("/GUI/Views/welcome.fxml"));
        Parent sceneFXML = loader.load();
        WelcomeController controller = (loader.getController());
        try {
            fileReader = new FileReader(System.getProperty("user.dir") + "/config.txt");
            Scanner scanner = new Scanner(fileReader);
            String[] data = scanner.nextLine().split(" ");
            hostName = data[0];
            port = Integer.parseInt(data[1]);
            fileReader.close();
        } catch (FileNotFoundException e) {
            controller.showAlert(String.format(controller.getResourceBundle().getString("configurationFileFound"), "config.txt", System.getProperty("user.dir")));
            return;
        } catch (IOException e) {
            controller.showAlert(controller.getResourceBundle().getString("errorReadingFromConfigurationFile") + e);
            return;
        } catch (NumberFormatException e) {
            controller.showAlert(controller.getResourceBundle().getString("thePortMustBeAnInteger"));
            return;
        } catch (NoSuchElementException e) {
            controller.showAlert(controller.getResourceBundle().getString("writeTheHostAndPortInAFileWithASpace") + " config.txt");
            return;
        }

        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(hostName, port);
            socketChannel = SocketChannel.open(inetSocketAddress);
            socketChannel.configureBlocking(false);

        } catch (SocketException e) {
            controller.showAlert(controller.getResourceBundle().getString("failedToConnectToRemoteAddress"));
            System.exit(1);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        if (socketChannel != null) { socketChannel.close(); }
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
