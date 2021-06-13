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

public class RegisterController {
    private CommandReceiver commandReceiver;
    private AnchorPane anchorPane;
    private Scene scene;
    private Stage stage;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ru"));

    public void setCommandReceiver(CommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
    }

    @FXML private Button regUserButton;
    @FXML private Button authUserWindowButton;
    @FXML private TextField newLoginField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField repNewPasswordField;
    @FXML private Button setRu;
    @FXML private Button setRo;
    @FXML private Button setHr;
    @FXML private Button setEc;

    @FXML
    public void regUserAction(ActionEvent event) throws IOException {
        if (!newLoginField.getText().isEmpty() && !newPasswordField.getText().isEmpty() && !repNewPasswordField.getText().isEmpty()) {
            if (newPasswordField.getText().equals(repNewPasswordField.getText())) {
                commandReceiver.setResourceBundle(resourceBundle);
                commandReceiver.setStage(stage);
                commandReceiver.register(newLoginField.getText(), newPasswordField.getText());
            }
            else {
                showAlert(resourceBundle.getString("passwordsEnteredDoNotMatch"));
            }
        }
        else {
            showAlert(resourceBundle.getString("oneOfTheFieldsIsEmpty"));
        }
    }

    public void changeLanguage() {
        regUserButton.setText(resourceBundle.getString("signUp"));
        authUserWindowButton.setText(resourceBundle.getString("login"));
        newLoginField.setPromptText(resourceBundle.getString("createAUsername"));
        newPasswordField.setPromptText(resourceBundle.getString("createAPassword"));
        repNewPasswordField.setPromptText(resourceBundle.getString("confirmPassword"));
    }

    @FXML
    public void displayAuthWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/Views/welcome.fxml"));
            anchorPane = (AnchorPane) loader.load();
            scene = new Scene(anchorPane, 350, 350);
            stage.setScene(scene);
            stage.show();

            WelcomeController welcomeController = loader.getController();
            welcomeController.setCommandReceiver(commandReceiver);
            welcomeController.setStage(stage);
            welcomeController.changeLanguage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("err"));

        alert.setHeaderText(null);
        alert.setContentText(alertMessage);

        alert.showAndWait();
    }

    public void showSuccessMessage(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("success"));

        alert.setHeaderText(null);
        alert.setContentText(alertMessage);

        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToMain(Stage stage, CommandReceiver commandReceiver, ResourceBundle resourceBundle) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/Views/main.fxml"));
        anchorPane = loader.load();
        scene = new Scene(anchorPane, 1198, 494);
        stage.setScene(scene);
        stage.setTitle("CityProject");
        stage.centerOnScreen();
        stage.show();

        MainController controller = loader.getController();
        commandReceiver.setMainController(controller);
        controller.setCommandReceiver(commandReceiver);
        controller.setStage(stage);
        controller.setResourceBundle(resourceBundle);
        controller.changeLanguage();
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

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

}
