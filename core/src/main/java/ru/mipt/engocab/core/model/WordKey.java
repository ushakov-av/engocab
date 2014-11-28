package ru.mipt.engocab.core.model;

/**
 * @author Alexander V. Ushakov
 */
public class WordKey implements Comparable<WordKey> {

    private final String word;
    private final PartOfSpeech partOfSpeech;
    private final int number;

    private String transcription;

    public WordKey(String word, PartOfSpeech partOfSpeech, int number) {
        this(word, partOfSpeech, number, null);
    }

    public WordKey(String word, PartOfSpeech partOfSpeech, int number, String transcription) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.number = number;
        this.transcription = transcription;
    }

    public String getWord() {
        return word;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public int getNumber() {
        return number;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordKey that = (WordKey) o;

        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + PartOfSpeech.getHashCode(partOfSpeech);
        result = 31 * result + number;
        return result;
    }

    @Override
    public int compareTo(WordKey that) {
        if (!word.equals(that.word)) {
            return word.compareTo(that.word);
        }
        if (partOfSpeech != that.partOfSpeech) {
            return Integer.compare(PartOfSpeech.getHashCode(partOfSpeech), PartOfSpeech.getHashCode(that.partOfSpeech));
        }
        return Integer.compare(number, that.number);
    }

    @Override
    public String toString() {
        return "WordKey{" +
                "word='" + word + '\'' +
                ", partOfSpeech=" + partOfSpeech +
                ", number=" + number +
                ", transcription='" + transcription + '\'' +
                '}';
    }
}
