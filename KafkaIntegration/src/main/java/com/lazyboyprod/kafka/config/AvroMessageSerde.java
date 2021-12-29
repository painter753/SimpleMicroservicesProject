package com.lazyboyprod.kafka.config;

import com.lazyboyprod.avro.generated.Message;
import com.lazyboyprod.kafka.mapper.AvroMessageMapper;
import com.lazyboyprod.kafka.model.KafkaMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Setter
public class AvroMessageSerde implements Serde<KafkaMessage>, Serializer<KafkaMessage>, Deserializer<KafkaMessage> {

    private AvroMessageMapper avroMessageMapper;

    private static byte[] EMPTY_BYTE_ARRAY = {};

    private static final ThreadLocal<ByteArrayOutputStream> outputStreamThreadLocal = ThreadLocal.withInitial(ByteArrayOutputStream::new);
    private static final ThreadLocal<BinaryEncoder> binaryEncoderThreadLocal = ThreadLocal.withInitial(() -> EncoderFactory.get().binaryEncoder(outputStreamThreadLocal.get(), null));
    private static final ThreadLocal<BinaryDecoder> binaryDecoderThreadLocal = ThreadLocal.withInitial(() -> DecoderFactory.get().binaryDecoder(EMPTY_BYTE_ARRAY, null));

    private SpecificDatumReader<Message> specificDatumReader = new SpecificDatumReader<>(Message.class);
    private SpecificDatumWriter<Message> specificDatumWriter = new SpecificDatumWriter<>(Message.class);

    @Override
    public KafkaMessage deserialize(String s, byte[] bytes) {
        try {
            if(bytes.length < 1) {
                KafkaMessage kafkaMessage = new KafkaMessage();
                kafkaMessage.setContext(new KafkaMessage.Context());
                return kafkaMessage;
            }

            Message message = specificDatumReader.read(null, DecoderFactory.get().binaryDecoder(bytes, binaryDecoderThreadLocal.get()));
            return avroMessageMapper.unmap(message);
        } catch (IOException e) {
            log.error("Error read data={}", bytes, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, KafkaMessage kafkaMessage) {
        try {
            Message message = avroMessageMapper.map(kafkaMessage);

            ByteArrayOutputStream byteArrayOutputStream = outputStreamThreadLocal.get();
            byteArrayOutputStream.reset();

            BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, binaryEncoderThreadLocal.get());
            specificDatumWriter.write(message, binaryEncoder);

            binaryEncoder.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error write data={}", kafkaMessage, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<KafkaMessage> serializer() {
        return this;
    }

    @Override
    public Deserializer<KafkaMessage> deserializer() {
        return this;
    }
}
