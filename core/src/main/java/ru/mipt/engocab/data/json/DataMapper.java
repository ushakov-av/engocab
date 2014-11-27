package ru.mipt.engocab.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.Index;
import ru.mipt.engocab.data.json.custom.CardsDeserializer;
import ru.mipt.engocab.data.json.custom.CardsSerializer;
import ru.mipt.engocab.data.json.custom.DictionaryDeserializer;
import ru.mipt.engocab.data.json.custom.DictionarySerializer;
import ru.mipt.engocab.data.json.custom.IndexDeserializer;
import ru.mipt.engocab.data.json.custom.IndexSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Alexander Ushakov
 */
public final class DataMapper {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Cards.class, new CardsSerializer());
        module.addDeserializer(Cards.class, new CardsDeserializer());
        module.addSerializer(Dictionary.class, new DictionarySerializer());
        module.addDeserializer(Dictionary.class, new DictionaryDeserializer());
        module.addSerializer(Index.class, new IndexSerializer());
        module.addDeserializer(Index.class, new IndexDeserializer());
        OBJECT_MAPPER.registerModule(module);
    }

    private DataMapper() {}

    public static String serialize(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalStateException("Serializing error", e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);

        } catch (IOException e) {
            throw new IllegalStateException("Deserializing message: " + json, e);
        }
    }

    public static <T> T deserializeFromStream(InputStream stream, Class<T> clazz) {
        if (stream == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(stream, clazz);

        } catch (IOException e) {
            throw new IllegalStateException("Deserialization error", e);
        }
    }

}
