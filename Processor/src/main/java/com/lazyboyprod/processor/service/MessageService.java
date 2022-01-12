package com.lazyboyprod.processor.service;

import com.lazyboyprod.kafka.model.KafkaEvent;
import com.lazyboyprod.kafka.model.KafkaMessage;
import com.lazyboyprod.processor.mapper.KafkaMessageMapper;
import com.lazyboyprod.processor.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MessageService {

    private ApplicationContext context;

    public MessageService(ApplicationContext context) {
        this.context = context;
    }

    public void start() {
            KafkaConsumer<String, KafkaEvent> consumer = context.getBean("kafka-consumer", KafkaConsumer.class);
            consumer.subscribe(Collections.singleton("public_messages"));

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                while (true) {
                    log.info("Try to fetch records");
                    try {
                        ConsumerRecords<String, KafkaEvent> records = consumer.poll(Duration.ofMillis(100));
                        for (ConsumerRecord<String, KafkaEvent> record : records) {
                            log.info("Get new consumer record={}", record);

                            KafkaEvent value = record.value();
                            log.info("Getting new message={}", value);
                        }
                    } catch (Exception e) {
                        log.error("Error occurred while try to get records", e);
                    }
                    Thread.sleep(3000);
                }
            });
    }
}
