package ru.mipt.engocab.ui.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.study.LearnCard;

/**
 * WordRecord model for displaying in list.
 *
 * @author Alexander V. Ushakov
 */
public class ModelWordRecord {

    // Column "learn percent"
    private StringProperty learntPercent;

    // Column "word"
    private StringProperty word;

    // Translation index. Column "№"
    private StringProperty translationNumber = new SimpleStringProperty();

    // Column "translation"
    private StringProperty translation;

    // Column "description"
    private StringProperty description;

    // Word index among the words with the same spelling
    private int number;

    // WordRecord
    private WordRecord wordRecord;

    // LearnCard for WordRecord
    private LearnCard learnCard;

    public ModelWordRecord(String fName, String lName, String description, String learnt) {
        this.word = new SimpleStringProperty(fName);
        this.translation = new SimpleStringProperty(lName);
        this.description = new SimpleStringProperty(description);
        this.learntPercent = new SimpleStringProperty(learnt);
    }

    public StringProperty translationNumberProperty() {
        return translationNumber;
    }

    public String getTranslationNumber() {
        return translationNumber.get();
    }

    public void setTranslationNumber(String translationNumber) {
        this.translationNumber.set(translationNumber);
    }

    public void setTranslationNumber(int translationNumber) {
        this.translationNumber.set(String.valueOf(translationNumber));
    }

    public StringProperty wordProperty() {
        return word;
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public StringProperty translationProperty() {
        return translation;
    }

    public String getTranslation() {
        return translationNumber + ". " + translation.get();
    }

    public void setTranslation(String translation) {
        this.translation.set(translation);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLearntPercent() {
        return learntPercent.get();
    }

    public StringProperty learntPercentProperty() {
        return learntPercent;
    }

    public void setLearntPercent(String learntPercent) {
        this.learntPercent.set(learntPercent);
    }

    public void addWordRecord(WordRecord record) {
        this.wordRecord = record;
    }

    public WordRecord getWordRecord() {
        return wordRecord;
    }

    public LearnCard getLearnCard() {
        return learnCard;
    }

    public void setLearnCard(LearnCard learnCard) {
        learntPercent.set(learnCard.getLearnPercent());
        this.learnCard = learnCard;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelWordRecord record = (ModelWordRecord) o;

        if (wordRecord != null ? !wordRecord.equals(record.wordRecord) : record.wordRecord != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return wordRecord != null ? wordRecord.hashCode() : 0;
    }
}
