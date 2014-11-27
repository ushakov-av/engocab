package ru.mipt.engocab.data.json;

import org.junit.Test;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Ushakov
 */
public class CardsDeserializationTest {

    @Test
    public void testDeserialization() {
        InputStream stream = DictionaryDeserializationTest.class.getResourceAsStream("/serialized_cards.json");
        Cards cards = DataMapper.deserializeFromStream(stream, Cards.class);

        List<LearnCard> learnt = cards.getLearnt();
        assertThat(learnt.size(), is(1));
        {
            LearnCard card = learnt.get(0);
            assertThat(card.getRecordId(), equalTo("97dfce47-a474-4a0b-a351-f4f332993068"));
            assertThat(card.getStatus(), is(Status.LEARNT));
            assertThat(card.getN(), is(20));
            assertThat(card.getL(), is(20));
            assertThat(card.getLr(), is(20));
        }
        List<LearnCard> active = cards.getActive();
        assertThat(active.size(), is(2));
        {
            LearnCard card = active.get(0);
            assertThat(card.getRecordId(), equalTo("c9444c55-a2f1-492a-80d7-6d3d72126a8c"));
            assertThat(card.getStatus(), is(Status.ACTIVE));
            assertThat(card.getN(), is(20));
            assertThat(card.getL(), is(0));
            assertThat(card.getLr(), is(0));
        }
        {
            LearnCard card = active.get(1);
            assertThat(card.getRecordId(), equalTo("f543c9dd-ed97-4615-99ff-7d992fadd092"));
            assertThat(card.getStatus(), is(Status.ACTIVE));
            assertThat(card.getN(), is(20));
            assertThat(card.getL(), is(0));
            assertThat(card.getLr(), is(0));
        }
    }
}
