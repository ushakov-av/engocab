package ru.mipt.engocab.core.model.study;

/**
 * @author Alexander V. Ushakov
 */
public enum Status {
    LEARNT,
    ACTIVE;

    public static Status get(String value) {
        switch (value) {
            case "To learn": return ACTIVE;
            case "Learnt": return LEARNT;
            default:
                throw new IllegalArgumentException("Unknown status");
        }
    }
}
