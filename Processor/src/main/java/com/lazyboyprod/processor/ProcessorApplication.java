package com.lazyboyprod.processor;

import com.lazyboyprod.processor.service.MessageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProcessorApplication {

    private final MessageService messageService;

    public ProcessorApplication(MessageService messageService) {
        this.messageService = messageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    @Bean
    public CommandLineRunner getRunner() {
        return (args) -> messageService.start();
    }

}
