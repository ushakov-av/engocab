package ru.mipt.engocab.ui.fx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.WordContainer;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.Index;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * UI Model.
 *
 * @author Alexander V. Ushakov
 */
public class Model {

    private ObservableList<ModelWordRecord> data = FXCollections.observableArrayList();

    private Dictionary dictionary;
    private Cards cards;
    private Index index;

    private String activeTag;

    public Model() {
        dictionary = new Dictionary();
        cards = new Cards();
        index = new Index();
    }

    public void updateModel() {
        //TODO: make correct refresh of the table view
        updateModel(dictionary, cards, index);
    }

    /**
     * Update model from dictionary, cards, index.
     *
     * @param dictionary dictionary
     * @param cards cards
     * @param index index
     */
    public void updateModel(Dictionary dictionary, Cards cards, Index index) {
        data.clear();
        this.index = index;
        this.dictionary = dictionary;
        this.cards = cards;
        for (WordContainer container : dictionary.getContainers()) {
            int number = container.getNumber();
            container.getRecords().sort((WordRecord r1, WordRecord r2) -> Integer.compare(r1.getIndex(), r2.getIndex()));
            for (WordRecord wordRecord : container.getRecords()) {
                LearnCard card = cards.getCard(wordRecord.getId());

                ModelWordRecord modelWordRecord = new ModelWordRecord(wordRecord.getWordKey().getWord(),
                        wordRecord.getTranslation(), wordRecord.getDescription(), card.getLearnPercent());

                modelWordRecord.addWordRecord(wordRecord);
                modelWordRecord.setLearnCard(card);
                modelWordRecord.setTranslationNumber(wordRecord.getIndex());
                modelWordRecord.setNumber(number);
                data.add(modelWordRecord);
            }
        }
    }

    /**
     * Adds new word to the dictionary. It is guaranteed that the word is not in the dictionary.
     *
     * @param modelRecord model word record
     */
    public void addNewWord(ModelWordRecord modelRecord) {
        WordRecord wordRecord = modelRecord.getWordRecord();
        dictionary.addRecord(wordRecord);
        LearnCard card = modelRecord.getLearnCard();
        switch (card.getStatus()) {
            case ACTIVE: cards.addActive(card); break;
            case LEARNT: cards.addLearnt(card); break;
            default:
                throw new RuntimeException("Unknown status");
        }
        index.addWord(wordRecord.getId(), wordRecord.getWordKey());
        data.add(modelRecord);
    }

    /**
     * Add a word from <tt>WordEnterForm</tt>.
     *
     * //todo: instead of returning false on addition attempt, detect existence asap
     *
     * @param wordKey word
     * @param modelRecords modelRecord
     * @return <tt>true</tt> if word was added, <tt>false</tt> otherwise
     */
    public boolean addWord(WordKey wordKey, List<ModelWordRecord> modelRecords) {
        if (!dictionary.contains(wordKey)) {
            modelRecords.forEach(this::addNewWord);
            return true;
        }
        return false;
    }

    /**
     * Update word record. <tt>oldModelWordRecord</tt> is not <tt>null</tt>.
     * <p/>
     * If translation text is empty the word will be removed.
     * <p/>
     * If <tt>newStatus</tt> is active and wasn't changed than set it to 0 %.
     *
     * @param oldModelWordRecord old <tt>ModelWordRecord</tt>
     * @param newModelWordRecord new <tt>ModelWordRecord</tt>
     * @param newStatus <tt>Status</tt>
     */
    public void updateWord(ModelWordRecord oldModelWordRecord, ModelWordRecord newModelWordRecord, Status newStatus) {

        WordRecord newWordRecord = newModelWordRecord.getWordRecord();
        WordRecord oldWordRecord = oldModelWordRecord.getWordRecord();
        LearnCard learnCard = cards.getCard(newWordRecord.getId());

        // Remove if translation is empty
        if (newWordRecord.getTranslation().trim().isEmpty()) {
            dictionary.removeRecord(oldWordRecord);
            index.removeWord(oldWordRecord.getId());
            cards.removeCard(learnCard);
        }

        if (!(newWordRecord.getWordKey().getWord().equalsIgnoreCase(oldWordRecord.getWordKey().getWord())
                && newWordRecord.getWordKey().getPartOfSpeech() == oldWordRecord.getWordKey().getPartOfSpeech()
                && oldWordRecord.getWordKey().getNumber() == newWordRecord.getWordKey().getNumber())) {

            /* Process change of word (en, pos, number). Either one of fields has changed */

            dictionary.removeRecord(oldWordRecord);
            dictionary.addRecord(newWordRecord);

            // Refresh index. Map old record id to new word
            index.addWord(newWordRecord.getId(), new WordKey(newWordRecord.getWordKey().getWord(),
                    newWordRecord.getWordKey().getPartOfSpeech(), newModelWordRecord.getNumber()));

            // New en word, set 0% learnt
            learnCard.setL(0);
            learnCard.setLr(0);

        } else {

            /* Update word */

            dictionary.removeRecord(oldWordRecord);
            dictionary.addRecord(newWordRecord);
        }

        if (learnCard.getStatus() != newStatus) {
            // set 100% if change active to learnt
            cards.changeStatus(learnCard, newStatus);
        } else if (!(newWordRecord.getWordKey().getTranscription().equalsIgnoreCase(oldWordRecord.getWordKey().getTranscription())
                && newWordRecord.getDescription().equalsIgnoreCase(oldModelWordRecord.getDescription()))
                && newStatus == Status.ACTIVE) {
            // new translation or description, so learnt status is 0%
            learnCard.setL(0);
            learnCard.setLr(0);
        }

        newModelWordRecord.setLearnCard(learnCard);
    }

