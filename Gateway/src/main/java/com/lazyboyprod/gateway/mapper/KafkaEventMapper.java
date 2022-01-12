package com.lazyboyprod.gateway.mapper;

import com.lazyboyprod.kafka.model.KafkaEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventMapper {
    public KafkaEvent map(com.lazyboyprod.gateway.model.v1.Message  source) {
        KafkaEvent kafkaEvent = new KafkaEvent();

        kafkaEvent.setId(source.getId());

        KafkaEvent.Meta meta = new KafkaEvent.Meta();
        meta.setContentType(1);
        meta.setModel(source.getModel());

        KafkaEvent.Meta.Version version = new KafkaEvent.Meta.Version();
        version.setApi(1L);
        version.setObject(2L);
        version.setModel(source.getVersion());
        meta.setVersion(version);

        kafkaEvent.setMeta(meta);
        kafkaEvent.setUserId(source.getId());
        kafkaEvent.setFrontend(source.getContext().getFrontend());
        kafkaEvent.setBusiness(source.getContext().getBusiness());
        kafkaEvent.setType("event_type");
        kafkaEvent.setCountry(source.getContext().getCountry());
        kafkaEvent.setLanguage(source.getContext().getCountry().toLowerCase());
        kafkaEvent.setData(source.getData());
        kafkaEvent.setGeneration(source.getGeneration());
        kafkaEvent.setTimestamp(source.getTimestamp());
        kafkaEvent.setModel(source.getModel());
        kafkaEvent.setModelVersion(source.getVersion());
        //kafkaEvent.setEntryPoint(null);

        return kafkaEvent;
    }

    public KafkaEvent map(com.lazyboyprod.gateway.model.v2.Message source) {
        KafkaEvent kafkaEvent = new KafkaEvent();

        KafkaEvent.Meta meta = new KafkaEvent.Meta();
        KafkaEvent.Meta.Version version = new KafkaEvent.Meta.Version();
        version.setApi(1L);
        version.setObject(2L);
        meta.setContentType(1);
        meta.setVersion(version);

        kafkaEvent.setId(source.getId());
        kafkaEvent.setMeta(meta);
        kafkaEvent.setUserId(source.getId());

        String entryPoint = source.getEntryPoint();
        kafkaEvent.setBusiness(entryPoint.split("_")[0]);
        kafkaEvent.setFrontend(entryPoint.split("_")[1]);
        kafkaEvent.setType("event_type");
        kafkaEvent.setCountry(source.getContext().getCountry());
        kafkaEvent.setLanguage(source.getContext().getCountry().toLowerCase());
        kafkaEvent.setData(source.getData());
        kafkaEvent.setGeneration(source.getGeneration());
        kafkaEvent.setTimestamp(source.getTimestamp());

        kafkaEvent.setModel(source.getModel());
        kafkaEvent.setModelVersion(source.getVersion());
        //kafkaEvent.setEntryPoint(source.getEntryPoint());

        return kafkaEvent;
    }
}
