package ru.mipt.engocab.ui.fx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.PaginationBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.Example;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.ui.fx.model.Lesson;
import ru.mipt.engocab.ui.fx.model.LessonEntry;
import ru.mipt.engocab.ui.fx.model.Model;

import java.util.List;

/**
 * @author Alexander V. Ushakov
 */
public class LearnForm {

    private final Dictionary dictionary;
    private final List<LessonEntry> cards;
    private Stage stage;

    public LearnForm(final Model model, Lesson lesson) {
        dictionary = model.getDictionary();
        cards = lesson.getCards();

        stage = new Stage();
        stage.setTitle("Study words");
        BorderPane borderPane = new BorderPane();

        if (cards.isEmpty()) {
            Text enWord = new Text("All words are learnt !");
            enWord.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
            borderPane.setCenter(enWord);
        } else {
            final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();
            final Pagination pagination = PaginationBuilder.create().pageCount(cards.size()).pageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    createWordForm(pageIndex, webEngine);
                    return browser;
                }
            }).build();
            pagination.setPrefSize(800, 500);

            borderPane.setCenter(pagination);
        }
        Scene scene = new Scene(borderPane, borderPane.getPrefWidth(), borderPane.getPrefHeight());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    private void createWordForm(Integer pageIndex, WebEngine webEngine) {

        LessonEntry entry = cards.get(pageIndex);
        WordRecord record = dictionary.getWordRecord(entry.getWordKey(), entry.getLearnCard().getRecordId());

//        final GridPane grid = new GridPane();
//        grid.setAlignment(Pos.TOP_CENTER);
//        grid.setHgap(5);
//        grid.setVgap(5);
//        grid.setPadding(new Insets(5, 5, 5, 5));

//        Text enWord = new Text(record.getWord());
//        enWord.setWrappingWidth(500);
//        enWord.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
//        grid.add(enWord, 0, 0, 2, 1);
//        TextArea ruWord = new TextArea(record.getTranslation());
//        ruWord.setPrefRowCount(3);
//        ruWord.setWrapText(true);
//        ruWord.setEditable(false);
//        grid.add(ruWord, 0, 1, 2, 1);

//        int i = 0;
//        int j = 1;
//        final GridPane exampleGrid = new GridPane();
//        for (Example example : record.getExamples()) {
//            Text exampleNumber = new Text(String.valueOf(j++));
//            exampleNumber.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
//            exampleGrid.add(exampleNumber, 0, i);
//            TextArea enExampleArea = new TextArea(example.getWordExample());
//            enExampleArea.setEditable(false);
//            enExampleArea.setWrapText(true);
//            enExampleArea.setPrefRowCount(3);
//            exampleGrid.add(enExampleArea, 1, i++, 2, 1);
//            TextArea ruExampleArea = new TextArea(example.getTranslationExample());
//            ruExampleArea.setEditable(false);
//            ruExampleArea.setWrapText(true);
//            ruExampleArea.setPrefRowCount(3);
//            exampleGrid.add(ruExampleArea, 1, i++, 2, 1);
//
//        }
//        grid.add(exampleGrid, 0, 2, 2, 1);

        StringBuilder builder = new StringBuilder();
        builder.append(WordViewBuilder.HEADER)
                .append(WordViewBuilder.createMainWord(record.getWordKey().getWord()))
                .append(WordViewBuilder.createSubMainWord(0, record.getWordKey().getWord(),
                        record.getWordKey().getTranscription(), record.getWordKey().getPartOfSpeech()))
                .append(WordViewBuilder.createTranslation(0, record.getTranslation(), null));

        int i = 1;
        for (Example example : record.getExamples()) {
            if (example.getPhrase() != null && !example.getPhrase().isEmpty()) {
                builder.append(WordViewBuilder.createPhrase(example.getPhrase()));
            }
            builder.append(WordViewBuilder.createExample(example.getWordExample()));
            if (example.getTranslationExample() != null && !example.getTranslationExample().isEmpty()) {
                builder.append(WordViewBuilder.createExample(example.getTranslationExample()));
            }
        }

        builder.append(WordViewBuilder.FOOTER);

        webEngine.loadContent(builder.toString());
    }

    public void showView() {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.show();
            }
        });
    }

}
