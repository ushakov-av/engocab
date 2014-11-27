package ru.mipt.engocab.ui.fx.model;

import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.study.LearnCard;

/**
 * @author Alexander V. Ushakov
 */
public class LessonEntry {

    private LearnCard learnCard;
    private WordKey wordKey;

    public LessonEntry(LearnCard learnCard, WordKey wordKey) {
        this.learnCard = learnCard;
        this.wordKey = wordKey;
    }

    public LearnCard getLearnCard() {
        return learnCard;
    }

    public WordKey getWordKey() {
        return wordKey;
    }
}
