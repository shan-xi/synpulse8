package com.synpulse8.ebanking.test.kafka;

import com.synpulse8.ebanking.test.kafka.dto.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTransactionTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTransactionTemplate.send(topic, message);
        log.info("Message sent to topic: " + topic);
    }

    public void sendTransactionMessage(TransactionRecord TransactionRecord) {
        var transactionId = UUID.randomUUID().toString();
        kafkaTransactionTemplate.send("transaction-record-listener-topic", transactionId, TransactionRecord);
        log.info("Message sent to topic:{}, transactionId:{}, message:{}", "transaction-record-listener-topic", transactionId, TransactionRecord);
    }


}
