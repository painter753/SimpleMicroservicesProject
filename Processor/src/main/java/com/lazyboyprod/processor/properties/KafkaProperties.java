package com.lazyboyprod.processor.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("application.kafka")
@Getter
@Setter
@ToString
@Component
public class KafkaProperties {

    private String applicationId;
    private String bootstrapServers;

}
