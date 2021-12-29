package com.lazyboyprod.kafka.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class KafkaMessage {

    @EqualsAndHashCode.Include
    private String id;
    private Context context;
    private Integer generation;
    private long timestamp;
    private String model;
    private String version;

    private String entryPoint;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class Context {

        private String frontend;
        private String business;
        private String country;

    }

    private byte[] data;

}
