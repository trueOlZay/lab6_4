import CommandModule.*;
import CommandModule.SpecificCommand.*;
import Connection.*;
import Utils.PasswordEncrypter;
import Utils.PasswordEncrypterImpl;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;


public class ClientConfigurator extends AbstractModule {

    protected void configure() {

        bind(CommandInvoker.class).to(CommandInvokerImpl.class);
        bind(CommandReceiver.class).to(CommandReceiverImpl.class);
        bind(ClientConnectionManager.class).to(ClientConnectionManagerImpl.class);
        bind(Decrypting.class).to(DecryptingImpl.class);
        bind(Receiver.class).to(ReceiverImpl.class);
        bind(Sender.class).to(SenderImpl.class);
        bind(PasswordEncrypter.class).to(PasswordEncrypterImpl.class);

        Multibinder<GeneralCommand> commandBinder = Multibinder.newSetBinder(binder(), GeneralCommand.class);
        commandBinder.addBinding().to(Add.class);
        commandBinder.addBinding().to(AddIfMax.class);
        commandBinder.addBinding().to(AddIfMin.class);
        commandBinder.addBinding().to(Clear.class);
        commandBinder.addBinding().to(CountGreaterThanGovernment.class);
        commandBinder.addBinding().to(ExecuteScript.class);
        commandBinder.addBinding().to(Exit.class);
        commandBinder.addBinding().to(FilterByGovernment.class);
        commandBinder.addBinding().to(Help.class);
        commandBinder.addBinding().to(Info.class);
        commandBinder.addBinding().to(PrintFieldAscendingAgglomeration.class);
        commandBinder.addBinding().to(RemoveByID.class);
        commandBinder.addBinding().to(RemoveFirst.class);
        commandBinder.addBinding().to(Show.class);
        commandBinder.addBinding().to(Update.class);
    }
}
