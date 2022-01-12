package com.lazyboyprod.gateway.controller;

import com.lazyboyprod.gateway.dao.KafkaDao;
import com.lazyboyprod.gateway.mapper.KafkaEventMapper;
import com.lazyboyprod.kafka.model.KafkaEvent;
import com.lazyboyprod.kafka.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MessageController {

    private KafkaDao kafkaDao;
    private KafkaEventMapper kafkaEventMapper;

    public MessageController(KafkaDao kafkaDao, KafkaEventMapper kafkaEventMapper) {
        this.kafkaDao = kafkaDao;
        this.kafkaEventMapper = kafkaEventMapper;
    }

    @GetMapping("/status")
    public String status() {
        return "OK";
    }

    @PostMapping("/v1/message")
    public void getMessage(@RequestBody com.lazyboyprod.gateway.model.v1.Message message) {
        log.info("Getting new v1 message {}", message);
        KafkaEvent event = kafkaEventMapper.map(message);
        kafkaDao.save(event);

    }

    @PostMapping("/v2/message")
    public void getMessage(@RequestBody com.lazyboyprod.gateway.model.v2.Message message) {
        log.info("Getting new v2 message {}", message);
        KafkaEvent event = kafkaEventMapper.map(message);
        kafkaDao.save(event);

    }

}