    public void changeWordLearntStatus(LearnCard learnCard, Status status) {
        cards.changeStatus(learnCard, status);
    }

    public ObservableList<ModelWordRecord> getData() {
        return data;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public Cards getCards() {
        return cards;
    }

    public Index getIndex() {
        return index;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getActiveTag() {
        return activeTag;
    }

    /**
     *
     * @param direct
     * @param tag
     * @return
     */
    public Lesson createCheckLesson(boolean direct, String tag) {
        Lesson lesson = new Lesson();
        List<LearnCard> learnCards = cards.getActive();
        int size = learnCards.size();
        if (size <= 10) {
            for (LearnCard card : learnCards) {
                WordKey wordKey = index.getWord(card.getRecordId());
                WordRecord record = dictionary.getWordRecord(wordKey, card.getRecordId());
                if (!checkLearnt(card, direct) && isSuitableWord(record, tag)) {
                    lesson.addEntry(new LessonEntry(card, index.getWord(card.getRecordId())));
                }
            }
        } else {
            Random rn = new Random(System.currentTimeMillis());
            Set<Integer> distinct = new HashSet<>();
            int attemptNumber = 0;
            while (distinct.size() < 10 && attemptNumber < 100) {
                int arrInd = rn.nextInt(size);
                if (distinct.add(arrInd)) {
                    System.out.println("Added " + arrInd);
                    LearnCard card = learnCards.get(arrInd);
                    WordKey wordKey = index.getWord(card.getRecordId());
                    WordRecord record = dictionary.getWordRecord(wordKey, card.getRecordId());
                    if (!checkLearnt(card, direct) && isSuitableWord(record, tag)) {
                        LessonEntry entry = new LessonEntry(card, index.getWord(card.getRecordId()));
                        lesson.addEntry(entry);
                    }
                }
                attemptNumber++;
            }
        }
        return lesson;
    }

    /**
     *
     * @param record
     * @param tag
     * @return
     */
    private boolean isSuitableWord(WordRecord record, String tag) {
        if (tag.isEmpty()) {
            return true;
        }
        if (record.getTags() == null) {
            return false;
        }
        for (String t : record.getTags()) {
            if (t.equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param tag
     * @return
     */
    public Lesson createLearnLesson(String tag) {
        Lesson lesson = new Lesson();
        List<LearnCard> learnCards = cards.getActive();
        for (LearnCard card : learnCards) {
            WordKey wordKey = index.getWord(card.getRecordId());
            WordRecord record = dictionary.getWordRecord(wordKey, card.getRecordId());
            if (record == null) {
                System.out.println("Word " + wordKey + " id " + card.getRecordId());
                continue;
            }
            if (isSuitableWord(record, tag)) {
                lesson.addEntry(new LessonEntry(card, wordKey));
            }
        }
        return lesson;
    }

    /**
     * Returns all the tags with the number of words marked with this tag
     *
     * @return a map of tags to the number of words having this tag
     */
    public Map<String, Integer> getTags() {
        Map<String, Integer> tagsMap = new HashMap<>();
        for (WordContainer container : dictionary.getContainers()) {
            for (WordRecord record : container.getRecords()) {
                if (record.getTags() != null) {
                    for (String tag : record.getTags()) {
                        if (!tag.isEmpty()) {
                            if (tagsMap.get(tag) == null) {
                                tagsMap.put(tag, 0);
                            }
                            Integer prevValue = tagsMap.get(tag);
                            tagsMap.put(tag, prevValue + 1);
                        }
                    }
                }
            }
        }
        return tagsMap;
    }

    /**
     *
     * @return
     */
    public Map<Integer, Integer> getReverseLearntStatistics() {
        Map<Integer, Integer> statistics = new TreeMap<>();
        for (LearnCard card : cards.getActive()) {
            int percent = card.getLearntReversePercent();
            Integer learnt = statistics.get(percent);
            if (learnt == null) {
                statistics.put(percent, 1);
            } else {
                statistics.put(percent, ++learnt);
            }
        }
        if (statistics.get(100) == null) {
            statistics.put(100, 0);
        }
        for (LearnCard card : cards.getLearnt()) {
            statistics.put(100, statistics.get(100) + 1);
        }
        return statistics;
    }

    /**
     *
     * @param card
     * @param direct
     * @return
     */
    private boolean checkLearnt(LearnCard card, boolean direct) {
        return direct ? card.isLearntDirect() : card.isLearntReverse();
    }
}
