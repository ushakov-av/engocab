package ru.mipt.engocab.core.model;

import com.google.common.base.Preconditions;

import java.util.*;

/**
 * Dictionary. Maps words to containers.
 *
 * @author Alexander Ushakov
 */
public class Dictionary {

    private Map<WordKey, WordContainer> wordContainers = new HashMap<>();

    public WordContainer getWordContainer(WordKey wordKey) {
        return wordContainers.get(wordKey);
    }

    /**
     * Adds a word container to the dictionary. Throws exception if dictionary already contains container for
     * the word key.
     *
     * @param container container to be added
     */
    public void addContainer(WordContainer container) {
        WordKey wordKey = container.getWordKey();
        Preconditions.checkArgument(!contains(wordKey), "Dictionary already contains container");
        wordContainers.put(wordKey, container);
    }

    /**
     * Adds a word record to the dictionary.
     *
     * @param record record
     */
    public void addRecord(WordRecord record) {
        WordKey wordKey = record.getWordKey();
        WordContainer container = wordContainers.get(wordKey);
        if (container == null) {
            container = new WordContainer(wordKey);
            wordContainers.put(wordKey, container);
        }
        container.addRecord(record);
    }

    /**
     * Checks whether the dictionary contains a container for the given word key.
     *
     * @param wordKey word key
     * @return <tt>true</tt> if the dictionary contains word key, <tt>false</tt> otherwise
     */
    public boolean contains(WordKey wordKey) {
        return wordContainers.get(wordKey) != null;
    }

    /**
     * Removes a word record from the dictionary. If the given record is the last
     * to remove, then remove the container from the dictionary.
     *
     * @param record record
     */
    public void removeRecord(WordRecord record) {
        WordContainer container = wordContainers.get(record.getWordKey());
        if (container != null) {
            List<WordRecord> records = container.getRecords();
            records.remove(record);
            if (records.isEmpty()) {
                wordContainers.remove(record.getWordKey());
            }
        }
    }

    /**
     * Returns a word record with the given word key and record id.
     *
     * @param wordKey word key
     * @param recordId record id
     * @return a word record
     */
    public WordRecord getWordRecord(WordKey wordKey, String recordId) {
        WordContainer container = wordContainers.get(wordKey);
        if (container == null) {
            System.out.println(wordKey + " " + recordId);
            return null;
        }
        for (WordRecord record : container.getRecords()) {
            if (record.getId().equalsIgnoreCase(recordId)) {
                return record;
            }
        }
        return null;
    }

    public Set<WordContainer> getContainers() {
        return new TreeSet<>(wordContainers.values());
    }

}
