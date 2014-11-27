package ru.mipt.engocab.data.json.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.mipt.engocab.core.model.*;

import static ru.mipt.engocab.data.json.custom.Fields.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Alexander Ushakov
 */
public class DictionaryDeserializer extends JsonDeserializer<Dictionary> {

    @Override
    public Dictionary deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        Dictionary dictionary = new Dictionary();

        JsonNode dictionaryNode = node.get(DICTIONARY);
        Iterator<String> fieldNamesIterator = dictionaryNode.fieldNames();
        while (fieldNamesIterator.hasNext()) {
            String fieldName = fieldNamesIterator.next();
            JsonNode containerNode = dictionaryNode.get(fieldName);

            // word
            String wordField = containerNode.get(WORD).asText();
            PartOfSpeech posField = PartOfSpeech.toPartOfSpeech(containerNode.get(POS).asText());
            int numberField = containerNode.get(NUMBER).asInt();

            JsonNode transcriptionFieldNode = containerNode.get(TRANSCRIPTION);
            String transcriptionField = transcriptionFieldNode.isNull() ? null : transcriptionFieldNode.asText();

            WordKey wordKey = new WordKey(wordField, posField, numberField, transcriptionField);

            WordContainer container = new WordContainer(wordKey);
            dictionary.addContainer(container);

            // records
            JsonNode records = containerNode.get(RECORDS);
            Iterator<JsonNode> recordsIterator = records.elements();
            while (recordsIterator.hasNext()) {

                JsonNode recordNode = recordsIterator.next();

                JsonNode idNode = recordNode.get(ID);
                String id = idNode.isNull() ? null : idNode.asText();

                int index = recordNode.get(INDEX).asInt();
                String translation = recordNode.get(TRANSLATION).asText();

                JsonNode tipNode = recordNode.get(TIP);
                String tip = tipNode.isNull() ? null : tipNode.asText();

                JsonNode descriptionNode = recordNode.get(DESCRIPTION);
                String description = descriptionNode.isNull() ? null : descriptionNode.asText();

                WordRecord record = new WordRecord(id, index, wordKey, translation, tip, description);
                container.addRecord(record);

                // examples
                JsonNode examples = recordNode.get(EXAMPLES);
                Iterator<JsonNode> examplesIterator = examples.elements();
                while (examplesIterator.hasNext()) {
                    JsonNode exampleNode = examplesIterator.next();
                    record.addExample(deserializeExample(exampleNode));
                }

                // synonyms

                // tags
                JsonNode tags = recordNode.get(TAGS);
                Iterator<JsonNode> tagsIterator = tags.elements();
                while (tagsIterator.hasNext()) {
                    String tag = tagsIterator.next().asText();
                    record.addTag(tag);
                }
            }
        }

        return dictionary;
    }

    private Example deserializeExample(JsonNode exampleNode) {
        JsonNode exWordNode = exampleNode.get(EX_WORD);
        String exWord = exWordNode.isNull() ? null : exWordNode.asText();

        JsonNode exTransNode = exampleNode.get(EX_TRANS);
        String exTrans = exTransNode.isNull() ? null : exTransNode.asText();

        JsonNode exPhraseNode = exampleNode.get(EX_PHRASE);
        String exPhrase = exPhraseNode.isNull() ? null : exPhraseNode.asText();

        return new Example(exWord, exTrans, exPhrase);
    }


}
