package com.lazyboyprod.gateway.controller;

import com.lazyboyprod.gateway.dao.KafkaDao;
import com.lazyboyprod.gateway.model.Message;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MessageController {

    private KafkaDao kafkaDao;

    public MessageController(KafkaDao kafkaDao) {
        this.kafkaDao = kafkaDao;
    }

    @GetMapping("/status")
    public String status() {
        return "OK";
    }

    @PostMapping("/message")
    public void getMessage(@RequestBody Message message) {
        log.info("Getting new message {}", message);

        kafkaDao.save(message);

    }

}
