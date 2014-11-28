package ru.mipt.engocab.ui.fx.view.wordform;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.mipt.engocab.core.model.Example;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;
import ru.mipt.engocab.ui.fx.model.Model;
import ru.mipt.engocab.ui.fx.model.ModelWordRecord;
import ru.mipt.engocab.ui.fx.view.wordform.WordForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Form for entering a word.
 *
 * @author Alexander V. Ushakov
 */
public class WordEnterForm {

    private Model model;
    private Stage stage;
    private WordForm form;

    public WordEnterForm(Model model) {
        this.model = model;
        stage = new Stage();
        stage.setTitle("Enter Word Form");
        form = new WordForm(stage);
        setupActions();
    }

    private void setupActions() {

        form.saveButton.setOnAction(event -> {

            List<ModelWordRecord> modelRecords = new ArrayList<>();

            String wordText = form.wordArea.getText();
            PartOfSpeech pos = PartOfSpeech.toPartOfSpeech(form.posChoice.getValue());
            int number = Integer.parseInt(form.numberChoice.getValue());
            String transcription = form.transcriptionField.getText();

            WordKey wordKey = new WordKey(wordText, pos, number, transcription);

            for (WordForm.RecordModel record : form.records) {

                String ruWordText = record.translationArea.getText();

                if (wordText.isEmpty() || ruWordText.isEmpty()) {
                    continue;
                }

                String enEnDescription = record.descArea.getText();
                //todo: check that index is a valid integer
                String index = record.index.getText();
                String tip = record.tipField.getText();

                ModelWordRecord modelRecord = new ModelWordRecord(wordText, ruWordText, enEnDescription, "0 %");
                modelRecord.setTranslationNumber(index);

                WordRecord wordRecord = new WordRecord(wordKey);
                wordRecord.setTranslation(ruWordText);
                wordRecord.setIndex(Integer.parseInt(index));
                wordRecord.setDescription(enEnDescription);
                wordRecord.setTip(tip);

                // Examples
                for (WordForm.ExampleForm exampleForm : record.examplesForms) {
                    String enExample = exampleForm.getWordText();
                    String ruExample = exampleForm.getTranslationText();
                    String phrase = exampleForm.getPhraseText();
                    if (!enExample.isEmpty() || !ruExample.isEmpty()) {
                        Example example = new Example(enExample, ruExample, phrase);
                        wordRecord.addExample(example);
                    }
                }

                // Synonyms

                // Tags
                for (TextField tagField : record.tags) {
                    if (!tagField.getText().isEmpty()) {
                        wordRecord.addTag(tagField.getText());
                    }
                }

                modelRecord.addWordRecord(wordRecord);
                modelRecord.setNumber(number);

                // Learnt status
                Status status = Status.get(record.learnStatusChoice.getValue());
                LearnCard card = new LearnCard(wordRecord.getId(), status);
                modelRecord.setLearnCard(card);

                modelRecords.add(modelRecord);
            }

            //todo: maybe show warning when partially filled?
            boolean added = model.addWord(wordKey, modelRecords);
            if (!added) {
                showWarning();
            }
            stage.close();
        });

        form.cancelButton.setOnAction(event -> stage.close());
    }

    private void showWarning() {
        final Stage warningPopup = new Stage();

        VBox popupPane = new VBox(10);
        popupPane.setMinSize(100, 100);
        popupPane.setPadding(new Insets(10, 10, 10, 10));
        Text text = new Text("Word already exists in the dictionary");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        popupPane.getChildren().addAll(text);

        Scene scene = new Scene(popupPane, popupPane.getPrefWidth(), popupPane.getPrefHeight());
        warningPopup.setResizable(false);
        warningPopup.setScene(scene);

        javafx.application.Platform.runLater(warningPopup::show);
    }

    public void setX(double x) {
        stage.setX(x);
    }

    public void setY(double y) {
        stage.setY(y);
    }

    public void showView() {
        javafx.application.Platform.runLater(stage::show);
    }

}
