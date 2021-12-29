package com.lazyboyprod.kafka.config;

import com.lazyboyprod.kafka.mapper.AvroMessageMapper;
import com.lazyboyprod.kafka.model.KafkaMessage;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Bean("avroMessageSerde")
    public Serde<KafkaMessage> getAvroMessageSerde(AvroMessageMapper avroMessageMapper) {
        AvroMessageSerde avroMessageSerde = new AvroMessageSerde();
        avroMessageSerde.setAvroMessageMapper(avroMessageMapper);
        return avroMessageSerde;
    }

    @Bean
    public AvroMessageMapper avroMessageMapper() {
        return new AvroMessageMapper();
    }


}
