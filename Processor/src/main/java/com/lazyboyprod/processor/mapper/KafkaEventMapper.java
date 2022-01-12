package com.lazyboyprod.processor.mapper;

import com.lazyboyprod.kafka.model.KafkaEvent;
import com.lazyboyprod.processor.model.Message;

import java.nio.charset.StandardCharsets;


public class KafkaEventMapper {

    public Message map(KafkaEvent source) {
        Message message = new Message();

        message.setId(source.getId());
        message.setGeneration(source.getGeneration());
        message.setTimestamp(source.getTimestamp());
        message.setModel(source.getModel());
        message.setVersion(source.getModelVersion());

        if (source.getData() != null) {
            String s = new String(source.getData(), StandardCharsets.UTF_8);
            message.setPayload(s);
        }

        Message.Context context = new Message.Context();
        context.setFrontend(source.getFrontend());
        context.setBusiness(source.getBusiness());
        context.setCountry(source.getCountry());

        message.setContext(context);
        return message;
    }
}
