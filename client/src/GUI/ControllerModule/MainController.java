package GUI.ControllerModule;

import BaseClass.*;
import CommandModule.CommandReceiver;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController implements Initializable {

    private ObservableList<City> observableList = FXCollections.observableArrayList();
    private ObservableList<String> commandNames = FXCollections.observableArrayList();
    private List<List<Integer>> elementsId = new ArrayList<>();
    private CommandReceiver commandReceiver;
    private Stage stage;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ru"));

    @FXML private Text hiText;
    @FXML private Pane cityMap;
    @FXML private Pane executeCommand;
    @FXML private Pane tablePane;
    @FXML private Button toTableButton;
    @FXML private Button toCityMapButton;
    @FXML private Button executeCommandsButton;
    @FXML private TableView<City> tableView;

    @FXML private TableColumn<City, Integer> idColumn;
    @FXML private TableColumn<City, String> nameColumn;
    @FXML private TableColumn<City, Double> xColumn;
    @FXML private TableColumn<City, Float> yColumn;
    @FXML private TableColumn<City, LocalDate> creationDateColumn;
    @FXML private TableColumn<City, Long> areaColumn;
    @FXML private TableColumn<City, Integer> populationColumn;
    @FXML private TableColumn<City, Float> metersAboveSeaLevelColumn;
    @FXML private TableColumn<City, Float> agglomerationColumn;
    @FXML private TableColumn<City, Climate> climateColumn;
    @FXML private TableColumn<City, Government> governmentColumn;
    @FXML private TableColumn<City, LocalDate> governorBirthdayColumn;

    @FXML private TextField idFilter;
    @FXML private TextField nameFilter;
    @FXML private TextField xFilter;
    @FXML private TextField yFilter;
    @FXML private TextField creationDateFilter;
    @FXML private TextField areaFilter;
    @FXML private TextField populationFilter;
    @FXML private TextField metersAboveSeaLevelFilter;
    @FXML private TextField agglomerationFilter;
    @FXML private ChoiceBox<String> climateFilter;
    @FXML private ChoiceBox<String> governmentFilter;
    @FXML private TextField governorBirthdayFilter;

    @FXML private TextField idField;
    @FXML private TitledPane cityTitledPane;
    @FXML private TitledPane governorTitledPane;
    @FXML private TextField cityNameField;
    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private TextField areaField;
    @FXML private TextField populationField;
    @FXML private TextField metersAboveSeaLevelField;
    @FXML private TextField agglomerationField;
    @FXML private ComboBox<String> climateComboBox;
    @FXML private ComboBox<String> governmentComboBox;
    @FXML private TextField governorBirthdayField;
    @FXML private ComboBox<String> commandChoiceComboBox;
    @FXML private Button executeCommandButton;

    @FXML private Button setRu;
    @FXML private Button setRo;
    @FXML private Button setHr;
    @FXML private Button setEc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        climateFilter.setItems(FXCollections.observableArrayList(Stream.concat(Stream.of(Climate.values()).map(Enum::toString), Stream.of("")).collect(Collectors.toList())));
        governmentFilter.setItems(FXCollections.observableArrayList(Stream.concat(Stream.of(Government.values()).map(Enum::toString), Stream.of("")).collect(Collectors.toList())));

        climateComboBox.setItems(FXCollections.observableArrayList(Stream.of(Climate.values()).map(Enum::toString).collect(Collectors.toList())));
        governmentComboBox.setItems(FXCollections.observableArrayList(Stream.of(Government.values()).map(Enum::toString).collect(Collectors.toList())));
        commandChoiceComboBox.setItems(commandNames);

        fillTable();
        updateTable();
        setListenersTextField();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    private void fillTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        xColumn.setCellValueFactory(city -> new SimpleDoubleProperty((city.getValue().getCoordinates().getX())).asObject());
        yColumn.setCellValueFactory(city -> new SimpleFloatProperty((city.getValue().getCoordinates().getY())).asObject());
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        metersAboveSeaLevelColumn.setCellValueFactory(new PropertyValueFactory<>("metersAboveSeaLevel"));
        agglomerationColumn.setCellValueFactory(new PropertyValueFactory<>("agglomeration"));
        climateColumn.setCellValueFactory(new PropertyValueFactory<>("climate"));
        governmentColumn.setCellValueFactory(new PropertyValueFactory<>("government"));
        governorBirthdayColumn.setCellValueFactory(city -> new SimpleObjectProperty<>(city.getValue().getGovernor().getBirthday()));

        tableView.setItems(observableList);
        tableView.setRowFactory(tv -> {
            TableRow<City> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    if (!row.isEmpty()) {
                        City selectedCity = row.getItem();
                        showInfoElement(selectedCity);
                    }
                    else {
                        tablePane.setVisible(false);
                        executeCommand.setVisible(true);
                        commandChoiceComboBox.setValue("add");
                    }
                }
            });
            return row;
        });
    }

    public void updateTable() {
        FilteredList<City> filtered = new FilteredList<>(observableList, t -> true);
        idFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> Integer.toString(city.getId()).equals(newValue)));
        nameFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> city.getName().toLowerCase().contains(newValue.toLowerCase())));
        xFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> city.getCoordinates().getX().toString().toLowerCase().equals(newValue.toLowerCase())));
        yFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> city.getCoordinates().getY().toString().toLowerCase().equals(newValue.toLowerCase())));
        creationDateFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> city.getCreationDate().toString().toLowerCase().equals(newValue.toLowerCase())));
        areaFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> Long.toString(city.getArea()).toLowerCase().equals(newValue.toLowerCase())));
        populationFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> Integer.toString(city.getPopulation()).toLowerCase().equals(newValue.toLowerCase())));
        metersAboveSeaLevelFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> Float.toString(city.getMetersAboveSeaLevel()).toLowerCase().equals(newValue.toLowerCase())));
        agglomerationFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> Float.toString(city.getAgglomeration()).toLowerCase().equals(newValue.toLowerCase())));
        climateFilter.setOnAction(event -> filtered.setPredicate(city -> city.getClimate().toString().contains(climateFilter.getValue())));
        governmentFilter.setOnAction(event -> filtered.setPredicate(city -> city.getGovernment().toString().contains(governmentFilter.getValue())));
        governorBirthdayFilter.textProperty().addListener((observable, oldValue, newValue) -> filtered.setPredicate(city -> city.getGovernor().getBirthday().toString().toLowerCase().equals(newValue.toLowerCase())));

        SortedList<City> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sorted);
    }

    public void visual() {
        ArrayList<Circle> circles = new ArrayList<>();
        HashMap<Circle, City> circleCityHashMap = new HashMap<>();
        HashMap<Color, List<Integer>> colorListHashMap = new HashMap<>();
        for (List<Integer> list: elementsId) {
            Color color = Color.color(Math.random(), Math.random(), Math.random(), Math.random());
            colorListHashMap.put(color, list);
        }

        for (City city: observableList) {
            double size = 20;
            Circle circle = new Circle();
            circle.setRadius(size);
            circle.setLayoutX(city.getCoordinates().getX() + 100);
            circle.setLayoutY(city.getCoordinates().getY() + 100);

            for (Map.Entry<Color, List<Integer>> entry: colorListHashMap.entrySet()) {
                if (entry.getValue().contains(city.getId())) {
                    circle.setFill(entry.getKey());
                }
            }

            circle.setStroke(Color.color(0,0,0));
            circle.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getSource() instanceof Circle) {
                        Circle selectedCircle = ((Circle) (event.getSource()));
                        City selectedCity = circleCityHashMap.get(selectedCircle);
                        showInfoElement(selectedCity);
                    }
                }
            });
            circles.add(circle);
            circleCityHashMap.put(circle,city);
        }
        Platform.runLater(() -> {
            cityMap.getChildren().clear();
            circles.sort(Comparator.comparing(Circle::getRadius));
            Collections.reverse(circles);
            cityMap.getChildren().setAll(circles);
        });
    }

    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("err"));

        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public void setCollection(LinkedList<City> linkedList, List<List<Integer>> elementsId) {
        observableList.clear();
        observableList.addAll(linkedList);
        this.elementsId = elementsId;
    }

    public void setCommandReceiver(CommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
        commandNames.clear();
        commandNames.addAll(commandReceiver.getCommandsName());
        changeLanguage();
    }

    @FXML
    private void showTable(ActionEvent event) {
        cityMap.setVisible(false);
        executeCommand.setVisible(false);
        tablePane.setVisible(true);
    }

    @FXML
    private void showCityMap(ActionEvent event) {
        visual();
        executeCommand.setVisible(false);
        tablePane.setVisible(false);
        cityMap.setVisible(true);
    }

    @FXML
    private void showExecuteCommand(ActionEvent event) {
        cityMap.setVisible(false);
        tablePane.setVisible(false);
        executeCommand.setVisible(true);
    }

    @FXML
    private void commandProcessing(ActionEvent actionEvent) {
        String commandName = commandChoiceComboBox.getSelectionModel().getSelectedItem();
        executeCommandsButton.setDisable(true);

        if (commandName.matches("add|add_if_max|add_if_min")) {
            cityTitledPane.setDisable(false);
            governorTitledPane.setDisable(false);
            idField.setDisable(true);
        }
        else if (commandName.equals("update")) {
            cityTitledPane.setDisable(false);
            governorTitledPane.setDisable(false);
            idField.setDisable(false);
        }
        else if (commandName.matches("remove_by_id")) {
            cityTitledPane.setDisable(true);
            governorTitledPane.setDisable(true);
            idField.setDisable(false);
        }
        else if (commandName.matches("count_greater_than_government|filter_by_government")) {
            cityTitledPane.setDisable(false);
            governorTitledPane.setDisable(false);
            idField.setDisable(false);
        }
        else {
            cityTitledPane.setDisable(true);
            governorTitledPane.setDisable(true);
            idField.setDisable(true);
            executeCommandsButton.setDisable(false);
        }

        if (commandName.equals("execute_script")) {
            executeCommandsButton.setDisable(false);
            String path = selectScriptFile();
            if (path != null) {
                showAlert("execute_script" + resourceBundle.getString("noLongerSupported"));
            }
        }
        clearTitlePanes();
    }

    private void clearTitlePanes() {
        idField.clear();
        governorBirthdayField.clear();
        xField.clear();
        yField.clear();
        cityNameField.clear();
        areaField.clear();
        populationField.clear();
        agglomerationField.clear();
        metersAboveSeaLevelField.clear();
        climateComboBox.getSelectionModel().select(-1);
        governmentComboBox.getSelectionModel().select(-1);
    }

    private String selectScriptFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt", "*.txt"));
        Stage stage = new Stage();
        stage.setTitle("SelectScriptFile");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    private void setListenersTextField() {
        idField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        governorBirthdayField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        areaField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        populationField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        metersAboveSeaLevelField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        agglomerationField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        xField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        yField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
        cityNameField.textProperty().addListener((observable, oldValue, newValue) -> dataValidator());
    }

    @FXML
    private void dataValidator() {
        try {
            executeCommandsButton.setDisable(true);

            boolean isIdCorrect = true;
            boolean isGovernorCorrect = true;
            boolean isCityCorrect = true;
            if (!idField.isDisable()) {
                isIdCorrect = Integer.parseInt(idField.getText()) > 0;
            }

            if (!cityTitledPane.isDisable()) {
                isCityCorrect = (cityNameField.getText() != null && !cityNameField.getText().equals("") &&
                        (Double.parseDouble(xField.getText()) < 531) &&
                        (Float.parseFloat(yField.getText()) > -653f) &&
                        (Long.parseLong(areaField.getText()) > 0) &&
                        (Integer.parseInt(populationField.getText()) > 0) &&
                        (Float.parseFloat(agglomerationField.getText()) > 0) &&
                        (governmentComboBox.getSelectionModel().getSelectedItem() != null) &&
                        (climateComboBox.getSelectionModel().getSelectedItem() != null));
            }

            if (isIdCorrect && isGovernorCorrect && isCityCorrect) {
                executeCommandsButton.setDisable(false);
            }
        }
        catch (Exception e) {

        }
    }

    private void showInfoElement(City city) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(resourceBundle.getString("information"));
        alert.setContentText(city.toLanguageString(resourceBundle));
        ButtonType close = new ButtonType("close");
        ButtonType edit = new ButtonType("update");
        ButtonType delete = new ButtonType("remove");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(delete, edit, close);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(close)) {
            alert.close();
        }
        else if (option.get().equals(edit)) {
            cityMap.setVisible(false);
            tablePane.setVisible(false);
            executeCommand.setVisible(true);

            commandChoiceComboBox.setValue("update");
            idField.setDisable(true);
            idField.setText(city.getId().toString());
            cityNameField.setText(city.getName());
            xField.setText(city.getCoordinates().getX().toString());
            yField.setText(city.getCoordinates().getY().toString());
            areaField.setText(Long.toString(city.getArea()));
            populationField.setText(Integer.toString(city.getPopulation()));
            metersAboveSeaLevelField.setText(Float.toString(city.getMetersAboveSeaLevel()));
            agglomerationField.setText(Float.toString(city.getAgglomeration()));
            governorBirthdayField.setText(city.getGovernor().getBirthday().toString());
            climateComboBox.setValue(city.getClimate().toString());
            governmentComboBox.setValue(city.getGovernment().toString());
        }
        else if (option.get().equals(delete)) {
            commandReceiver.remove_by_id(city.getId().toString());
        }
    }

    @FXML
    private void sendCommand(ActionEvent event) {
        Integer id;
        Human governor = null;
        City city = null;

        String command = commandChoiceComboBox.getSelectionModel().getSelectedItem();

        if (!idField.isDisable() || command.equals("update")) {
            id = Integer.parseInt(idField.getText());
        }

        if (!governorTitledPane.isDisable()) {
            governor = new Human(LocalDate.parse(governorBirthdayField.getText()));
        }

        if (!cityTitledPane.isDisable()) {
            city = new City(cityNameField.getText(), new Coordinates(Double.parseDouble(xField.getText()), Float.parseFloat(yField.getText())), Long.parseLong(areaField.getText()), Integer.parseInt(populationField.getText()),
                    Float.parseFloat(metersAboveSeaLevelField.getText()), Float.parseFloat(agglomerationField.getText()),
                    Climate.valueOf(climateComboBox.getSelectionModel().getSelectedItem()), Government.valueOf(governmentComboBox.getSelectionModel().getSelectedItem()), governor);
        }

        if (command.matches("add|add_if_max|add_if_min")) {
            commandReceiver.setCity(city);
            commandReceiver.getCommandInvoker().execute(command.split(" "));
        }
        else if (command.equals("update")) {
            commandReceiver.setCity(city);
            commandReceiver.update(idField.getText());
        }
        else if (command.matches("remove_by_id")) {
            commandReceiver.remove_by_id(idField.getText());
        }
        else {
            commandReceiver.getCommandInvoker().execute(command.split(" "));
        }
    }

    public void showInfo(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("information"));

            alert.setHeaderText(null);
            alert.setContentText(message);

            TextArea area = new TextArea(message);
            area.setWrapText(true);
            area.setEditable(false);

            alert.getDialogPane().setContent(area);
            alert.setResizable(true);

            alert.showAndWait();
        });
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void changeLanguage() {
        toCityMapButton.setText(resourceBundle.getString("cityMap"));
        toTableButton.setText(resourceBundle.getString("toTheTable"));
        executeCommandsButton.setText(resourceBundle.getString("executeCommand"));
        hiText.setText(resourceBundle.getString("hi").replace("\n", "")+", " + commandReceiver.getLogin());
        idField.setPromptText(resourceBundle.getString("enterItemId"));
        cityTitledPane.setText(resourceBundle.getString("cityInformation"));
        governorTitledPane.setText(resourceBundle.getString("governorInformation"));
        cityNameField.setPromptText(resourceBundle.getString("enterCityName"));
        xField.setPromptText(resourceBundle.getString("enter") + " X (max = 531)");
        yField.setPromptText(resourceBundle.getString("enter") + " Y (min = -653)");
        areaField.setPromptText(resourceBundle.getString("enterArea"));
        populationField.setPromptText(resourceBundle.getString("enterPopulation"));
        metersAboveSeaLevelField.setPromptText(resourceBundle.getString("enterMetersAboveSeaLevel"));
        agglomerationField.setPromptText(resourceBundle.getString("enterAgglomeration"));
        climateComboBox.setPromptText(resourceBundle.getString("chooseAClimate"));
        governmentComboBox.setPromptText(resourceBundle.getString("chooseGovernment"));
        commandChoiceComboBox.setPromptText(resourceBundle.getString("chooseACommand"));
        executeCommandsButton.setText(resourceBundle.getString("run"));
    }

    @FXML private void setRuLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ru"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRu.setDisable(true);
        setEc.setDisable(false);
        setHr.setDisable(false);
        setRo.setDisable(false);
        changeLanguage();
    }

    @FXML private void setHrLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("hr"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRu.setDisable(false);
        setEc.setDisable(false);
        setHr.setDisable(true);
        setRo.setDisable(false);
        changeLanguage();
    }

    @FXML private void setRoLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("ro"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRu.setDisable(false);
        setEc.setDisable(false);
        setHr.setDisable(false);
        setRo.setDisable(true);
        changeLanguage();
    }

    @FXML private void setEcLanguage() {
        resourceBundle = ResourceBundle.getBundle("Messages", new Locale("es", "EC"));
        commandReceiver.setResourceBundle(resourceBundle);
        setRu.setDisable(false);
        setEc.setDisable(true);
        setHr.setDisable(false);
        setRo.setDisable(false);
        changeLanguage();
    }
}
