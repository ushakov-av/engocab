package ru.mipt.engocab.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * WordContainer is a container for (word, partOfSpeech, number). It contains a list of translations.
 *
 * - word    - word (en word, partOfSpeech, number, transcription)
 * - records - list of translations
 */
public class WordContainer implements Comparable<WordContainer> {

    private WordKey wordKey;

    private List<WordRecord> records;

    public WordContainer(WordKey wordKey) {
        this.wordKey = wordKey;
        this.records = new ArrayList<>();
    }

    public WordContainer(WordKey wordKey, List<WordRecord> records) {
        this.wordKey = wordKey;
        this.records = records;
    }

    public WordKey getWordKey() {
        return wordKey;
    }

    public void setWordKey(WordKey wordKey) {
        this.wordKey = wordKey;
    }

    public List<WordRecord> getRecords() {
        return records;
    }

    public void addRecord(WordRecord record) {
        records.add(record);
    }

    public void setRecords(List<WordRecord> records) {
        this.records = records;
    }

    public String getWord() {
        return wordKey.getWord();
    }

    public PartOfSpeech getPartOfSpeech() {
        return wordKey.getPartOfSpeech();
    }

    public int getNumber() {
        return wordKey.getNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return compareTo((WordContainer) o) == 0;
    }

    @Override
    public int hashCode() {
        return wordKey.hashCode();
    }

    @Override
    public int compareTo(WordContainer that) {
        return wordKey.compareTo(that.getWordKey());
    }
}
