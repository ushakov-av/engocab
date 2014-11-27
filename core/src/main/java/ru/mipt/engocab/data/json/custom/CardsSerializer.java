package ru.mipt.engocab.data.json.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.LearnCard;


import static ru.mipt.engocab.data.json.custom.Fields.*;

import java.io.IOException;

/**
 * @author Alexander Ushakov
 */
public class CardsSerializer extends JsonSerializer<Cards> {

    @Override
    public void serialize(Cards cards, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeFieldName(LEARNT_CARDS);
        jgen.writeStartArray();
        for (LearnCard card : cards.getLearnt()) {
            serializeCard(jgen, card);
        }
        jgen.writeEndArray();
        jgen.writeFieldName(ACTIVE_CARDS);
        jgen.writeStartArray();
        for (LearnCard card : cards.getActive()) {
            serializeCard(jgen, card);
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    public void serializeCard(JsonGenerator jgen, LearnCard card) throws IOException {
        jgen.writeStartObject();
        {
            jgen.writeStringField(RECORD_ID, card.getRecordId());
            jgen.writeStringField(STATUS, card.getStatus().toString());
            jgen.writeNumberField(N, card.getN());
            jgen.writeNumberField(L, card.getL());
            jgen.writeNumberField(LR, card.getLr());
        }
        jgen.writeEndObject();
    }
}
