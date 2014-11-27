package ru.mipt.engocab.core.model;

/**
 * @author Alexander V. Ushakov
 */
public class Synonym {

    // points to record id
    private String id;

    private WordKey wordKey;

    public Synonym(String id, WordKey wordKey) {
        this.id = id;
        this.wordKey = wordKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WordKey getWordKey() {
        return wordKey;
    }

    public void setWordKey(WordKey wordKey) {
        this.wordKey = wordKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Synonym synonym = (Synonym) o;

        if (wordKey != null ? !wordKey.equals(synonym.wordKey) : synonym.wordKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return wordKey != null ? wordKey.hashCode() : 0;
    }
}
