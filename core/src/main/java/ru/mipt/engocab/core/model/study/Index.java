package ru.mipt.engocab.core.model.study;

import ru.mipt.engocab.core.model.WordKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps <tt>WordRecord</tt> UUID ids to <tt>Word</tt>.
 *
 * @author Alexander V. Ushakov
 */
public class Index {

    // Maps record id to word
    private Map<String, WordKey> idToWord = new HashMap<>();

    public Index() {
    }

    public Index(Map<String, WordKey> idToWord) {
        this.idToWord = idToWord;
    }

    public WordKey getWord(String id) {
        return idToWord.get(id);
    }

    public void addWord(String id, WordKey wordKey) {
        idToWord.put(id, wordKey);
    }

    public void removeWord(String id) {
        idToWord.remove(id);
    }

    public Set<Map.Entry<String, WordKey>> getEntries() {
        return idToWord.entrySet();
    }

}
