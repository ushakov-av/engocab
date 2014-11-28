package ru.mipt.engocab.ui.fx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.mipt.engocab.ui.fx.controller.MainController;
import ru.mipt.engocab.ui.fx.model.Model;
import ru.mipt.engocab.ui.fx.model.ModelWordRecord;

public class MainStage extends Application {

    private Model model;

    private MainController mainController;

    private Stage stage;

    private void init(Stage primaryStage) {
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
        stage.setOnCloseRequest(mainController::stopScheduler);
    }

    @SuppressWarnings("unchecked")
    private TableView createTable() {

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn learntCol = createTableColumn("learnt percent", 100, "learntPercent");
        TableColumn firstNameCol = createTableColumn("en word", 200, "word");
        TableColumn translationNumber = createTableColumn("№", 5, "translationNumber");
        TableColumn lastNameCol = createTableColumn("ru word", 400, "translation");
        TableColumn emailCol = createTableColumn("description", 600, "description");
        tableView.getColumns().addAll(learntCol, firstNameCol, translationNumber, lastNameCol, emailCol);

        tableView.setItems(model.getData());

        return tableView;
    }

    @SuppressWarnings("unchecked")
    private TableColumn createTableColumn(String text, int minWidth, String column) {

        Callback<TableColumn, TableCell> stringCellFactory =
                p -> {
                    MainStringTableCell cell = new MainStringTableCell();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MainTableEventHandler());
                    return cell;
                };

        TableColumn tableColumn = new TableColumn();
        tableColumn.setText(text);
        tableColumn.setMinWidth(minWidth);
        tableColumn.setCellValueFactory(new PropertyValueFactory(column));
        tableColumn.setCellFactory(stringCellFactory);
        return tableColumn;
    }

    private MenuBar createMenus() {

        MenuBar menuBar = new MenuBar();

        Menu dictionary = new Menu("Dictionary");

        MenuItem open = new MenuItem("Open");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        open.setOnAction(mainController::showOpenWindow);

        MenuItem save = new MenuItem("Save");
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(mainController::saveDictionary);

        MenuItem saveAs = new MenuItem("Save as...");
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveAs.setOnAction(mainController::showSaveAsWindow);

        MenuItem statistics = new MenuItem("Statistics");
        statistics.setOnAction(mainController::showStatistics);

        MenuItem charts = new MenuItem("Charts");
        charts.setOnAction(mainController::showStudyProgress);
        dictionary.getItems().addAll(open, save, saveAs, statistics, charts);

        Menu settings = new Menu("Settings");

        MenuItem options = new MenuItem("Options...");
        options.setOnAction(mainController::showOptions);

        MenuItem dictionaries = new MenuItem("Dictionaries...");
        dictionaries.setOnAction(mainController::showDictionaries);

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
        newButton.setOnAction(mainController::showWordEnterForm);

        Separator sep1 = new Separator(Orientation.VERTICAL);

        Button learnButton = new Button();
        learnButton.setId("learnButton");
        learnButton.setGraphic(getRectangle());
        learnButton.setTooltip(new Tooltip("Start learning... Ctrl+L"));
        learnButton.setOnAction(mainController::showLearningStage);

        Separator sep2 = new Separator(Orientation.VERTICAL);

        Button tagButton = new Button();
        tagButton.setId("setTag");
        tagButton.setGraphic(getRectangle());
        tagButton.setTooltip(new Tooltip("Set tag"));
        tagButton.setOnAction(mainController::showActiveTagForm);

        Separator sep3 = new Separator(Orientation.VERTICAL);

        Button directLesson = new Button();
        directLesson.setId("directLesson");
        directLesson.setGraphic(getRectangle());
        directLesson.setTooltip(new Tooltip("Start Lesson en->ru"));
        directLesson.setOnAction(mainController::showDirectLessonForm);

        Button reverseLesson = new Button();
        reverseLesson.setId("reverseLesson");
        reverseLesson.setGraphic(getRectangle());
        reverseLesson.setTooltip(new Tooltip("Start Lesson ru->en"));
        reverseLesson.setOnAction(mainController::showReverseLessonForm);

        Separator sep4 = new Separator(Orientation.VERTICAL);

        Label currentTag = new Label(model.getActiveTag());

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new Model();
        mainController = new MainController(model, primaryStage);
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class MainTableEventHandler implements EventHandler<MouseEvent> {

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

    private static class MainStringTableCell extends TableCell<ModelWordRecord, String> {

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

}
