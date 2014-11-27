package ru.mipt.engocab.data.json;

import org.junit.Test;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.study.Index;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Ushakov
 */
public class IndexSerializationTest {

    @Test
    public void testSerialization() {
        Index index = createIndex();
        String serialized = DataMapper.serialize(index);
        Index deserialized = DataMapper.deserialize(serialized, Index.class);
        assertThat(deserialized.getEntries().size(), is(4));
        for (Map.Entry<String, WordKey> indexEntry : deserialized.getEntries()) {
            String key = indexEntry.getKey();
            WordKey wordKey = index.getWord(key);
            assertThat(indexEntry.getValue(), equalTo(wordKey));
        }
    }

    private Index createIndex() {
        Index index = new Index();
        {
            String key = "c9444c55-a2f1-492a-80d7-6d3d72126a8c";
            WordKey wordKey = new WordKey("afraid", PartOfSpeech.Adjective, 1);
            index.addWord(key, wordKey);
        }
        {
            String key = "2acefdda-d692-41f5-b5f8-a3fcd95db7e9";
            WordKey wordKey = new WordKey("agree", PartOfSpeech.Verb, 1);
            index.addWord(key, wordKey);
        }
        {
            String key = "f543c9dd-ed97-4615-99ff-7d992fadd092";
            WordKey wordKey = new WordKey("angry", PartOfSpeech.Adjective, 1);
            index.addWord(key, wordKey);
        }
        {
            String key = "2f9487a8-fd02-4cd6-b90d-4ae51745917b";
            WordKey wordKey = new WordKey("arrive", PartOfSpeech.Verb, 1);
            index.addWord(key, wordKey);
        }
        return index;
    }

}
