package ru.mipt.engocab.core.model.study;

/**
 * @author Alexander V. Ushakov
 */
public class LearnCard {

    // recordId references (WordRecord id)
    private String recordId;
    // status
    private Status status;
    // total number
    private int n;
    // learnt
    private int l;
    // learnt reverse
    private int lr;

    public LearnCard(String recordId, Status status, int n, int l, int lr) {
        this.recordId = recordId;
        this.status = status;
        this.n = n;
        this.l = l;
        this.lr = lr;
    }

    public LearnCard(String recordId) {
        this.status = Status.ACTIVE;
        this.recordId = recordId;
        this.n = 20;
        this.l = 0;
        this.lr = 0;
    }

    public LearnCard(String recordId, Status status) {
        this.recordId = recordId;
        this.status = status;
        this.n = 20;
        this.l = (status == Status.LEARNT) ? 20 : 0;
        this.lr = (status == Status.LEARNT) ? 20 : 0;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getLr() {
        return lr;
    }

    public void setLr(int lr) {
        this.lr = lr;
    }

    public void incrementLearnt() {
        this.l++;
    }

    public void decrementLearnt() {
        if (l > 0) {
            l--;
        }
    }

    public void incrementLearntReverse() {
        lr++;
    }

    public void decrementLearntReverse() {
        if (lr > 0) {
            lr--;
        }
    }

    public boolean isLearntDirect() {
        return l == n;
    }

    public boolean isLearntReverse() {
        return lr == n;
    }

    public boolean isLearnt() {
        return l == n && lr == n;
    }

    public String getLearnPercent() {
        String prefix = (l < 2) ? "0" : "";
        String prefix2 = (lr < 2) ? "0" : "";
        return prefix + (int)((l / (double) n) * 100) + ", " + prefix2 + (int)((lr / (double) n) * 100) + " %";
    }

    public int getLearntReversePercent() {
        return (int)((lr / (double) n) * 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LearnCard learnCard = (LearnCard) o;

        if (!recordId.equals(learnCard.recordId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return recordId.hashCode();
    }
}
