package ru.mipt.engocab.ui.fx;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import ru.mipt.engocab.core.model.Example;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;
import ru.mipt.engocab.ui.fx.model.Model;
import ru.mipt.engocab.ui.fx.model.ModelWordRecord;

import java.util.List;

/**
 * Form to edit word.
 * <p/>
 * When form is edited it is the responsibility of a user to retain contiguous order of translation indexes.
 *
 * @author Alexander V. Ushakov
 */
public class WordEditForm {

    private Model model;
    private Stage stage;
    private WordForm form;

    public WordEditForm(final Model model, int index) {
        this.model = model;
        stage = new Stage();
        stage.setTitle("Edit Word Form");
        this.form = new WordForm(stage);

        final ModelWordRecord modelWordRecord = model.getData().get(index);

        populateForm(modelWordRecord);

        // Save all form records
        // If translation is empty the word record will be removed
        form.saveButton.setOnAction(e -> {

            // Create Word
            String wordText = form.wordArea.getText();
            String transcription = form.transcriptionField.getText();
            PartOfSpeech pos = PartOfSpeech.toPartOfSpeech(form.posChoice.getValue());
            int number = Integer.parseInt(form.numberChoice.getValue());
            WordKey wordKey = new WordKey(wordText, pos, number, transcription);

            // Save all form records
            for (WordForm.RecordModel formRecord : form.records) {

                // Is record is edited or created anew
                boolean newWord = true;
                WordRecord oldWordRecord = null;
                ModelWordRecord oldModelWordRecord = formRecord.modelWordRecord;
                if (oldModelWordRecord != null) {
                    newWord = false;
                    oldModelWordRecord = formRecord.modelWordRecord;
                    oldWordRecord = oldModelWordRecord.getWordRecord();
                }

                String translationText = formRecord.translationArea.getText();
                if (translationText.trim().isEmpty()) {
                    continue;
                }
                String descriptionText = formRecord.descArea.getText();
                String tip = formRecord.tipField.getText();
                String learntPercentText = newWord ? "0 %" : oldModelWordRecord.getLearntPercent();
                ModelWordRecord newModelWordRecord = new ModelWordRecord(wordText, translationText,
                        descriptionText, learntPercentText);

                newModelWordRecord.setNumber(number);

                // copy the previous word record id, which is used for learning cards
                WordRecord newWordRecord = new WordRecord(oldWordRecord);
                newWordRecord.setWordKey(wordKey);
                newWordRecord.setTranslation(translationText);
                newWordRecord.setDescription(descriptionText);
                newWordRecord.setIndex(Integer.parseInt(formRecord.index.getText()));
                newWordRecord.setTip(tip);

                // add examples to newWordRecord
                if (!formRecord.examplesForms.isEmpty()) {
                    for (WordForm.ExampleForm exampleForm : formRecord.examplesForms) {
                        if (!(exampleForm.getWordText().isEmpty() && exampleForm.getTranslationText().isEmpty())) {
                            Example example = new Example(exampleForm.getWordText(), exampleForm.getTranslationText(), exampleForm.getPhraseText());
                            newWordRecord.addExample(example);
                        }
                    }
                }

                // add synonyms to newWordRecord

                // add tags to newWordRecord
                for (TextField tagField : formRecord.tags) {
                    if (!tagField.getText().isEmpty()) {
                        newWordRecord.addTag(tagField.getText());
                    }
                }

                newModelWordRecord.addWordRecord(newWordRecord);

                // Learnt status. Reset learn card
                Status status = Status.get(formRecord.learnStatusChoice.getValue());
                LearnCard card = new LearnCard(newWordRecord.getId(), status);
                newModelWordRecord.setLearnCard(card);

                if (newWord) {
                    model.addNewWord(newModelWordRecord);
                } else {
                    model.updateWord(oldModelWordRecord, newModelWordRecord, status);
                }
            }

            model.updateModel();
            stage.close();
        });

        // todo: show warning to detect erroneous close
        form.cancelButton.setOnAction(actionEvent -> stage.close());
    }

    /**
     * Create <tt>WordForm</tt> model containing list of <tt>ModelWordRecord</tt>s for the word in the parameter.
     * <tt>form.records</tt> contains 10 model <tt>WordForm.RecordModel</tt>s.
     *
     * @param modelWordRecord <tt>ModelWordRecord</tt> that is selected to be edited
     */
    private void populateForm(ModelWordRecord modelWordRecord) {

        final WordRecord wordRecord = modelWordRecord.getWordRecord();
        final WordKey wordKey = new WordKey(wordRecord.getWordKey().getWord(), wordRecord.getWordKey().getPartOfSpeech(),
                modelWordRecord.getNumber());

        // Word container header

        form.wordArea.setText(wordRecord.getWordKey().getWord());
        form.transcriptionField.setText(wordRecord.getWordKey().getTranscription());

        int i = 0;
        switch (wordRecord.getWordKey().getPartOfSpeech()) {
            case Noun: i = 0; break;
            case Verb: i = 1; break;
            case PhrasalVerb: i = 2; break;
            case Adjective: i = 3; break;
            case Adverb: i = 4; break;
            case Preposition: i = 5; break;
        }
        form.posChoice.getSelectionModel().select(i);
        form.numberChoice.getSelectionModel().select(modelWordRecord.getNumber() - 1);

        /* Edit all the records for the given word */

        int k = 0;
        for (ModelWordRecord modelWordRecord_ : model.getData()) {
            WordRecord wordRecord_ = modelWordRecord_.getWordRecord();
            WordKey word_Key_ = new WordKey(wordRecord_.getWordKey().getWord(), wordRecord_.getWordKey().getPartOfSpeech(),
                    modelWordRecord_.getNumber());

            if (wordKey.equals(word_Key_)) {

                WordForm.RecordModel recordModel = form.records.get(k);
                recordModel.modelWordRecord = modelWordRecord_;
                recordModel.translationArea.setText(wordRecord_.getTranslation());
                recordModel.descArea.setText(wordRecord_.getDescription());
                recordModel.index.setText(String.valueOf(wordRecord_.getIndex()));
                recordModel.tipField.setText(wordRecord_.getTip());

                recordModel.addExampleButton.setOnAction(actionEvent -> form.createTab(recordModel));

                // Examples

                List<Example> examples = wordRecord_.getExamples();

                if (examples.size() != 0) {
                    int j = 0;
                    for (Example example : examples) {
                        if (j == 0) {
                            WordForm.ExampleForm exampleForm0 = recordModel.examplesForms.get(0);
                            exampleForm0.wordText.setText(example.getWordExample());
                            exampleForm0.translationText.setText(example.getTranslationExample());
                            exampleForm0.phrase.setText(example.getPhrase());
                        } else {
                            form.createTab(recordModel, example);
                        }
                        j++;
                    }
                }

                // Tags

                List<String> tags = wordRecord_.getTags();
                if (!tags.isEmpty()) {
                    int j = 0;
                    for (String tag : tags) {
                        recordModel.tags[j++].setText(tag);
                    }
                }

                k++;
            }
        }
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
