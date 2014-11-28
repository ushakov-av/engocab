package ru.mipt.engocab.ui.fx.view;

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

/**
 * Main view.
 *
 * @author Alexander Ushakov
 */
public class MainStage {

    private Model model;

    private MainController mainController;

    private Stage primaryStage;

    public MainStage(Stage primaryStage, Model model, MainController mainController) {
        this.primaryStage = primaryStage;
        this.model = model;
        this.mainController = mainController;

        BorderPane pane = new BorderPane();

        VBox topBox = new VBox(0);
        topBox.getChildren().addAll(createMenus(), createToolBar());
        pane.setTop(topBox);
        pane.setCenter(createTable());

        Scene scene = new Scene(pane, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Engocab");
        primaryStage.show();
        primaryStage.setOnCloseRequest(mainController::stopScheduler);
    }

    public void show() {
        primaryStage.show();
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
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, mainController::showWordEditForm);
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

        Button newButton = createToolbarButton("newButton", "Create New Card... Ctrl+N");
        newButton.setOnAction(mainController::showWordEnterForm);

        Button learnButton = createToolbarButton("learnButton", "Start learning... Ctrl+L");
        learnButton.setOnAction(mainController::showLearningStage);

        Button tagButton = createToolbarButton("setTag", "Set tag");
        tagButton.setOnAction(mainController::showActiveTagForm);

        Button directLesson = createToolbarButton("directLesson", "Start Lesson en->ru");
        directLesson.setOnAction(mainController::showDirectLessonForm);

        Button reverseLesson = createToolbarButton("reverseLesson", "Start Lesson ru->en");
        reverseLesson.setOnAction(mainController::showReverseLessonForm);

        Label currentTag = new Label(model.getActiveTag());

        toolBar.getItems().addAll(newButton, createSeparator(), learnButton, createSeparator(), tagButton,
                createSeparator(), directLesson, reverseLesson, createSeparator(), currentTag);

        return toolBar;
    }

    private Button createToolbarButton(String id, String tooltip) {
        Button button = new Button();
        button.setId(id);
        button.setGraphic(getRectangle());
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    private Separator createSeparator() {
        return new Separator(Orientation.VERTICAL);
    }

    private Rectangle getRectangle() {
        Rectangle rect = new Rectangle(24, 16);
        rect.setArcHeight(2);
        rect.setArcWidth(2);
        rect.setFill(Color.GREY);
        return rect;
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
