package ru.mipt.engocab.ui.fx.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander V. Ushakov
 */
public class Lesson {

    private List<LessonEntry> cards = new ArrayList<>();

    public Lesson() {
    }

    public Lesson(List<LessonEntry> cards) {
        this.cards = cards;
    }

    public List<LessonEntry> getCards() {
        return cards;
    }

    public void addEntry(LessonEntry entry) {
        cards.add(entry);
    }
}
