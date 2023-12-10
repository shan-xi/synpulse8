package com.synpulse8.ebanking.test.kafka;

import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.transaction.dto.TransactionData;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Tag(name = "Test - Kafka Simulation", description = "Kafka Simulation APIs")
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

    @GetMapping("/send-transaction-message")
    public String sendTransactionMessage() {
        producerService.sendTransactionMessage(
                TransactionData.builder()
                        .currency(Currency.CHF)
                        .amount(100.0)
                        .iban("CH93-0000-0000-0000-0000-0")
                        .valueDate(new Date())
                        .description("Online payment CHF")
                        .accountUid("P-0123456789")
                        .build());
        return "Message sent to Kafka topic.";
    }
}
