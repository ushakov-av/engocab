package ru.mipt.engocab.ui.fx.controller.wordform;

import javafx.event.Event;
import javafx.scene.control.TextField;
import ru.mipt.engocab.core.model.Example;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;
import ru.mipt.engocab.ui.fx.model.Model;
import ru.mipt.engocab.ui.fx.model.ModelWordRecord;
import ru.mipt.engocab.ui.fx.view.wordform.WordForm;

/**
 * @author Alexander Ushakov
 */
public class WordEditController {

    private Model model;

    private WordForm form;

    public WordEditController(Model model) {
        this.model = model;
    }

    public void setForm(WordForm form) {
        this.form = form;
    }

    public void saveWord(Event event) {
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
        form.stage.close();
    }

}
