package com.lazyboyprod.kafka.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KafkaEvent {

    private String id;
    private Meta meta;

    @Getter
    @Setter
    @ToString
    public static class Meta {

        private Integer contentType;
        private String model;
        private Version version;

        @Getter
        @Setter
        @ToString
        public static class Version {

            private Long api;
            private Long object;
            private String model;

        }

    }

    private String userId;
    private String frontend;
    private String business;
    private String type;
    private String country;
    private String language;
    private byte[] data;
    private Integer generation;
    private long timestamp;
    private String model;
    private String modelVersion;
    private String entryPoint;

}
