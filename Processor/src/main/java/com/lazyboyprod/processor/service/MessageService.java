package com.lazyboyprod.processor.service;

import com.lazyboyprod.kafka.model.KafkaMessage;
import com.lazyboyprod.processor.mapper.KafkaMessageMapper;
import com.lazyboyprod.processor.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MessageService {

    private ApplicationContext context;
    private KafkaMessageMapper messageMapper;
    private MessageProcessor messageProcessor;

    public MessageService(ApplicationContext context, KafkaMessageMapper messageMapper, MessageProcessor messageProcessor) {
        this.context = context;
        this.messageMapper = messageMapper;
        this.messageProcessor = messageProcessor;
    }

    public void start() {
            KafkaConsumer<String, KafkaMessage> consumer = context.getBean("kafka-consumer", KafkaConsumer.class);
            consumer.subscribe(Collections.singleton("public_messages"));

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                Message message = null;
                while (true) {
                    log.info("Try to fetch records");
                    try {
                        ConsumerRecords<String, KafkaMessage> records = consumer.poll(Duration.ofMillis(100));
                        for (ConsumerRecord<String, KafkaMessage> record : records) {
                            log.info("Get new consumer record={}", record);

                            message = messageMapper.map(record.value());
                            messageProcessor.process(message);
                        }
                    } catch (Exception e) {
                        log.error("Error occurred while try to get records", e);
                    }
                    Thread.sleep(3000);
                }
            });
    }
}
