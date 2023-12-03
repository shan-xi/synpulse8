package com.synpulse8.ebanking.test.kafka;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test - Kafka", description = "Kafka management APIs")
@RequestMapping("/test/kafka")
@RestController
public class KafkaController {
    @Autowired
    private KafkaProducerService producerService;

    @GetMapping("/send")
    public String sendMessage() {
        producerService.sendMessage("test-topic", "Hello, Kafka!");
        return "Message sent to Kafka topic.";
    }
}
