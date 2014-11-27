package ru.mipt.engocab.core.model;

/**
* @author Alexander V. Ushakov
*/
public enum PartOfSpeech {
    Noun,
    Verb,
    PhrasalVerb,
    Adverb,
    Adjective,
    Preposition;

    public static PartOfSpeech toPartOfSpeech(String posStr) {
        switch (posStr) {
            case "Noun": return Noun;
            case "Verb": return Verb;
            case "PhrasalVerb": return PhrasalVerb;
            case "Phrasal Verb": return PhrasalVerb;
            case "Phrasal verb": return PhrasalVerb;
            case "Adverb": return Adverb;
            case "Adjective": return Adjective;
            case "Preposition": return Preposition;
            default:
                throw new IllegalArgumentException("Unknown PartOfSpeech : " + posStr);
        }
    }

    public static String value(PartOfSpeech pos) {
        switch (pos) {
            case Noun: return "Noun";
            case Verb: return "Verb";
            case PhrasalVerb: return "Phrasal Verb";
            case Adverb: return "Adverb";
            case Adjective: return "Adjective";
            case Preposition: return "Preposition";
        }
        throw new IllegalArgumentException("Unknown PartOfSpeech");
    }

    public static int getHashCode(PartOfSpeech pos) {
        switch (pos) {
            case Noun: return 1;
            case Verb: return 2;
            case PhrasalVerb: return 3;
            case Adverb: return 4;
            case Adjective: return 5;
            case Preposition: return 6;
        }
        throw new IllegalArgumentException("Unknown PartOfSpeech");
    }
}
