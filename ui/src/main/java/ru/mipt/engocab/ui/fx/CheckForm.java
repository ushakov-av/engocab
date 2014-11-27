package ru.mipt.engocab.ui.fx;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.Example;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;
import ru.mipt.engocab.ui.fx.model.Lesson;
import ru.mipt.engocab.ui.fx.model.LessonEntry;
import ru.mipt.engocab.ui.fx.model.Model;

import java.util.List;

/**
 * @author Alexander V. Ushakov
 */
public class CheckForm {

    private final Dictionary dictionary;
    private final List<LessonEntry> entries;
    private final Model model;

    private Stage stage;

    private boolean direct;
    private int index = 0;

    public CheckForm(final Model model, Lesson lesson, boolean direct) {
        this.model = model;
        this.dictionary = model.getDictionary();
        this.entries = lesson.getCards();
        this.direct = direct;

        this.stage = new Stage();
        stage.setTitle("Check Words");
        stage.setOnCloseRequest(e -> model.updateModel());
    }

    public void showView() {
        javafx.application.Platform.runLater(() -> {
            showNext();
            stage.show();
        });
    }

    public void showNext() {

        final Form form = createForm(true, null);
        showForm(form);

        form.button.setOnAction(e -> {

            boolean checkResult;

            String translationText = form.translationWord.getText();
            if (!translationText.isEmpty() && form.translation.contains(translationText)
                    && translationText.length() > 1) {

                checkResult = true;
                if (direct) {
                    form.learnCard.incrementLearnt();
                } else {
                    form.learnCard.incrementLearntReverse();
                }
                if (form.learnCard.isLearnt()) {
                    model.changeWordLearntStatus(form.learnCard, Status.LEARNT);
                }
            } else {
                checkResult = false;
                if (direct) {
                    form.learnCard.decrementLearnt();
                } else {
                    form.learnCard.decrementLearntReverse();
                }
            }

            final Form form1 = createForm(false, checkResult);
            form1.button.setOnAction(actionEvent -> {
                index++;
                if (index == entries.size()) {
                    model.updateModel();
                    stage.close();
                } else {
                    showNext();
                }
            });
            showForm(form1);
        });
    }

