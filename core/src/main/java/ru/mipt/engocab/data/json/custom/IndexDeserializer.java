package ru.mipt.engocab.data.json.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.study.Index;

import static ru.mipt.engocab.data.json.custom.Fields.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Alexander Ushakov
 */
public class IndexDeserializer extends JsonDeserializer<Index> {

    @Override
    public Index deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Index index = new Index();
        JsonNode root = jp.getCodec().readTree(jp);
        JsonNode indexesNode = root.get(INDEX);
        Iterator<String> indexesIterator = indexesNode.fieldNames();
        while (indexesIterator.hasNext()) {
            String indexKey = indexesIterator.next();
            JsonNode indexNode = indexesNode.get(indexKey);
            String wordText = indexNode.get(WORD).asText();
            PartOfSpeech pos = PartOfSpeech.toPartOfSpeech(indexNode.get(POS).asText());
            int numberField = indexNode.get(NUMBER).asInt();
            WordKey wordKey = new WordKey(wordText, pos, numberField);
            index.addWord(indexKey, wordKey);
        }
        return index;
    }
}
