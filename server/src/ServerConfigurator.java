import CollectionUtils.CollectionManager;
import CollectionUtils.CollectionManagerImpl;
import CollectionUtils.SupportManager;
import CollectionUtils.SupportManagerImpl;
import CommandModule.ServerCommandReceiver;
import CommandModule.ServerCommandReceiverImpl;
import DataBase.DataBaseManager;
import DataBase.DataBaseManagerImpl;
import Utils.Decrypting;
import Utils.DecryptingImpl;
import Utils.PasswordEncrypter;
import Utils.PasswordEncrypterImpl;
import com.google.inject.AbstractModule;

public class ServerConfigurator extends AbstractModule {
    protected void configure() {
        bind(ServerConnectionManager.class).to(ServerConnectionManagerImpl.class);
        bind(CollectionManager.class).to(CollectionManagerImpl.class);
        bind(SupportManager.class).to(SupportManagerImpl.class);
        bind(DataBaseManager.class).to(DataBaseManagerImpl.class);
        bind(PasswordEncrypter.class).to(PasswordEncrypterImpl.class);
        bind(ServerCommandReceiver.class).to(ServerCommandReceiverImpl.class);
        bind(Decrypting.class).to(DecryptingImpl.class);
    }
}
