package com.lazyboyprod.gateway.mapper;

import com.lazyboyprod.kafka.model.KafkaMessage;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageMapper {

    public KafkaMessage map(com.lazyboyprod.gateway.model.v1.Message  source) {
        KafkaMessage kafkaMessage = new KafkaMessage();

        kafkaMessage.setId(source.getId());
        kafkaMessage.setGeneration(source.getGeneration());
        kafkaMessage.setTimestamp(source.getTimestamp());
        kafkaMessage.setModel(source.getModel());
        kafkaMessage.setVersion(source.getVersion());

        KafkaMessage.Context context = new KafkaMessage.Context();
        context.setFrontend(source.getContext().getFrontend());
        context.setBusiness(source.getContext().getBusiness());
        context.setCountry(source.getContext().getCountry());

        kafkaMessage.setContext(context);
        kafkaMessage.setData(source.getData());
        return kafkaMessage;
    }

    public KafkaMessage map(com.lazyboyprod.gateway.model.v2.Message source) {
        KafkaMessage kafkaMessage = new KafkaMessage();

        kafkaMessage.setId(source.getId());
        kafkaMessage.setGeneration(source.getGeneration());
        kafkaMessage.setTimestamp(source.getTimestamp());
        kafkaMessage.setModel(source.getModel());
        kafkaMessage.setVersion(source.getVersion());

        KafkaMessage.Context context = new KafkaMessage.Context();
        //context.setFrontend(source.getContext().getFrontend());
        //context.setBusiness(source.getContext().getBusiness());
        context.setCountry(source.getContext().getCountry());

        kafkaMessage.setContext(context);
        kafkaMessage.setData(source.getData());
        return kafkaMessage;
    }


}