    private void showForm(Form form) {
        final Scene scene = new Scene(form.pane, form.pane.getPrefWidth(), form.pane.getPrefHeight());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    private Form createForm(boolean check, Boolean checkResult) {

        final Form form = new Form();

        GridPane pane = form.pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(10);
        pane.setHgap(10);

        if (entries.isEmpty()) {

            Text word = new Text("All words are learnt !");
            word.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
            pane.setAlignment(Pos.CENTER);
            pane.add(word, 0, 0);

        } else {
            LessonEntry lessonEntry = entries.get(index);
            form.learnCard = lessonEntry.getLearnCard();
            final WordRecord record = dictionary.getWordRecord(lessonEntry.getWordKey(), form.learnCard.getRecordId());

            form.translation = (direct) ? record.getTranslation() : record.getWordKey().getWord();

            int rowIndex = 0;

            // row 0

            String wordText = direct ? record.getWordKey().getWord() : record.getTranslation();
            Text word = new Text(wordText);
            word.setWrappingWidth(800);
            word.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
            pane.add(word, 0, rowIndex, 1, 1);

            Text posLabel = new Text(PartOfSpeech.value(record.getWordKey().getPartOfSpeech()));
            posLabel.setWrappingWidth(100);
            posLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
            pane.add(posLabel, 1, rowIndex, 1, 1);

            // row 1
            rowIndex++;

            form.translationWord = new TextArea();
            if (!check) {
                form.translationWord.setText(form.translation);
            }
            form.translationWord.setPrefRowCount(2);
            form.translationWord.setWrapText(true);
            form.translationWord.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                private KeyEvent recodedEvent;

                @Override
                public void handle(KeyEvent event) {
                    if (recodedEvent != null) {
                        recodedEvent = null;
                        return;
                    }
                    Parent parent = form.translationWord.getParent();
                    if (parent != null) {
                        switch (event.getCode()) {
                            case ENTER: {
                                {
                                    Event parentEvent = event.copyFor(parent, parent);
                                    form.translationWord.getParent().fireEvent(parentEvent);
                                }
                                event.consume();
                                break;
                            }
                            default: {
                                if (event.isAltDown()) {
                                    System.out.println("Alt");
                                    form.altHandler.handle(new ActionEvent());
                                    event.consume();
                                }
                                if (event.isControlDown()) {
                                    System.out.println("Ctrl");
                                    form.ctrlHandler.handle(new ActionEvent());
                                    event.consume();
                                }
                            }
                        }
                    }
                }
            });
            pane.add(form.translationWord, 0, rowIndex, 2, 1);

            // row 2
            rowIndex++;

            HBox resultBox = new HBox();
            resultBox.setAlignment(Pos.BASELINE_CENTER);
            String resultText;
            boolean correct = false;
            if (checkResult == null) {
                resultText = " ";
            } else if (checkResult) {
                correct = true;
                resultText = "Correct";
            } else {
                resultText = "Incorrect";
            }
            form.result = new Text(resultText);
            form.result.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            if (correct) {
                form.result.setFill(Color.GREEN);
            } else {
                form.result.setFill(Color.RED);
            }
            pane.add(form.result, 0, rowIndex, 1, 1);


            String buttonText = (check) ? "Check" : "Next";
            form.button = new Button(buttonText);
            form.button.setDefaultButton(true);
            form.button.setMinSize(100, 40);
            form.button.setDefaultButton(true);
            pane.add(form.button, 1, rowIndex, 1, 1);

            if (check) {

                // row 3
                rowIndex++;

                form.show = new Button("Show Examples");
                form.show.setMinSize(100, 40);
                pane.add(form.show, 0, rowIndex);

                // row 4
                rowIndex++;

                final GridPane exampleGrid = new GridPane();
                exampleGrid.setPrefWidth(800);
                fillExamples(record, exampleGrid, true, false);
                pane.add(exampleGrid, 0, rowIndex, 2, 1);

                final int tmp = rowIndex;

                form.ctrlHandler = e -> {
                    pane.getChildren().remove(exampleGrid);
                    fillExamples(record, exampleGrid, true, true);
                    pane.add(exampleGrid, 0, tmp, 2, 1);
                };
                form.show.setOnAction(form.ctrlHandler);
                form.altHandler = e -> form.translationWord.setText(String.valueOf(form.translation.charAt(0)));
            } else {
                // row 3
                rowIndex++;

                final GridPane exampleGrid = new GridPane();
                fillExamples(record, exampleGrid, false, true);
                pane.add(exampleGrid, 0, rowIndex, 2, 1);
                form.ctrlHandler = e -> {};
                form.altHandler = e -> {};
            }
        }
        return form;
    }

    private class Form {

        public GridPane pane;
        public Button button;
        public TextArea translationWord;
        public Text result;

        public Button show;

        public EventHandler<ActionEvent> ctrlHandler;
        public EventHandler<ActionEvent> altHandler;

        public String translation;
        public LearnCard learnCard;
    }

    private void fillExamples(WordRecord record, GridPane exampleGrid, boolean check, boolean showDirectExample) {
        exampleGrid.getChildren().removeAll();
        exampleGrid.setVgap(5);
        int columnCount = 160;
        int i = 0;
        int j = 1;
        for (Example example : record.getExamples()) {
            Text exampleNumber = new Text(String.valueOf(j++));
            exampleNumber.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
            exampleGrid.add(exampleNumber, 0, i, 1, 1);
            TextArea enExampleArea = new TextArea();
            String dirTxt = (direct) ? example.getWordExample() : example.getTranslationExample();
            int drowCount = (dirTxt == null) ? 0 : dirTxt.length() / columnCount;
//            enExampleArea.setPrefColumnCount(columnCount);
            enExampleArea.setPrefRowCount(drowCount + 1);
            enExampleArea.setMaxWidth(Double.MAX_VALUE);
            if (showDirectExample) {
                String phraseString = example.getPhrase();
                if (phraseString == null) {
                    phraseString = "";
                } else {
                    if (check && !direct) {
                        phraseString = phraseString.replace(record.getWordKey().getWord(), "?");
                    }
                }
                Text phrase = new Text("   " + phraseString);
                phrase.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
                exampleGrid.add(phrase, 1, i, 1, 1);
                enExampleArea.setText(dirTxt);
            } else {
                enExampleArea.setText(" ");
            }
            enExampleArea.setEditable(false);
            enExampleArea.setWrapText(true);
            exampleGrid.add(enExampleArea, 0, ++i, 2, 1);
            if (!check) {
                String text = direct ? example.getTranslationExample() : example.getWordExample();
                int rowCount = (text == null) ? 0 : text.length() / columnCount;
                TextArea ruExampleArea = new TextArea(text);
//                ruExampleArea.setPrefColumnCount(columnCount);
                ruExampleArea.setMaxWidth(Double.MAX_VALUE);
                ruExampleArea.setEditable(false);
                ruExampleArea.setWrapText(true);
                ruExampleArea.setPrefRowCount(rowCount + 1);
                exampleGrid.add(ruExampleArea, 0, ++i, 2, 1);
            }
            i++;
        }
        if (!check) {
            String description = record.getDescription();
            Text desc = new Text(description);
            desc.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
            desc.setWrappingWidth(800);
            exampleGrid.add(desc, 0, i, 2, 1);
        }
        StringBuilder builder = new StringBuilder();
        for (String tag : record.getTags()) {
            builder.append(tag).append(", ");
        }
        int lastIndex = builder.length() > 0 ? builder.length() - 2 : 0;
        Text tagsText = new Text("\nTags : [" + builder.toString().substring(0, lastIndex) + "]");
        tagsText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        tagsText.setFill(Color.BLUE);
        exampleGrid.add(tagsText, 0, ++i, 2, 1);
    }

}
