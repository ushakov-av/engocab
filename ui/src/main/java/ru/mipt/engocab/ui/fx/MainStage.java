package ru.mipt.engocab.ui.fx;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.Index;
import ru.mipt.engocab.data.json.DataMapper;
import ru.mipt.engocab.ui.fx.model.Lesson;
import ru.mipt.engocab.ui.fx.model.Model;
import ru.mipt.engocab.ui.fx.model.ModelWordRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainStage extends Application {

    private Model model;
    private Stage stage;

    private FileChooser fileChooser;

    private Label currentTag;

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    private File currentDictionaryFile;

    private void init(Stage primaryStage) {
        model = new Model();
        stage = primaryStage;

        BorderPane pane = new BorderPane();

        VBox topBox = new VBox(0);
        topBox.getChildren().addAll(createMenus(), createToolBar());
        pane.setTop(topBox);

        pane.setCenter(createTable());

        Scene scene = new Scene(pane, 800, 600);

        stage.setScene(scene);
        stage.setTitle("Engocab");
        stage.show();
        stage.setOnCloseRequest(e -> scheduler.shutdownNow());
    }

    @SuppressWarnings("unchecked")
    private TableView createTable() {

        TableView tableView = new TableView();

        Callback<TableColumn, TableCell> stringCellFactory =
                p -> {
                    MyStringTableCell cell = new MyStringTableCell();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        TableColumn learntCol = new TableColumn();
        learntCol.setText("learnt percent");
        learntCol.setMinWidth(100);
        learntCol.setCellValueFactory(new PropertyValueFactory("learntPercent"));
        learntCol.setCellFactory(stringCellFactory);

        TableColumn firstNameCol = new TableColumn();
        firstNameCol.setText("en word");
        firstNameCol.setMinWidth(200);
        firstNameCol.setCellValueFactory(new PropertyValueFactory("word"));
        firstNameCol.setCellFactory(stringCellFactory);

        TableColumn translationNumber = new TableColumn();
        translationNumber.setText("№");
        translationNumber.setMinWidth(5);
        translationNumber.setCellValueFactory(new PropertyValueFactory("translationNumber"));
        translationNumber.setCellFactory(stringCellFactory);

        TableColumn lastNameCol = new TableColumn();
        lastNameCol.setText("ru word");
        lastNameCol.setMinWidth(400);
        lastNameCol.setCellValueFactory(new PropertyValueFactory("translation"));
        lastNameCol.setCellFactory(stringCellFactory);

        TableColumn emailCol = new TableColumn();
        emailCol.setText("description");
        emailCol.setMinWidth(600);
        emailCol.setCellValueFactory(new PropertyValueFactory("description"));
        emailCol.setCellFactory(stringCellFactory);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(learntCol, firstNameCol, translationNumber, lastNameCol, emailCol);

        tableView.setItems(model.getData());

        model.getData().addListener((ListChangeListener<ModelWordRecord>) change -> {
//                System.out.println("list changed");
        });

        return tableView;
    }

    private MenuBar createMenus() {
        fileChooser = new FileChooser();
        MenuBar menuBar = new MenuBar();

        Menu dictionary = new Menu("Dictionary");
        MenuItem open = new MenuItem("Open");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        open.setOnAction(getOpenAction());
        MenuItem save = new MenuItem("Save");
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(e -> saveDictionary());
        MenuItem saveAs = new MenuItem("Save as...");
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveAs.setOnAction(getSaveAsAction());
        MenuItem statistics = new MenuItem("Statistics");
        statistics.setOnAction(getStatisticsAction());
        MenuItem charts = new MenuItem("Charts");
        charts.setOnAction(getChartsAction());
        dictionary.getItems().addAll(open, save, saveAs, statistics, charts);

        Menu settings = new Menu("Settings");
        MenuItem options = new MenuItem("Options...");
        options.setOnAction(e -> {
            OptionsForm form = new OptionsForm();
            form.showView();
        });
        MenuItem dictionaries = new MenuItem("Dictionaries...");
        dictionaries.setOnAction(e -> {
            DictionariesForm form = new DictionariesForm();
            form.showView();
        });
        settings.getItems().addAll(options, dictionaries);

        menuBar.getMenus().addAll(dictionary, settings);

        return menuBar;
    }

    private ToolBar createToolBar() {

        ToolBar toolBar = new ToolBar();

        Button newButton = new Button();
        newButton.setId("newButton");
        newButton.setGraphic(getRectangle());
        newButton.setTooltip(new Tooltip("Create New Card... Ctrl+N"));
        newButton.setOnAction((e -> {
            WordEnterForm enterForm = new WordEnterForm(model);
            enterForm.setX(stage.getX());
            enterForm.setY(stage.getY());
            enterForm.showView();
        }));

        Separator sep1 = new Separator(Orientation.VERTICAL);

        Button learnButton = new Button();
        learnButton.setId("learnButton");
        learnButton.setGraphic(getRectangle());
        learnButton.setTooltip(new Tooltip("Start learning... Ctrl+L"));
        learnButton.setOnAction((e -> {
            Lesson lesson = model.createLearnLesson(currentTag.getText());
            LearnForm learnForm = new LearnForm(model, lesson);
            learnForm.showView();
        }));

        Separator sep2 = new Separator(Orientation.VERTICAL);

        Button tagButton = new Button();
        tagButton.setId("setTag");
        tagButton.setGraphic(getRectangle());
        tagButton.setTooltip(new Tooltip("Set tag"));
        tagButton.setOnAction(e -> {
            ChooseActiveTagForm activeTagForm = new ChooseActiveTagForm(model, currentTag);
            activeTagForm.showView();
        });

        Separator sep3 = new Separator(Orientation.VERTICAL);

        Button directLesson = new Button();
        directLesson.setId("directLesson");
        directLesson.setGraphic(getRectangle());
        directLesson.setTooltip(new Tooltip("Start Lesson en->ru"));
        directLesson.setOnAction(e -> {
            Lesson lesson = model.createCheckLesson(true, currentTag.getText());
            CheckForm checkForm = new CheckForm(model, lesson, true);
            checkForm.showView();
        });

        Button reverseLesson = new Button();
        reverseLesson.setId("reverseLesson");
        reverseLesson.setGraphic(getRectangle());
        reverseLesson.setTooltip(new Tooltip("Start Lesson ru->en"));
        reverseLesson.setOnAction(e -> {
            Lesson lesson = model.createCheckLesson(false, currentTag.getText());
            CheckForm checkForm = new CheckForm(model, lesson, false);
            checkForm.showView();
        });

        Separator sep4 = new Separator(Orientation.VERTICAL);

        currentTag = new Label();

        toolBar.getItems().addAll(newButton, sep1, learnButton, sep2, tagButton, sep3,
                directLesson, reverseLesson, sep4, currentTag);

        return toolBar;
    }

    private Rectangle getRectangle() {
        Rectangle rect = new Rectangle(24, 16);
        rect.setArcHeight(2);
        rect.setArcWidth(2);
        rect.setFill(Color.GREY);
        return rect;
    }

    private EventHandler<ActionEvent> getStatisticsAction() {
        return actionEvent -> javafx.application.Platform.runLater(() -> {
            Stage statStage = new Stage();
            GridPane grid = new GridPane();

            int rowIndex = 0;

            // row 0

            Label numberLabel = new Label("Words Number : ");
            Label numberValue = new Label(String.valueOf(model.getDictionary().getContainers().size()));
            grid.add(numberLabel, 0, rowIndex);
            grid.add(numberValue, 1, rowIndex);

            // row 1
            rowIndex++;

            Label activeLabel = new Label("Active cards : ");
            Label activeValue = new Label(String.valueOf(model.getCards().getActive().size()));
            grid.add(activeLabel, 0, rowIndex);
            grid.add(activeValue, 1, rowIndex);

            // row 2
            rowIndex++;

            Label learntLabel = new Label("Learnt cards : ");
            Label learntValue = new Label(String.valueOf(model.getCards().getLearnt().size()));
            grid.add(learntLabel, 0, rowIndex);
            grid.add(learntValue, 1, rowIndex);

            // Tags :

            List<String> tags = new ArrayList<>(model.getTags().keySet());
            Collections.sort(tags);
            for (String tag : tags) {
                rowIndex++;
                Label tagLabel = new Label("Tag " + tag + " : ");
                Label tagCount = new Label(String.valueOf(model.getTags().get(tag)));
                grid.add(tagLabel, 0, rowIndex);
                grid.add(tagCount, 1, rowIndex);
            }

            Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
            statStage.setResizable(false);
            statStage.setScene(scene);
            statStage.show();
        });
    }

    private void saveDictionary() {
        if (currentDictionaryFile == null) {
            fileChooser.setTitle("Save as...");
            currentDictionaryFile = fileChooser.showOpenDialog(stage);
            if (currentDictionaryFile != null) {
                fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
            }
        }
        saveDictionaryFiles();
    }

    private EventHandler<ActionEvent> getSaveAsAction() {
        return actionEvent -> {
            fileChooser.setTitle("Save as...");
            File chosenFile = fileChooser.showOpenDialog(stage);
            if (chosenFile != null) {
                currentDictionaryFile = chosenFile;
            } else {
                return;
            }
            fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
            saveDictionaryFiles();
        };
    }

    private void saveDictionaryFiles() {
        if (currentDictionaryFile != null) {
            String fileName = currentDictionaryFile.getName();
            int i = fileName.lastIndexOf('.');
            String prefix = fileName.substring(0, i);
            File directoryFile = currentDictionaryFile.getParentFile();
            String directoryPath = directoryFile.getPath();
            File cardsFile = new File(directoryPath + java.io.File.separator + prefix + "_cards.dat");
            File indexFile = new File(directoryPath + java.io.File.separator + prefix + "_index.dat");
            if (!cardsFile.exists()) {
                try {
                    cardsFile.createNewFile();
                } catch (IOException e) {
                    //
                }
            }
            if (!indexFile.exists()) {
                try {
                    indexFile.createNewFile();
                } catch (IOException e) {
                    //
                }
            }
            String serializedDictionary = DataMapper.serialize(model.getDictionary());
            String serializedCards = DataMapper.serialize(model.getCards());
            String serializedIndex = DataMapper.serialize(model.getIndex());
            writeToFile(currentDictionaryFile, serializedDictionary);
            writeToFile(cardsFile, serializedCards);
            writeToFile(indexFile, serializedIndex);

            final Stage savePopup = new Stage();

            VBox popupPane = new VBox(10);
            popupPane.setMinSize(100, 100);
            popupPane.setPadding(new Insets(10, 10, 10, 10));
            Text text = new Text("Saved!");
            text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
            popupPane.getChildren().addAll(text);

            Scene scene = new Scene(popupPane, popupPane.getPrefWidth(), popupPane.getPrefHeight());
            savePopup.setResizable(false);
            savePopup.setScene(scene);


            javafx.application.Platform.runLater(() -> {
                savePopup.show();
                scheduler.schedule(() -> javafx.application.Platform.runLater(savePopup::close), 2, TimeUnit.SECONDS);
            });
        }
    }

    private EventHandler<ActionEvent> getOpenAction() {
        return e -> {
            fileChooser.setTitle("Open dictionary...");
            currentDictionaryFile = fileChooser.showOpenDialog(stage);
            if (currentDictionaryFile != null) {
                System.out.println(currentDictionaryFile.getAbsolutePath());
                String fileName = currentDictionaryFile.getName();
                int i = fileName.lastIndexOf('.');
                String prefix = fileName.substring(0, i);
                fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
                File directoryFile = currentDictionaryFile.getParentFile();
                String directoryPath = directoryFile.getPath();
                File cardsFile = new File(directoryPath + File.separator + prefix + "_cards.dat");
                File indexFile = new File(directoryPath + File.separator + prefix + "_index.dat");
                try {
                    Dictionary dictionary = DataMapper.deserializeFromStream(new FileInputStream(currentDictionaryFile), Dictionary.class);
                    Cards cards = DataMapper.deserializeFromStream(new FileInputStream(cardsFile), Cards.class);
                    Index index = DataMapper.deserializeFromStream(new FileInputStream(indexFile), Index.class);
                    model.updateModel(dictionary, cards, index);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private EventHandler<ActionEvent> getChartsAction() {
        return e -> {
            Map<Integer, Integer> reverseStatistics = model.getReverseLearntStatistics();
            final Stage popup = new Stage();
            popup.setTitle("Words Study Progress");

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart<String, Number> bc = new BarChart<>(xAxis,yAxis);

            bc.setTitle("Words Study Progress");
            xAxis.setLabel("Percent Learnt");
            yAxis.setLabel("Words Number");


            XYChart.Series series = new XYChart.Series();
            for (Map.Entry<Integer, Integer> entry : reverseStatistics.entrySet()) {
                series.getData().add(new XYChart.Data(String.valueOf(entry.getKey()), entry.getValue()));
            }
            bc.getData().addAll(series);

            Scene scene = new Scene(bc, 800, 600);
            popup.setScene(scene);

            javafx.application.Platform.runLater(popup::show);
        };
    }

    private void writeToFile(File file, String serializedDictionary) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            out.write(serializedDictionary);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    private class MyEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
            if (t.getClickCount() > 1) {
                TableCell c = (TableCell) t.getSource();
                int index = c.getIndex();
                ObservableList<ModelWordRecord> data = model.getData();
                if (data.size() > index) {
                    WordEditForm form = new WordEditForm(model, index);
                    form.setX(stage.getX());
                    form.setY(stage.getY());
                    form.showView();
                }
            }
        }
    }

    class MyStringTableCell extends TableCell<ModelWordRecord, String> {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
