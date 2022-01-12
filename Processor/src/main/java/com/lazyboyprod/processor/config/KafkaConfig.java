package com.lazyboyprod.processor.config;

import com.lazyboyprod.kafka.model.KafkaEvent;
import com.lazyboyprod.processor.properties.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    private final Serde<KafkaEvent> kafkaEventSerde;

    public KafkaConfig(Serde<KafkaEvent> kafkaEventSerde) {
        this.kafkaEventSerde = kafkaEventSerde;
    }

    @Bean("consumer-kafka-properties")
    public Properties getConsumerProperties(KafkaProperties kafkaProperties) {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getApplicationId());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 50);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 100_000);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 33_000);

        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1_000);

        return properties;
    }

    @Bean("kafka-consumer")
    public KafkaConsumer<String, KafkaEvent> getConsumer(@Qualifier("consumer-kafka-properties") Properties properties) {
        return new KafkaConsumer<>(properties, Serdes.String().deserializer(), kafkaEventSerde.deserializer());
    }
}
