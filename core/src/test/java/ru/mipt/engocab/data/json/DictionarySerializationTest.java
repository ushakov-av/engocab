package ru.mipt.engocab.data.json;

import org.junit.Test;
import ru.mipt.engocab.core.model.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Ushakov
 */
public class DictionarySerializationTest {

    @Test
    public void testSerialize() throws IOException {
        Dictionary dic = createDictionary();

        String serialized = DataMapper.serialize(dic);

        dic = DataMapper.deserialize(serialized, Dictionary.class);

        String serialized2 = DataMapper.serialize(dic);

        assertEquals(serialized, serialized2);
    }

    private Dictionary createDictionary() {

        final PartOfSpeech pos = PartOfSpeech.Noun;
        final int number = 1;

        Dictionary dictionary = new Dictionary();

        String enWord = "hello";
        String ruWord = "привет";
        String ruWord2 = "привет2";

        WordKey wordKey = new WordKey(enWord, pos, number);
        WordRecord record = new WordRecord(wordKey);
        record.setTranslation(ruWord);
        record.setIndex(1);

        Example example = new Example("hello again", "снова привет");
        record.addExample(example);

        example = new Example("hello alex", "привет алекс", "hello smbd");
        record.addExample(example);

        record.addTag("longman");
        record.addTag("basic4000");

        dictionary.addRecord(record);

        record = new WordRecord(wordKey);
        record.setTranslation(ruWord2);
        record.setIndex(2);

        dictionary.addRecord(record);

        enWord = "world";
        ruWord = "мир";

        wordKey = new WordKey(enWord, pos, number);
        record = new WordRecord(wordKey);
        record.setTranslation(ruWord);
        record.setIndex(1);

        dictionary.addRecord(record);

        return dictionary;
    }
}
