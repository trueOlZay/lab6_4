package GUI.ControllerModule;

import CommandModule.CommandReceiver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class WelcomeController {
    private CommandReceiver commandReceiver;
    private Stage stage;
    private AnchorPane pane;
    private Scene scene;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ru"));

    public void setCommandReceiver(CommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML private Button logUserButton;
    @FXML private Button regUserButton;
    @FXML private TextField userLogField;
    @FXML private PasswordField userPasswordField;
    @FXML private Button setRu;
    @FXML private Button setRo;
    @FXML private Button setHr;
    @FXML private Button setEc;

    @FXML
    void displayRegWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/Views/register.fxml"));
            pane = (AnchorPane) loader.load();
            scene = new Scene(pane, 350, 390);
            stage.setScene(scene);
            stage.show();

            RegisterController registerController = loader.getController();
            registerController.setCommandReceiver(commandReceiver);
            registerController.setStage(stage);
            registerController.setResourceBundle(resourceBundle);
            registerController.changeLanguage();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loginUserAction(ActionEvent event) {
        if (!userLogField.getText().isEmpty() && !userPasswordField.getText().isEmpty()) {
            commandReceiver.setResourceBundle(resourceBundle);
            commandReceiver.setStage(stage);
            commandReceiver.tryLog(userLogField.getText().trim(), userPasswordField.getText().trim());
        }
        else {
            showAlert(resourceBundle.getString("didNotEnterUsernameOrPassword"));
        }
    }

    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("err"));

        alert.setHeaderText(null);
        alert.setContentText(alertMessage);

        alert.showAndWait();
    }

    public void changeToMain(Stage stage, CommandReceiver commandReceiver, ResourceBundle resourceBundle) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/VIEWS/main.fxml"));
        pane = loader.load();
        scene = new Scene(pane, 1198, 494);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("City");
        stage.show();

        MainController mainController = loader.getController();
        commandReceiver.setMainController(mainController);
        mainController.setCommandReceiver(commandReceiver);
        mainController.setStage(stage);
        mainController.setResourceBundle(resourceBundle);
        mainController.changeLanguage();
    }

    public void changeLanguage() {
        logUserButton.setText(resourceBundle.getString("logIn"));
        regUserButton.setText(resourceBundle.getString("checkIn"));
        userLogField.setPromptText(resourceBundle.getString("enterYourLogin"));
        userPasswordField.setPromptText(resourceBundle.getString("enterYourPassword"));
    }

    @FXML private void setRoLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ro"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRo.setDisable(true);
        setRu.setDisable(false);
        setEc.setDisable(false);
        setHr.setDisable(false);
        changeLanguage();
    }

    @FXML private void setRuLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ru"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRo.setDisable(false);
        setRu.setDisable(true);
        setEc.setDisable(false);
        setHr.setDisable(false);
        changeLanguage();
    }

    @FXML private void setEcLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("es", "EC"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRo.setDisable(false);
        setRu.setDisable(false);
        setEc.setDisable(true);
        setHr.setDisable(false);
        changeLanguage();
    }

    @FXML private void setHrLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("hr"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRo.setDisable(false);
        setRu.setDisable(false);
        setEc.setDisable(false);
        setHr.setDisable(true);
        changeLanguage();
    }

    public void setResourceBundle(ResourceBundle bundle) {
        this.resourceBundle = resourceBundle;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
