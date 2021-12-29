package com.lazyboyprod.kafka.mapper;

import com.lazyboyprod.avro.generated.Context;
import com.lazyboyprod.avro.generated.Message;
import com.lazyboyprod.kafka.model.KafkaMessage;

import java.nio.ByteBuffer;

public class AvroMessageMapper implements Mapper<KafkaMessage, Message>{

    @Override
    public Message map(KafkaMessage source) {
        KafkaMessage.Context sourceContext = source.getContext();
        Context context = new Context(sourceContext.getFrontend(), sourceContext.getBusiness(), sourceContext.getCountry());

        Message.Builder builder = Message.newBuilder()
                .setId(source.getId())
                .setGeneration(source.getGeneration())
                .setTimestamp(source.getTimestamp())
                .setModel(source.getModel())
                .setVersion(source.getVersion())
                .setContext(context)
                .setEntryPoint(source.getEntryPoint());

        byte[] data = source.getData();
        if (data != null) {
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();

            builder.setData(buffer);
        } else {
            builder.setData(null);
        }

        return builder.build();
    }

    @Override
    public KafkaMessage unmap(Message source) {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setId(String.valueOf(source.getId()));
        kafkaMessage.setGeneration(source.getGeneration());
        kafkaMessage.setTimestamp(source.getTimestamp());
        kafkaMessage.setEntryPoint(String.valueOf(source.getEntryPoint()));

        Context context = source.getContext();
        KafkaMessage.Context resultContext = new KafkaMessage.Context();
        resultContext.setFrontend(String.valueOf(context.getFrontend()));
        resultContext.setBusiness(String.valueOf(context.getBisuness()));
        resultContext.setCountry(String.valueOf(context.getCountry()));

        kafkaMessage.setContext(resultContext);

        ByteBuffer data = source.getData();
        if (data != null) {
            byte[] bytes = new byte[data.remaining()];
            data.get(bytes);
            kafkaMessage.setData(bytes);
        }

        if (source.getModel() != null) {
            kafkaMessage.setModel(String.valueOf(source.getModel()));
        }
        if (source.getVersion() != null) {
            kafkaMessage.setVersion(String.valueOf(source.getVersion()));
        }

        return kafkaMessage;
    }

}
