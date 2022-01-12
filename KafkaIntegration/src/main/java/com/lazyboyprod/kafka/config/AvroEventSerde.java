package com.lazyboyprod.kafka.config;

import com.lazyboyprod.avro.generated.Event;
import com.lazyboyprod.avro.generated.Message;
import com.lazyboyprod.kafka.mapper.AvroEventMapper;
import com.lazyboyprod.kafka.model.KafkaEvent;
import com.lazyboyprod.kafka.model.KafkaMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
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
public class AvroEventSerde implements Serde<KafkaEvent>, Serializer<KafkaEvent>, Deserializer<KafkaEvent> {

    private AvroEventMapper avroEventMapper;

    private static byte[] EMPTY_BYTE_ARRAY = {};

    private static final ThreadLocal<ByteArrayOutputStream> outputStreamThreadLocal = ThreadLocal.withInitial(ByteArrayOutputStream::new);
    private static final ThreadLocal<BinaryEncoder> binaryEncoderThreadLocal = ThreadLocal.withInitial(() -> EncoderFactory.get().binaryEncoder(outputStreamThreadLocal.get(), null));
    private static final ThreadLocal<BinaryDecoder> binaryDecoderThreadLocal = ThreadLocal.withInitial(() -> DecoderFactory.get().binaryDecoder(EMPTY_BYTE_ARRAY, null));

    private SpecificDatumReader<Event> specificDatumReader = new SpecificDatumReader<>(Event.class);
    private SpecificDatumWriter<Event> specificDatumWriter = new SpecificDatumWriter<>(Event.class);

    @Override
    public KafkaEvent deserialize(String s, byte[] bytes) {
        try {
            if(bytes.length < 1) {
                KafkaEvent kafkaMessage = new KafkaEvent();
                kafkaMessage.setMeta(new KafkaEvent.Meta());
                return kafkaMessage;
            }

            Event event = specificDatumReader.read(null, DecoderFactory.get().binaryDecoder(bytes, binaryDecoderThreadLocal.get()));
            return avroEventMapper.unmap(event);
        } catch (IOException e) {
            log.error("Error read data={}", bytes, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, KafkaEvent kafkaEvent) {
        try {
            Event event = avroEventMapper.map(kafkaEvent);

            ByteArrayOutputStream byteArrayOutputStream = outputStreamThreadLocal.get();
            byteArrayOutputStream.reset();

            BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, binaryEncoderThreadLocal.get());
            specificDatumWriter.write(event, binaryEncoder);

            binaryEncoder.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error write data={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<KafkaEvent> serializer() {
        return this;
    }

    @Override
    public Deserializer<KafkaEvent> deserializer() {
        return this;
    }
}
