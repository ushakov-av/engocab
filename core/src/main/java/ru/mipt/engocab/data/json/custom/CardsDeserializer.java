package ru.mipt.engocab.data.json.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.LearnCard;
import ru.mipt.engocab.core.model.study.Status;

import static ru.mipt.engocab.data.json.custom.Fields.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Alexander Ushakov
 */
public class CardsDeserializer extends JsonDeserializer<Cards> {

    @Override
    public Cards deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode root = jp.getCodec().readTree(jp);
        Cards cards = new Cards();
        deserializeCards(root.get(LEARNT_CARDS), cards::addLearnt);
        deserializeCards(root.get(ACTIVE_CARDS), cards::addActive);
        return cards;
    }

    private void deserializeCards(JsonNode cardsNode, Consumer<LearnCard> addFunction) {
        Iterator<JsonNode> learntCardsIterator = cardsNode.elements();
        while (learntCardsIterator.hasNext()) {
            JsonNode cardNode = learntCardsIterator.next();
            String recordId = cardNode.get(RECORD_ID).asText();
            Status status = Status.valueOf(cardNode.get(STATUS).asText());
            int n = cardNode.get(N).intValue();
            int l = cardNode.get(L).intValue();
            int lr = cardNode.get(LR).intValue();
            LearnCard card = new LearnCard(recordId, status, n, l, lr);
            addFunction.accept(card);
        }
    }
}
