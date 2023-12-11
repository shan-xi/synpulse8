package com.synpulse8.ebanking.test.kafka;

import com.synpulse8.ebanking.test.kafka.dto.TransactionRecord;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test - Kafka Simulation", description = "Kafka Simulation APIs")
@RequestMapping("/test/kafka")
@RestController
public class KafkaController {
    @Autowired
    private KafkaProducerService producerService;

    @PostMapping("/send-transaction-message")
    public String sendTransactionMessage(
            @RequestBody
            TransactionRecord TransactionRecord) {
        producerService.sendTransactionMessage(TransactionRecord);
        return "Send Transaction Message Success";
    }
}
