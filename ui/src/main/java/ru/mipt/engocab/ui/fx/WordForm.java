package ru.mipt.engocab.ui.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import ru.mipt.engocab.core.model.Example;

import ru.mipt.engocab.ui.fx.model.ModelWordRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * <tt>WordForm</tt> form which can be used for adding new words or editing existing words.
 *
 * @author Alexander V. Ushakov
 */
public class WordForm {

    private static final Font tahomaFont = Font.font("Tahoma", FontWeight.NORMAL, 20);
    private static final String SPACE = "     ";

    public BorderPane borderPane;
    public Stage stage;
    public Scene currentScene;

    // fields for word
    public TextField wordArea;
    public ChoiceBox<String> posChoice;
    public ChoiceBox<String> numberChoice;
    public TextField transcriptionField;

    // word records, added 10 when form is initialized
    public TabPane recordsTabPane;
    public List<RecordModel> records = new ArrayList<>();

    public Button saveButton;
    public Button cancelButton;

    public static class RecordModel {
        // number of examples
        public int examplesNumber = 0;
        // record index (importance of word translation)
        public TextField index;
        // word translation
        public TextArea translationArea;
        // word description
        public TextArea descArea;
        // word tip (short word)
        public TextField tipField;
        // word learn status
        public ChoiceBox<String> learnStatusChoice;
        // button to add example
        public Button addExampleButton;
        // tab pane to hold examples
        public TabPane tabPane;
        // list of example forms which are in tab
        public List<ExampleForm> examplesForms = new ArrayList<>();
        // word tags
        public TextField[] tags = new TextField[5];

        // ModelRecord being edited
        public ModelWordRecord modelWordRecord;
    }

    public static class ExampleForm {
        public TextArea wordText;
        public TextArea translationText;
        public TextField phrase;

        public ExampleForm(Tab tab, Example example) {
            this(tab);
            this.wordText.setText(example.getWordExample());
            this.translationText.setText(example.getTranslationExample());
            this.phrase.setText(example.getPhrase());
        }

        public ExampleForm(Tab tab) {
            VBox exampleBox = new VBox(5);
            exampleBox.setPadding(new Insets(10, 0, 10, 0));

            HBox phraseBox = new HBox(0);
            Text phraseLabel = new Text("Phrase ");
            phraseLabel.setFont(tahomaFont);
            phrase = new TextField();
            phrase.setPrefColumnCount(30);
            phrase.setMinHeight(30);
            phraseBox.getChildren().addAll(phraseLabel, phrase);

            Text enExample = new Text("En ");
            enExample.setFont(tahomaFont);
            wordText = new TextArea();
            wordText.setWrapText(true);
            wordText.setMaxWidth(Double.MAX_VALUE);
            wordText.setPrefRowCount(2);

            Text ruExample = new Text("Ru ");
            ruExample.setFont(tahomaFont);
            translationText = new TextArea();
            translationText.setWrapText(true);
            translationText.setMaxWidth(Double.MAX_VALUE);
            translationText.setPrefRowCount(2);

            exampleBox.getChildren().addAll(phraseBox, enExample, wordText, ruExample, translationText);
            tab.setContent(exampleBox);
        }

        public String getWordText() {
            return wordText.getText();
        }

        public String getTranslationText() {
            return translationText.getText();
        }

        public String getPhraseText() {
            return phrase.getText();
        }
    }

