package ru.mipt.engocab.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * WordRecord contains all the information about the word translation.
 *
 * @author Alexander V. Ushakov
 */
@SuppressWarnings("unchecked")
public class WordRecord {

    // record id
    private String id;
    // index in the container, shows the priority of the translation entry
    private int index;

    private WordKey wordKey;

    private String translation;
    private String tip;
    private String description;

    private List<Example> examples = Collections.EMPTY_LIST;
    private List<Synonym> synonyms = Collections.EMPTY_LIST;
    private List<String> tags = Collections.EMPTY_LIST;


    public WordRecord(WordKey wordKey) {
        this.id = UUID.randomUUID().toString();
        this.wordKey = wordKey;
    }

    public WordRecord(WordRecord oldRecord) {
        this.id = oldRecord == null ? UUID.randomUUID().toString() : oldRecord.getId();
    }

    public WordRecord(String id,
                      int index,
                      WordKey wordKey,
                      String translation,
                      String tip,
                      String description) {
        this.id = id;
        this.index = index;
        this.wordKey = wordKey;
        this.translation = translation;
        this.tip = tip;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public WordKey getWordKey() {
        return wordKey;
    }

    public void setWordKey(WordKey wordKey) {
        this.wordKey = wordKey;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public void addExample(Example example) {
        if (examples.isEmpty()) {
            //todo:
            examples = new ArrayList<>();
        }
        examples.add(example);
    }

    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    public void addSynonym(Synonym synonym) {
        if (synonyms.isEmpty()) {
            //todo:
            synonyms = new ArrayList<>();
        }
        synonyms.add(synonym);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        if (tags.isEmpty()) {
            //todo:
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordRecord that = (WordRecord) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
