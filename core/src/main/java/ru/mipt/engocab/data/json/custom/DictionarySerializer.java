package ru.mipt.engocab.data.json.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.mipt.engocab.core.model.*;

import static ru.mipt.engocab.data.json.custom.Fields.*;

import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Ushakov
 */
public class DictionarySerializer extends JsonSerializer<Dictionary> {

    @Override
    public void serialize(Dictionary dictionary, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        jgen.writeFieldName(DICTIONARY);
        jgen.writeStartObject();
        {
            for (WordContainer container : dictionary.getContainers()) {
                WordKey wordKey = container.getWordKey();
                String key = wordKey.getWord() + "|" + PartOfSpeech.value(wordKey.getPartOfSpeech()) + "|" + wordKey.getNumber();
                jgen.writeFieldName(key);
                jgen.writeStartObject();
                {
                    serializeWord(wordKey, jgen);

                    List<WordRecord> records = container.getRecords();
                    records.sort((WordRecord r1, WordRecord r2) -> Integer.compare(r1.getIndex(), r2.getIndex()));
                    jgen.writeFieldName(RECORDS);
                    jgen.writeStartArray();
                    {
                        for (WordRecord record : records) {
                            jgen.writeStartObject();
                            {
                                jgen.writeStringField(ID, record.getId());
                                jgen.writeNumberField(INDEX, record.getIndex());
                                jgen.writeStringField(TRANSLATION, record.getTranslation());
                                jgen.writeStringField(TIP, record.getTip());
                                jgen.writeStringField(DESCRIPTION, record.getDescription());

                                // Examples
                                jgen.writeFieldName(EXAMPLES);
                                jgen.writeStartArray();
                                {
                                    for (Example example : record.getExamples()) {
                                        serializeExample(example, jgen);
                                    }
                                }
                                jgen.writeEndArray();

                                // Synonyms
                                jgen.writeFieldName(SYNONYMS);
                                jgen.writeStartArray();
                                {
                                    for (Synonym synonym : record.getSynonyms()) {
                                    }
                                }
                                jgen.writeEndArray();

                                // Tags
                                jgen.writeFieldName(TAGS);
                                jgen.writeStartArray();
                                {
                                    for (String tag : record.getTags()) {
                                        jgen.writeString(tag);
                                    }
                                }
                                jgen.writeEndArray();
                            }
                            jgen.writeEndObject();
                        }
                    }
                    jgen.writeEndArray();
                }
                jgen.writeEndObject();
            }
        }
        jgen.writeEndObject();
        jgen.writeEndObject();

    }

    private void serializeWord(WordKey wordKey, JsonGenerator jgen) throws IOException {
        jgen.writeStringField(WORD, wordKey.getWord());
        jgen.writeStringField(POS, wordKey.getPartOfSpeech().toString());
        jgen.writeNumberField(NUMBER, wordKey.getNumber());
        jgen.writeStringField(TRANSCRIPTION, wordKey.getTranscription());
    }

    private void serializeExample(Example example, JsonGenerator jgen) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(EX_WORD, example.getWordExample());
        jgen.writeStringField(EX_TRANS, example.getTranslationExample());
        jgen.writeStringField(EX_PHRASE, example.getPhrase());
        jgen.writeEndObject();
    }
}
