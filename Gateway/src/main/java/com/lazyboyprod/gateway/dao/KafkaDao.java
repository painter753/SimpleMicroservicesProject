package com.lazyboyprod.gateway.dao;

import com.lazyboyprod.gateway.mapper.KafkaMessageMapper;
import com.lazyboyprod.gateway.model.v1.Message;
import com.lazyboyprod.kafka.model.KafkaEvent;
import com.lazyboyprod.kafka.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaDao {

    private final KafkaProducer<String, KafkaEvent> kafkaProducer;

    private static final String topic = "public_messages";

    public KafkaDao(@Qualifier("kafka-event-producer") KafkaProducer<String, KafkaEvent> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void save(KafkaEvent event) {
        ProducerRecord<String, KafkaEvent> record = new ProducerRecord<>(topic, event.getId(), event);
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Error during produce record={}", record, exception);
            } else {
                if (metadata != null) {
                    log.info("Success produced record={}, for event={}, metadata={}", record, event, metadata);
                } else {
                    log.warn("Warning no metadata for producing record={}", record);
                }
            }
        });
    }

}
