package ru.mipt.engocab.core.model;

/**
* @author Alexander V. Ushakov
*/
public class Example {

    private String wordExample;
    private String translationExample;
    // use of the word with preposition in the example
    private String phrase;

    public Example() {
    }

    public Example(String wordExample, String translationExample) {
        this(wordExample, translationExample, null);
    }

    public Example(String wordExample, String translationExample, String phrase) {
        this.wordExample = wordExample;
        this.translationExample = translationExample;
        this.phrase = phrase;
    }

    public String getWordExample() {
        return wordExample;
    }

    public String getTranslationExample() {
        return translationExample;
    }

    public String getPhrase() {
        return phrase;
    }

    @Override
    public String toString() {
        return "Example{" +
                "wordExample='" + wordExample + '\'' +
                ", translationExample='" + translationExample + '\'' +
                ", phrase='" + phrase + '\'' +
                '}';
    }
}
