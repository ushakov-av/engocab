package ru.mipt.engocab.data.json;

import org.junit.Test;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.study.Index;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Ushakov
 */
public class IndexDeserializationTest {

    @Test
    public void testDeserialization() {
        InputStream stream = DictionaryDeserializationTest.class.getResourceAsStream("/serialized_index.json");
        Index index = DataMapper.deserializeFromStream(stream, Index.class);
        assertThat(index.getEntries().size(), equalTo(4));
        {
            WordKey wordKey = index.getWord("c9444c55-a2f1-492a-80d7-6d3d72126a8c");
            assertThat(wordKey.getWord(), equalTo("afraid"));
            assertThat(wordKey.getPartOfSpeech(), equalTo(PartOfSpeech.Adjective));
            assertThat(wordKey.getNumber(), is(1));
        }
        {
            WordKey wordKey = index.getWord("2acefdda-d692-41f5-b5f8-a3fcd95db7e9");
            assertThat(wordKey.getWord(), equalTo("agree"));
            assertThat(wordKey.getPartOfSpeech(), equalTo(PartOfSpeech.Verb));
            assertThat(wordKey.getNumber(), is(1));
        }
        {
            WordKey wordKey = index.getWord("f543c9dd-ed97-4615-99ff-7d992fadd092");
            assertThat(wordKey.getWord(), equalTo("angry"));
            assertThat(wordKey.getPartOfSpeech(), equalTo(PartOfSpeech.Adjective));
            assertThat(wordKey.getNumber(), is(1));
        }
        {
            WordKey wordKey = index.getWord("2f9487a8-fd02-4cd6-b90d-4ae51745917b");
            assertThat(wordKey.getWord(), equalTo("arrive"));
            assertThat(wordKey.getPartOfSpeech(), equalTo(PartOfSpeech.Verb));
            assertThat(wordKey.getNumber(), is(1));
        }
    }
}
