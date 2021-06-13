package Connection;

import BaseClass.City;
import CommandModule.CommandReceiver;
import CommandModule.SerializedCommand.SerializedCollection;
import CommandModule.SerializedCommand.SerializedMessage;
import CommandModule.SerializedCommand.SerializedReAuth;
import GUI.ControllerModule.MainController;
import GUI.ControllerModule.RegisterController;
import GUI.ControllerModule.WelcomeController;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class DecryptingImpl implements Decrypting {
    private final CommandReceiver commandReceiver;

    @Inject
    public DecryptingImpl(CommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
    }

    public void decrypt(Object o) throws IOException {
        System.out.println("decrypting");
        if (o instanceof SerializedMessage) {
            SerializedMessage serializedMessage = (SerializedMessage) o;
            FXMLLoader loader = new FXMLLoader(DecryptingImpl.class.getResource("/GUI/Views/main.fxml"));
            Parent parent = loader.load();
            MainController controller = (loader.getController());
            if (serializedMessage.getMessage() != null) {
                controller.showInfo(translate(serializedMessage.getMessage()));
            }
            else {
                if (serializedMessage.getLinkedList().isEmpty()) {
                    controller.showInfo(commandReceiver.getResourceBundle().getString("collectionIsEmpty"));
                }
                else {
                    controller.showInfo(serializedMessage.getLinkedList().stream().map(city -> city.toLanguageString(commandReceiver.getResourceBundle())).collect(Collectors.joining()));
                }
            }
        }

        if (o instanceof SerializedReAuth) {
            SerializedReAuth serializedReAuth = (SerializedReAuth) o;

            if (serializedReAuth.getType().equals("log")) {
                FXMLLoader loader = new FXMLLoader(DecryptingImpl.class.getResource("/GUI/Views/welcome.fxml"));
                Parent parent = loader.load();
                WelcomeController controller = (loader.getController());
                controller.setCommandReceiver(commandReceiver);

                if (serializedReAuth.getResult()) {
                    controller.changeToMain(commandReceiver.getStage(), commandReceiver, commandReceiver.getResourceBundle());
                    commandReceiver.getCollection("init");
                }
                else {
                    controller.showAlert(commandReceiver.getResourceBundle().getString("unableToLogIn"));
                }
            }

            if (serializedReAuth.getType().equals("reg")) {
                FXMLLoader loader = new FXMLLoader(DecryptingImpl.class.getResource("/GUI/Views/register.fxml"));
                Parent parent = loader.load();
                RegisterController controller = (loader.getController());
                controller.setCommandReceiver(commandReceiver);

                if (serializedReAuth.getResult()) {
                    controller.changeToMain(commandReceiver.getStage(), commandReceiver, commandReceiver.getResourceBundle());
                    controller.showSuccessMessage(commandReceiver.getResourceBundle().getString("userSuccessfullyRegistered"));
                    commandReceiver.getCollection("init");
                }
                else {
                    controller.showAlert(commandReceiver.getResourceBundle().getString("aUserWithThisLoginAlreadyExists"));
                }
            }
        }

        if (o instanceof SerializedCollection) {
            SerializedCollection serializedCollection = (SerializedCollection) o;
            LinkedList<City> linkedList = serializedCollection.getLinkedList();
            List<List<Integer>> idElements = serializedCollection.getIdElements();
            if (serializedCollection.getType().equals("init")) {
                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            commandReceiver.getCollection("regular");
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                MainController mainController = commandReceiver.getMainController();
                mainController.setCommandReceiver(commandReceiver);
                mainController.setCollection(linkedList, idElements);
            }
            else {
                MainController mainController = commandReceiver.getMainController();
                mainController.setCollection(linkedList, idElements);
                mainController.updateTable();
                mainController.visual();
            }
        }
    }

    private String translate(String message) {
        switch (message.toLowerCase()) {
            case "элемент добавлен" : return commandReceiver.getResourceBundle().getString("itemAdded");
            case "элемент не добавлен" : return commandReceiver.getResourceBundle().getString("itemNotAdded");
            case "элемент обновлен" : return commandReceiver.getResourceBundle().getString("itemUpdated");
            case "элемент не обновлен" : return commandReceiver.getResourceBundle().getString("itemNotUpdated");
            case "элеменет создан другим пользователем" : return commandReceiver.getResourceBundle().getString("itemCreatedByAnotherUser");
            case "элемента с таким ID нет в коллекции" : return commandReceiver.getResourceBundle().getString("anItemWithThisIdIsNotInTheCollection");
            case "некорректный аргумент" : return commandReceiver.getResourceBundle().getString("invalidArgument");
            case "элемент удален" : return commandReceiver.getResourceBundle().getString("itemDeleted");
            case "ваши элементы коллекции удалены" : return commandReceiver.getResourceBundle().getString("yourCollectionItemsHaveBeenDeleted");
            case "таких элементов не найдено" : return commandReceiver.getResourceBundle().getString("noSuchItemsFound");
            case "элемент не прошел валидацию на стороне сервера" : return commandReceiver.getResourceBundle().getString("itemFailedServerSideValidation");
        }

        if (message.contains("removeElements")) {
            message = message.replace("removeElements", commandReceiver.getResourceBundle().getString("deletedItemsWithID"));
        }
        else if (message.contains("ошибка при удалении из бд элеемента с id=")) {
            message = message.replace("ошибка при удалении из бд элеемента с id=", commandReceiver.getResourceBundle().getString("anErrorOccurredWhileDeletingAnElementWithIdFromTheDatabase"));
        }
        else if (message.contains("%data")) {
            message = message.replace("%type", commandReceiver.getResourceBundle().getString("collectionType"));
            message = message.replace("%data", commandReceiver.getResourceBundle().getString("collectionInitializationDate"));
            message = message.replace("%size", commandReceiver.getResourceBundle().getString("collectionLength"));
        }

        return message;
    }
}
