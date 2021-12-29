package com.lazyboyprod.gateway.mapper;

import com.lazyboyprod.gateway.model.Message;
import com.lazyboyprod.kafka.mapper.Mapper;
import com.lazyboyprod.kafka.model.KafkaMessage;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageMapper implements Mapper<Message, KafkaMessage> {

    @Override
    public KafkaMessage map(Message source) {
        KafkaMessage kafkaMessage = new KafkaMessage();

        kafkaMessage.setId(source.getId());
        kafkaMessage.setGeneration(source.getGeneration());
        kafkaMessage.setTimestamp(source.getTimestamp());
        kafkaMessage.setModel(source.getModel());
        kafkaMessage.setVersion(source.getVersion());

        kafkaMessage.setEntryPoint(source.getEntryPoint());

        KafkaMessage.Context context = new KafkaMessage.Context();
        context.setFrontend(source.getContext().getFrontend());
        context.setBusiness(source.getContext().getBusiness());
        context.setCountry(source.getContext().getCountry());

        kafkaMessage.setContext(context);
        kafkaMessage.setData(source.getData());
        return kafkaMessage;
    }

    @Override
    public Message unmap(KafkaMessage source) {
        return null;
    }


}
