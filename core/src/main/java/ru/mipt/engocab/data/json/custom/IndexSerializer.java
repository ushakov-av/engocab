package ru.mipt.engocab.data.json.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.mipt.engocab.core.model.PartOfSpeech;
import ru.mipt.engocab.core.model.WordKey;
import ru.mipt.engocab.core.model.study.Index;

import static ru.mipt.engocab.data.json.custom.Fields.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Ushakov
 */
public class IndexSerializer extends JsonSerializer<Index> {

    @Override
    public void serialize(Index index, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        {
            jgen.writeFieldName(INDEX);
            jgen.writeStartObject();
            {
                List<Map.Entry<String, WordKey>> list = new ArrayList<>(index.getEntries());
                Collections.sort(list, (e1, e2) -> e1.getKey().compareTo(e2.getKey()));
                for (Map.Entry<String, WordKey> entry : list) {
                    jgen.writeFieldName(entry.getKey());
                    jgen.writeStartObject();
                    {
                        WordKey wordKey = entry.getValue();
                        jgen.writeStringField(WORD, wordKey.getWord());
                        jgen.writeStringField(POS, PartOfSpeech.value(wordKey.getPartOfSpeech()));
                        jgen.writeNumberField(NUMBER, wordKey.getNumber());
                    }
                    jgen.writeEndObject();
                }
            }
            jgen.writeEndObject();
        }
        jgen.writeEndObject();
    }

}
