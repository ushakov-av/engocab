package ru.mipt.engocab.data.json;

import org.junit.Test;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Ushakov
 */
public class CardsSerializationTest {

    @Test
    public void testSerialization() {
        Cards cards = createCards();
        String serialized = DataMapper.serialize(cards);
        System.out.println(serialized);

        Cards deserializedCards = DataMapper.deserialize(serialized, Cards.class);
        assertCards(deserializedCards);
    }


    private Cards createCards() {
        Cards cards = new Cards();
        {
            LearnCard card = new LearnCard("97dfce47-a474-4a0b-a351-f4f332993068", Status.LEARNT, 20, 20, 20);
            cards.addLearnt(card);
        }
        {
            LearnCard card = new LearnCard("c9444c55-a2f1-492a-80d7-6d3d72126a8c", Status.ACTIVE, 20, 0, 0);
            cards.addActive(card);
        }
        {
            LearnCard card = new LearnCard("f543c9dd-ed97-4615-99ff-7d992fadd092", Status.ACTIVE, 20, 0, 0);
            cards.addActive(card);
        }
        return cards;
    }

    private void assertCards(Cards cards) {
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
