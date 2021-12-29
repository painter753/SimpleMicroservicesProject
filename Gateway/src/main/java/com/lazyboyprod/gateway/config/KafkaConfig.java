package com.lazyboyprod.gateway.config;

import com.lazyboyprod.gateway.properties.KafkaProperties;
import com.lazyboyprod.kafka.model.KafkaMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    private final Serde<KafkaMessage> kafkaMessageSerde;

    public KafkaConfig(Serde<KafkaMessage> kafkaMessageSerde) {
        this.kafkaMessageSerde = kafkaMessageSerde;
    }

    @Bean("producer-kafka-properties")
    public Properties getProducerProperties(KafkaProperties kafkaProperties) {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 32_768);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 67_108_864);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 4_000);

        properties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getApplicationId());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        return properties;
    }

    @Bean("kafka-producer")
    public KafkaProducer<String, KafkaMessage> getProducer(@Qualifier("producer-kafka-properties") Properties properties) {
        return new KafkaProducer<String, KafkaMessage>(properties, Serdes.String().serializer(), kafkaMessageSerde.serializer());
    }
}
