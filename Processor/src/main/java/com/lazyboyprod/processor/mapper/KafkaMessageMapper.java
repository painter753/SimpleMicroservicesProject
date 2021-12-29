package com.lazyboyprod.processor.mapper;

import com.lazyboyprod.kafka.mapper.Mapper;
import com.lazyboyprod.kafka.model.KafkaMessage;
import com.lazyboyprod.processor.model.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class KafkaMessageMapper implements Mapper<KafkaMessage, Message> {

    @Override
    public KafkaMessage unmap(Message source) {
        return null;
    }

    @Override
    public Message map(KafkaMessage source) {
        Message message = new Message();

        message.setId(source.getId());
        message.setGeneration(source.getGeneration());
        message.setTimestamp(source.getTimestamp());
        message.setModel(source.getModel());
        message.setVersion(source.getVersion());


        if (source.getData() != null) {
            String s = new String(source.getData(), StandardCharsets.UTF_8);
            message.setPayload(s);
        }

        Message.Context context = new Message.Context();
        context.setFrontend(source.getContext().getFrontend());
        context.setBusiness(source.getContext().getBusiness());
        context.setCountry(source.getContext().getCountry());

        message.setContext(context);
        return message;
    }

    public static void main(String[] args) {
        String s = "hello"; //104, 101, 108, 108, 111
        System.out.println(Arrays.toString(s.getBytes()));
        System.out.println(new String(s.getBytes(), StandardCharsets.UTF_8));
    }
}
