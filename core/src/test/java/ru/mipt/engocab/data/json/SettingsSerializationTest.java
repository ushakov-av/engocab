package ru.mipt.engocab.data.json;

import org.junit.Test;
import ru.mipt.engocab.core.config.Settings;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Ushakov
 */
public class SettingsSerializationTest {

    @Test
    public void testDeserialization() {
        InputStream stream = DictionaryDeserializationTest.class.getResourceAsStream("/serialized_settings.json");
        Settings settings = DataMapper.deserializeFromStream(stream, Settings.class);
        assertThat(settings.getCurrentDictionary(), equalTo("cur"));
        assertThat(settings.getDictionariesPath(), equalTo("path"));
    }

    @Test
    public void testEmptySettingsDeserialization() {
        String serialized = "{}";
        Settings settings = DataMapper.deserialize(serialized, Settings.class);
        assertNull(settings.getCurrentDictionary());
        assertNull(settings.getDictionariesPath());
    }

    @Test
    public void testSerialization() {
        final String currentDictionary = "basic";
        final String dictionariesPath = "C:\\users\\Lunix\\.engocab\\dicts";

        Settings settings = new Settings();
        settings.setCurrentDictionary(currentDictionary);
        settings.setDictionariesPath(dictionariesPath);
        String serialized = DataMapper.serialize(settings);
        System.out.println(serialized);

        settings = DataMapper.deserialize(serialized, Settings.class);
        assertThat(settings.getCurrentDictionary(), equalTo(currentDictionary));
        assertThat(settings.getDictionariesPath(), equalTo(dictionariesPath));
    }

}