    public WordForm(final Stage stage) {

        this.stage = stage;

        borderPane = new BorderPane();
        stage.setTitle("Enter Word Form");

        final GridPane grid = createGrid();
        borderPane.setCenter(grid);
        int rowIndex = 0;

        // row 0

        Text enWord = new Text("Word");
        enWord.setFont(tahomaFont);
        grid.add(enWord, 0, rowIndex, 1, 1);

        HBox translationBox = new HBox(10);
        translationBox.setAlignment(Pos.CENTER_RIGHT);
        Text transcriptionLabel = new Text("Transcription: ");
        transcriptionLabel.setFont(tahomaFont);
        transcriptionField = new TextField();
        transcriptionField.setPrefColumnCount(16);
        transcriptionField.setMinHeight(30);
        translationBox.getChildren().addAll(transcriptionLabel, transcriptionField);
        grid.add(translationBox, 1, rowIndex, 1, 1);

        // row 1
        rowIndex++;

        wordArea = new TextField();
        wordArea.setMaxWidth(Double.MAX_VALUE);
        wordArea.setPrefColumnCount(30);
        wordArea.setMinHeight(30);
        grid.add(wordArea, 0, rowIndex, 1, 1);

        HBox posBox = new HBox(10);
        posBox.setAlignment(Pos.CENTER_RIGHT);
        Text posLabel = new Text("Part of speech: ");
        posLabel.setFont(tahomaFont);
        posChoice = new ChoiceBox<>();
        posChoice.setMinHeight(30);
        posChoice.setPrefWidth(100);
        posChoice.getItems().addAll("Noun", "Verb", "Phrasal Verb", "Adjective", "Adverb", "Preposition");
        posChoice.getSelectionModel().selectFirst();
        Text numLabel = new Text("№: ");
        numLabel.setFont(tahomaFont);
        numberChoice = new ChoiceBox<>();
        numberChoice.setMinHeight(30);
        numberChoice.setPrefWidth(30);
        numberChoice.getItems().addAll("1", "2", "3", "4", "5");
        numberChoice.getSelectionModel().selectFirst();
        posBox.getChildren().addAll(posLabel, posChoice, numLabel, numberChoice);
        grid.add(posBox, 1, rowIndex, 1, 1);

        // row 2
        rowIndex++;

        // Create tab pane for records

        BorderPane recordsTabBox = new BorderPane();
        recordsTabPane = new TabPane();
        recordsTabPane.setSide(Side.TOP);
        recordsTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        recordsTabPane.setMaxWidth(Double.MAX_VALUE);
        recordsTabBox.setCenter(recordsTabPane);
        grid.add(recordsTabBox, 0, rowIndex, 2, 1);

        for (int j = 1; j <= 10; j++) {
            recordsTabPane.getTabs().add(createRecordTab(j));
        }

        // row3
        rowIndex++;

        saveButton = new Button("Save");
        saveButton.setMinSize(100, 40);
        saveButton.setDefaultButton(true);
        cancelButton = new Button("Cancel");
        cancelButton.setMinSize(100, 40);
        HBox hbBtn = new HBox(50);
        hbBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hbBtn.setSpacing(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(saveButton, cancelButton);
        grid.add(hbBtn, 0, rowIndex, 2, 1);

        setScene();
    }

    private GridPane createGrid() {
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return grid;
    }

    private Tab createRecordTab(int i) {

        RecordModel record = new RecordModel();
        records.add(record);

        Tab tab = new Tab(SPACE + String.valueOf(i) + SPACE);


        final GridPane grid = createGrid();
        grid.setPadding(new Insets(25, 0, 25, 0));
        tab.setContent(grid);
        int rowIndex = 0;

        // row 1

        Text translationLabel = new Text("Translation");
        translationLabel.setFont(tahomaFont);
        grid.add(translationLabel, 0, rowIndex, 2, 1);

        // row 2
        rowIndex++;

        record.translationArea = new TextArea();
        record.translationArea.setPrefColumnCount(50);
        record.translationArea.setWrapText(true);
        record.translationArea.setMaxWidth(Double.MAX_VALUE);
        record.translationArea.setPrefRowCount(3);
        grid.add(record.translationArea, 0, rowIndex, 2, 1);

        // row 3
        rowIndex++;

        HBox descBox = new HBox(10);

        Text enEnLabel = new Text("Description");
        enEnLabel.setFont(tahomaFont);

        descBox.getChildren().addAll(enEnLabel);
        grid.add(descBox, 0, rowIndex, 2, 1);

        // row 4
        rowIndex++;

        record.descArea = new TextArea();
        record.descArea.setMaxWidth(Double.MAX_VALUE);
        record.descArea.setWrapText(true);
        record.descArea.setPrefRowCount(3);
        grid.add(record.descArea, 0, rowIndex, 2, 1);

        // row 5
        rowIndex++;

        HBox examplesButtons = new HBox(50);
        record.addExampleButton = new Button("Add Example");
        record.addExampleButton.setMinSize(100, 40);
        examplesButtons.setAlignment(Pos.BOTTOM_LEFT);
        examplesButtons.getChildren().add(record.addExampleButton);
        grid.add(examplesButtons, 0, rowIndex);

        HBox tipBox = new HBox(10);
        tipBox.setAlignment(Pos.CENTER_RIGHT);

        Text transcriptionText = new Text("№ : ");
        transcriptionText.setFont(tahomaFont);
        record.index = new TextField();
        record.index.setPrefColumnCount(2);
        record.index.setMinHeight(30);
        Text tipText = new Text("Tip : ");
        tipText.setFont(tahomaFont);
        record.tipField = new TextField();
        record.tipField.setPrefColumnCount(30);
        record.tipField.setMinHeight(30);
        Text learnStatusText = new Text("Learn : ");
        learnStatusText.setFont(tahomaFont);
        record.learnStatusChoice = new ChoiceBox<>();
        record.learnStatusChoice.getItems().addAll("To learn", "Learnt");
        record.learnStatusChoice.setMinHeight(30);
        record.learnStatusChoice.getSelectionModel().selectFirst();
        tipBox.getChildren().addAll(transcriptionText, record.index, tipText, record.tipField, learnStatusText, record.learnStatusChoice);
        grid.add(tipBox, 1, rowIndex);

        // row 6
        rowIndex++;

        BorderPane tabBox = new BorderPane();
        record.tabPane = new TabPane();
        record.tabPane.setSide(Side.TOP);
        record.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        record.tabPane.setMaxWidth(Double.MAX_VALUE);
        tabBox.setCenter(record.tabPane);
        grid.add(tabBox, 0, rowIndex, 2, 1);

        createTab(record);

        record.addExampleButton.setOnAction(actionEvent -> createTab(record));

        // row 7
        rowIndex++;

        Text tags = new Text("Tags");
        tags.setFont(tahomaFont);
        grid.add(tags, 0, rowIndex, 2, 1);

        // row 8
        rowIndex++;

        HBox tagsBox = new HBox(8);
        for (int j = 0; j < 5; j++) {
            record.tags[j] = new TextField();
        }
        tagsBox.getChildren().addAll(record.tags);
        grid.add(tagsBox, 0, rowIndex, 2, 1);

        return tab;
    }

    private void setScene() {
        double prefHeight = borderPane.getPrefHeight();
        double prefWidth = borderPane.getPrefWidth();
        currentScene = new Scene(borderPane, prefWidth, prefHeight);
        stage.setResizable(false);
        stage.setScene(currentScene);
    }

    public Tab createTab(RecordModel record) {
        Tab tab = new Tab("Example " + (++record.examplesNumber));
        record.tabPane.getTabs().add(tab);
        record.examplesForms.add(new ExampleForm(tab));
        return tab;
    }

    public Tab createTab(RecordModel record, Example example) {
        Tab tab = new Tab("Example " + (++record.examplesNumber));
        record.tabPane.getTabs().add(tab);
        record.examplesForms.add(new ExampleForm(tab, example));
        return tab;
    }

}
