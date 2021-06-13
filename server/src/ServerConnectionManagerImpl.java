import CollectionUtils.CollectionManager;
import DataBase.DataBaseException;
import DataBase.DataBaseManager;
import Utils.Decrypting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.sun.org.slf4j.internal.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class ServerConnectionManagerImpl implements ServerConnectionManager{
    private final CollectionManager collectionManager;
    private ServerSocket server;
    private final Decrypting decrypting;
    private final DataBaseManager dataBaseManager;
    private static final Logger logger = LoggerFactory.getLogger(ServerConnectionManagerImpl.class);
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Inject
    public ServerConnectionManagerImpl(CollectionManager collectionManager,DataBaseManager dataBaseManager, Decrypting decrypting) {
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
        this.decrypting = decrypting;
    }

    public void run(int port) {
        try {
            try {
                collectionManager.initList();
                try {
                    dataBaseManager.cityFromDB(collectionManager);
                } catch (DataBaseException e) {
                    e.printStackTrace();
                }

                server = new ServerSocket(port);

                while (true) {
                    Socket clientSocket = server.accept();
                    Thread executorThread = new Thread(() -> {
                        ObjectInputStream in = null;
                        try {
                            try {
                                while (true) {
                                        in = new ObjectInputStream(clientSocket.getInputStream());
                                        Object o = in.readObject();
                                        logger.error("дошли до server connection manager");
                                        decrypting.decrypt(o, clientSocket);
                                    }
                            } catch (EOFException | SocketException e) {
                                Thread.currentThread().interrupt();

                            } catch (DataBaseException e) {
                                e.printStackTrace();
                            } finally {
                                Thread.currentThread().interrupt();
                                clientSocket.close();
                                if (in != null) {
                                    in.close();
                                }
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                    executor.submit(executorThread);
                }
            } finally {
                server.close();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}