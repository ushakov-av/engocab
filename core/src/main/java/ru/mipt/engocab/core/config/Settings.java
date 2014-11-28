package ru.mipt.engocab.core.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Alexander Ushakov
 */
public class Settings {

    @JsonProperty("dictPath")
    private String dictionariesPath;
    @JsonProperty("curDict")
    private String currentDictionary;

    @JsonCreator
    public Settings() {
    }

    public String getDictionariesPath() {
        return dictionariesPath;
    }

    public void setDictionariesPath(String dictionariesPath) {
        this.dictionariesPath = dictionariesPath;
    }

    public String getCurrentDictionary() {
        return currentDictionary;
    }

    public void setCurrentDictionary(String currentDictionary) {
        this.currentDictionary = currentDictionary;
    }
}
