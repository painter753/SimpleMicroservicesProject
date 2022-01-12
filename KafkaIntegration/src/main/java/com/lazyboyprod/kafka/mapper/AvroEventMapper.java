package com.lazyboyprod.kafka.mapper;

import com.lazyboyprod.avro.generated.Event;
import com.lazyboyprod.avro.generated.Meta;
import com.lazyboyprod.avro.generated.Version;
import com.lazyboyprod.kafka.model.KafkaEvent;

import java.nio.ByteBuffer;

public class AvroEventMapper implements Mapper<KafkaEvent, Event> {
    @Override
    public Event map(KafkaEvent source) {

        Event.Builder builder = Event.newBuilder()
                .setId(source.getId())
                .setBusiness(source.getBusiness())
                .setFrontend(source.getFrontend())
                .setEntryPoint(source.getEntryPoint())
                .setCountry(source.getCountry())
                .setLanguage(source.getLanguage())
                .setGeneration(source.getGeneration())
                .setUserId(source.getUserId())
                .setType(source.getType())
                .setCreateTimestamp(source.getTimestamp())
                .setModel(source.getModel())
                .setModelVersion(source.getModelVersion());

        //prepare the data
        byte[] data = source.getData();
        if (data != null) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);

            byteBuffer.put(data);
            byteBuffer.flip();

            builder.setData(byteBuffer);
        } else {
            builder.setData(null);
        }

        KafkaEvent.Meta sourceMeta = source.getMeta();
        KafkaEvent.Meta.Version sourceVersion = sourceMeta.getVersion();
        Version version = Version.newBuilder()
                .setApi(sourceVersion.getApi())
                .setObject(sourceVersion.getObject())
                .build();

        Meta meta = Meta.newBuilder()
                .setContentType(sourceMeta.getContentType())
                .setVersion(version)
                .build();

        builder.setMeta(meta);

        return builder.build();
    }

    @Override
    public KafkaEvent unmap(Event source) {
        KafkaEvent event = new KafkaEvent();

        event.setId(String.valueOf(source.getId()));
        event.setFrontend(String.valueOf(source.getFrontend()));
        event.setBusiness(String.valueOf(source.getBusiness()));
        event.setType(String.valueOf(source.getType()));
        event.setCountry(String.valueOf(source.getCountry()));
        event.setLanguage(String.valueOf(source.getLanguage()));
        event.setGeneration(source.getGeneration());
        event.setUserId(String.valueOf(source.getUserId()));
        event.setTimestamp(source.getCreateTimestamp());

        ByteBuffer data = source.getData();
        if (data != null) {
            byte[] bytes = new byte[data.remaining()];
            data.get(bytes);
            event.setData(bytes);
        }

        Meta sourceMeta = source.getMeta();
        KafkaEvent.Meta meta = new KafkaEvent.Meta();

        KafkaEvent.Meta.Version version = new KafkaEvent.Meta.Version();
        meta.setVersion(version);
        event.setMeta(meta);
        if (source.getModel() != null) {
            meta.setModel(String.valueOf(source.getModel()));
            event.setModel(String.valueOf(source.getModel()));
        }
        if (source.getModelVersion() != null) {
            version.setModel(String.valueOf(source.getModelVersion()));
            event.setModelVersion(String.valueOf(source.getModelVersion()));
        }
        if (source.getEntryPoint() != null) {
            event.setEntryPoint(String.valueOf(source.getEntryPoint()));
        }

        if (sourceMeta != null) {
            Integer contentType = sourceMeta.getContentType();
            meta.setContentType(contentType);

            Version sourceVersion = sourceMeta.getVersion();
            if (sourceVersion != null) {
                version.setApi(sourceVersion.getApi());
                version.setObject(sourceVersion.getObject());
            }
        }
        return event;
    }
}
