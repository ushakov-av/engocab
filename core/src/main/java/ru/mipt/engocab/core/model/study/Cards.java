package ru.mipt.engocab.core.model.study;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores learnt and active cards.
 *
 * @author Alexander V. Ushakov
 */
public class Cards {

    private List<LearnCard> learnt;
    private List<LearnCard> active;

    public Cards() {
        learnt = new ArrayList<>();
        active = new ArrayList<>();
    }

    public Cards(List<LearnCard> learnt, List<LearnCard> active) {
        this.learnt = learnt;
        this.active = active;
    }

    public List<LearnCard> getLearnt() {
        return learnt;
    }

    public List<LearnCard> getActive() {
        return active;
    }

    public void addActive(LearnCard card) {
        active.add(card);
    }

    public void addLearnt(LearnCard card) {
        learnt.add(card);
    }

    public LearnCard getCard(String id) {
        for (LearnCard card : learnt) {
            if (card.getRecordId().equalsIgnoreCase(id)) {
                return card;
            }
        }
        for (LearnCard card : active) {
            if (card.getRecordId().equalsIgnoreCase(id)) {
                return card;
            }
        }
        return null;
    }

    public void changeStatus(LearnCard learnCard, Status status) {
        Preconditions.checkArgument(learnCard.getStatus() != status, "Status must be changed");

        if (learnCard.getStatus() == Status.ACTIVE && status == Status.LEARNT) {
            learnCard.setStatus(Status.LEARNT);
            learnCard.setL(20);
            learnCard.setLr(20);
            active.remove(learnCard);
            learnt.add(learnCard);
        } else {
            learnCard.setStatus(Status.ACTIVE);
            learnCard.setL(0);
            learnCard.setLr(0);
            active.add(learnCard);
            learnt.remove(learnCard);
        }
    }

    public void removeCard(LearnCard learnCard) {
        switch (learnCard.getStatus()) {
            case LEARNT:
                learnt.remove(learnCard);
                break;
            case ACTIVE:
                active.remove(learnCard);
                break;
        }
    }

}
