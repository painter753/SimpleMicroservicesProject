package com.lazyboyprod.processor.service;

import com.lazyboyprod.processor.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProcessor {

    public void process(Message message) {
        log.info("Getting new message={}", message);
    }


}
