package com.lazyboyprod.gateway.model.v2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

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
    public static class Context {

        private String country;

    }

    private byte[] data;

}
