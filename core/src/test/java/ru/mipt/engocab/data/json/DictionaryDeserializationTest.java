package ru.mipt.engocab.data.json;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import ru.mipt.engocab.core.model.Example;
import ru.mipt.engocab.core.model.WordContainer;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.WordRecord;
import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.PartOfSpeech;

import java.io.InputStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Ushakov
 */
public class DictionaryDeserializationTest {

    @Test
    public void testDeserialization() {
        InputStream stream = DictionaryDeserializationTest.class.getResourceAsStream("/serialized_dictionary.json");
        Dictionary dictionary = DataMapper.deserializeFromStream(stream, Dictionary.class);

        /* "hello" word */

        WordKey wordKey = new WordKey("hello", PartOfSpeech.Noun, 1);

        WordContainer wordContainer = dictionary.getWordContainer(wordKey);
        assertThat(wordContainer.getWordKey(), CoreMatchers.equalTo(wordKey));
        assertNull(wordContainer.getWordKey().getTranscription());
        assertThat(wordContainer.getRecords().size(), equalTo(2));

        WordRecord wordRecord =  wordContainer.getRecords().get(0);
        assertThat(wordRecord.getWordKey(), equalTo(wordKey));

        assertThat(wordRecord.getId(), equalTo("089112b4-288e-4e49-99f2-42472404ce3a"));
        assertThat(wordRecord.getIndex(), equalTo(1));
        assertThat(wordRecord.getTranslation(), equalTo("привет"));
        assertThat(wordRecord.getTip(), equalTo("Some Tip"));
        assertThat(wordRecord.getDescription(), equalTo("hello is used for greeting"));

        // examples
        assertThat(wordRecord.getExamples().size(), equalTo(2));

        Example example = wordRecord.getExamples().get(0);
        assertThat(example.getWordExample(), equalTo("hello again"));
        assertNull(example.getTranslationExample());
        assertNull(example.getPhrase());

        example = wordRecord.getExamples().get(1);
        assertThat(example.getWordExample(), equalTo("hello alex"));
        assertThat(example.getTranslationExample(), equalTo("привет алекс"));
        assertThat(example.getPhrase(), equalTo("hello smbd"));

        // synonyms
        assertThat(wordRecord.getSynonyms().size(), equalTo(0));

        // tags
        assertThat(wordRecord.getTags().size(), equalTo(2));
        assertThat(wordRecord.getTags().get(0), equalTo("longman"));
        assertThat(wordRecord.getTags().get(1), equalTo("basic4000"));


        wordRecord =  wordContainer.getRecords().get(1);

        assertThat(wordRecord.getId(), equalTo("0da3edf3-2153-4d7b-93fe-9f58f4d75d6e"));
        assertThat(wordRecord.getIndex(), equalTo(2));
        assertThat(wordRecord.getTranslation(), equalTo("привет2"));
        assertNull(wordRecord.getTip());
        assertNull(wordRecord.getDescription());
        assertThat(wordRecord.getExamples().size(), equalTo(0));
        assertThat(wordRecord.getSynonyms().size(), equalTo(0));
        assertThat(wordRecord.getTags().size(), equalTo(0));


        /* "world" word */

        wordKey = new WordKey("world", PartOfSpeech.Noun, 1);

        wordContainer = dictionary.getWordContainer(wordKey);
        assertThat(wordContainer.getWordKey(), CoreMatchers.equalTo(wordKey));
        assertThat(wordContainer.getWordKey().getTranscription(), equalTo("world"));
        assertThat(wordContainer.getRecords().size(), equalTo(1));

        wordRecord =  wordContainer.getRecords().get(0);
        assertThat(wordRecord.getIndex(), equalTo(1));
        assertThat(wordRecord.getId(), equalTo("bea324b9-abcb-4fbf-a653-1015f3a3cf24"));
        assertThat(wordRecord.getTranslation(), equalTo("мир"));
        assertNull(wordRecord.getTip());
        assertNull(wordRecord.getDescription());
        assertThat(wordRecord.getExamples().size(), equalTo(0));
        assertThat(wordRecord.getSynonyms().size(), equalTo(0));
        assertThat(wordRecord.getTags().size(), equalTo(0));
    }
}
