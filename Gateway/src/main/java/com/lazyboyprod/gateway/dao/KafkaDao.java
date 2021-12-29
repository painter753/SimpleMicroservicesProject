package com.lazyboyprod.gateway.dao;

import com.lazyboyprod.gateway.mapper.KafkaMessageMapper;
import com.lazyboyprod.gateway.model.Message;
import com.lazyboyprod.kafka.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaDao {

    private final KafkaProducer<String, KafkaMessage> kafkaProducer;
    private final KafkaMessageMapper kafkaMessageMapper;

    private static final String topic = "public_messages";


    public KafkaDao(@Qualifier("kafka-producer") KafkaProducer<String, KafkaMessage> kafkaProducer, KafkaMessageMapper kafkaMessageMapper) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageMapper = kafkaMessageMapper;
    }

    public void save(Message message) {
        KafkaMessage dto = kafkaMessageMapper.map(message);
        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>(topic, message.getId(), dto);
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Error during produce record={}", record, exception);
            } else {
                if (metadata != null) {
                    log.info("Success produced record={}, for message={}, metadata={}", record, dto, metadata);
                } else {
                    log.warn("Warning no metadata for producing record={}", record);
                }
            }
        });
    }

}
